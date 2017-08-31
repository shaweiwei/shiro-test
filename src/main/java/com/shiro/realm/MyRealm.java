package com.shiro.realm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalMap;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Test;

import com.shiro.authen.MyAuthenticationInfo;
import com.shiro.context.Config;
import com.shiro.dao.impl.UserDaoImpl;
import com.shiro.entity.UserInfo;
import com.shiro.entity.UserRolePermission;
import com.shiro.exception.MyException;
import com.shiro.service.UserService;
import com.shiro.service.impl.UserServiceImpl;
import com.shiro.util.CryptographyUtil;
import com.shiro.util.JdbcHelper;

/**
 * shiro要进行身份验证，就要从realm中获取相应的身份信息来进行验证，简单来说，
 * 我们可以自行定义realm，在realm中，从数据库获取身份信息，然后和 用户输入的身份
 * 信息进行匹配。这一切都由我们自己来定义
 * @author ko
 *
 */
public class MyRealm extends AuthorizingRealm{
	
	UserService userService = new UserServiceImpl(new UserDaoImpl());

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("----------doGetAuthorizationInfo方法被调用----------");
        String username = (String) getAvailablePrincipal(principals);
        // 根据用户名查询该用户的权限、角色
        return getAuthorizationInfoByUsername(username);
    }
    
    /**
     * 获取身份验证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	System.out.println("----------doGetAuthenticationInfo方法被调用----------");
        //输入的用户名
        String username = (String) token.getPrincipal();
        System.out.println("username:"+username);
        //输入的密码
        String password = new String((char[])token.getCredentials());
        System.out.println("password:"+password);
        // 加密后的密码
        String md5Pwd = CryptographyUtil.md5(password, Config.SALT_ADMIN);
        
        //从数据库获取用户信息进行身份判断,这里的验证对应登陆的时候currentUser.login捕获的各种异常
        UserInfo user = userService.findByUsername(username);
        if(user == null){
            throw new UnknownAccountException();
        }
//        if(!user.getPassword().equals(md5Pwd)){ 本来在这里完全可以验证密码是否匹配，不过为了使用RetryLimitHashedCredentialsMatcher，改到这里面的doCredentialsMatch
//            throw new IncorrectCredentialsException();
//        }
        if(user.getIslock() == 1){
        	throw new LockedAccountException();
        }
        if(new Random().nextInt(10) > 8){// 弄个随机条件，测试自定义异常,实际项目中根据业务需求编写
        	throw new MyException();
        }
        
        // 存放用户名密码map PrincipalMap
        SimplePrincipalMap spMap = new SimplePrincipalMap();
        spMap.put("xxxxx", username);
        
        // 验证用户的权限
        List<String> ll = new ArrayList<String>();// 存放
        ll.add("上传文件");
        ll.add("修改文件");
        boolean[] rr = hasRoles(spMap, ll);
        StringBuffer norole = new StringBuffer();
        for (int i = 0; i < rr.length; i++) {
        	if(rr[i] == false){
        		norole.append(ll.get(i)+"/");
        	}
		}
        String noroleStr = norole.toString();
        if(noroleStr.length() > 0){
        	System.out.println(username+"没有"+noroleStr.substring(0, noroleStr.length()-1)+"的权限");
        }
        
        // 验证用户的角色
        List<Permission> pp = new ArrayList<Permission>();
        pp.add(new WildcardPermission("管理员"));
        pp.add(new WildcardPermission("检验员"));
        boolean[] ims = isPermitted(spMap, pp);
        StringBuffer nop = new StringBuffer();
        for (int i = 0; i < ims.length; i++) {
        	if(ims[i] == false){
        		nop.append(pp.get(i).toString()+"/");
        	}
		}
        String nopStr = nop.toString();
        if(nopStr.length() > 0){
        	System.out.println(username+"没有"+nopStr.substring(0, nopStr.length()-1)+"角色");
        }
        
        //身份验证通过,返回一个身份信息
//        SimpleAuthenticationInfo aInfo = new SimpleAuthenticationInfo(username,user.getPassword(),getName());// 存放的是从数据库里获取的密码
        MyAuthenticationInfo aInfo = new MyAuthenticationInfo(username,user.getPassword(),getName());// 其实用上面一个就可以
        
        // 为CredentialsMatcher准备的
        aInfo.setUser(user);
        aInfo.setUserService(userService);
        
        // 一定要设置盐，不然HashedCredentialsMatcher（RetryLimitHashedCredentialsMatcher继承了该类）里
        // 的hashProvidedCredentials(token,info)方法，（该方法是验证输入的密码和库里的密码是否一致）需要用到的时候获取不到
        aInfo.setCredentialsSalt(ByteSource.Util.bytes(Config.SALT_ADMIN));
        
        return aInfo;
    }
    
    /**
     * 获取用户的角色和权限
     * @param username
     * @throws SQLException 
     */
    public SimpleAuthorizationInfo getAuthorizationInfoByUsername(String username){

        List<UserRolePermission> userRolePermissionList = userService.findUserRolePermissions(username);

        // 通过用户名从数据库获取权限/角色信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(userRolePermissionList != null && userRolePermissionList.size() > 0){
        	Set<String> p = new HashSet<String>();//权限
        	Set<String> r = new HashSet<String>();//角色
        	for (UserRolePermission urp : userRolePermissionList) {
        		
                p.add(urp.getPname());
                info.setStringPermissions(p);
                
                r.add(urp.getRname());
                info.setRoles(r);
			}
        }
        
        return info;
    }
    
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
