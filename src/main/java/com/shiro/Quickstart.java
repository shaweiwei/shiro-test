package com.shiro;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import com.shiro.context.Config;
import com.shiro.dao.impl.UserDaoImpl;
import com.shiro.ehcache.factory.SharedEhCacheManagerFactory;
import com.shiro.exception.MyException;
import com.shiro.service.UserService;
import com.shiro.service.impl.UserServiceImpl;
import com.shiro.util.CryptographyUtil;
import com.shiro.util.JdbcHelper;

import net.sf.ehcache.CacheManager;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * 本实例是javase的环境，用到了junit进行测试，不是web,也没用到spring
 * 具体使用到了shiro的下列功能
 * Authentication：身份认证/登录，验证用户是不是拥有相应的身份；
 * Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
 * Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境的，也可以是如Web环境的；
 * Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
 * Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。
 * Caching：缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以提高效率
 * @author ko
 */
public class Quickstart {
	
	String username = "liubei";
	String password = "123456";
	
	@Test
	public void login() {
		//获取SecurityManager的实例
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
			
		if (!currentUser.isAuthenticated()) {// 登陆
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			token.setRememberMe(true);
			try {
				currentUser.login( token );// 用户登陆
			} catch ( UnknownAccountException uae ) {
				System.out.println("系统里没有"+username+"用户，请核查");
			    //username wasn't in the system, show them an error message?
			} catch ( IncorrectCredentialsException ice ) {
			    //password didn't match, try again?
				System.out.println("输入密码不匹配，请重试");
			} catch ( LockedAccountException lae ) {
			    //account for that username is locked - can't login.  Show them a message?
				System.out.println("该账户被锁定，请联系管理员");
			} catch ( ExcessiveAttemptsException er ) {
				// 这里要有一个锁定用户的操作
				System.out.println("密码重试超过5次，该账户已被锁定");
			} catch ( AuthenticationException ae ) {
				System.out.println("未知错误，请联系管理员");
			}  catch (MyException e) {
				// TODO: Custom exception
				System.out.println("这是自定义异常");
			}
			//... more types exceptions to check if you want ...
		}

		if (currentUser.isAuthenticated()){
			System.out.println( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
		}
	    
	    // 测试该用户是否有“检验员”的角色
	    if (currentUser.hasRole("上传文件")) {
			System.out.println("you can upload file");
		} else {
			System.out.println("sorry, you can not upload file");
		}
	    
	    // 测试该用户是否有上传文件的权限许可
//	    Permission permission = new WildcardPermission("filemanage:upload");
	    if (currentUser.isPermitted("检验员")) {
	    	System.out.println("hello，inspector");
		}else {
			System.out.println("hello，tourist");
		}
	    
	    // 注解版测试角色
	    roleByAnnotation();
	    
	    // 注解版测试权限
	    permissionByAnnotation();
	    
	    // isRemembered和isAuthenticated的区别
	    // 是否被记住和是否被认证，举个例子
	    // 昨天我打开淘宝登陆选了一些商品在购物车里，有事情耽搁了电脑一直放在那，网站页面没关，等到今天才想起来准备去付款
	    // 这时isRemembered()还是返回true，购物车里的东西都在，推荐商品也是相关的
	    // 但是isAuthenticated()返回false，因为要付款的时候必须要我在重新登录授权，因为这个时候有可能会是其它人来偷偷操作我的电脑
	    System.out.println("isRemembered："+currentUser.isRemembered());// 
	    System.out.println("isAuthenticated："+currentUser.isAuthenticated());
	    
	    
	    // 会话管理
	    Session session = currentUser.getSession();
	    sessionManageTest(session);
	    
	    // 获取缓存信息
	    getEhCacheAuthorInfo();
	    
	    currentUser.logout(); // 登出，移除所有的识别信息和无效的session
		
	}
	
	@RequiresRoles("上传文件")
	public static void roleByAnnotation(){
		System.out.println("hi, you can upload file");
	}
	
	@RequiresPermissions("管理员")
	public static void permissionByAnnotation(){
		System.out.println("you are manager");
	}
	
	@Test
	public void regist(){
		// 把用户信息插入数据库
		JdbcHelper jdbcHelper = new JdbcHelper();
		jdbcHelper.getConnection();
		String sql = "insert into user (uname,islock,password) values (?,?,?)"; 
        List<Object> params = new ArrayList<Object>(); 
//        params.add(2); id自增
        params.add(username); 
        params.add(18); 
        params.add(CryptographyUtil.md5(password, Config.SALT_ADMIN)); // 存到数据库里的密码一定要加密，而且要加salt(盐)
        try { 
            boolean flag = jdbcHelper.updateByPreparedStatement(sql, params); 
            System.out.println(flag); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
	}
	
	/**
	 * 测试连续尝试登陆超过5次都失败是什么效果，密码一定要改为错误密码
	 */
	@Test
	public void testLoginOver5(){
		password = "1234";
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();

        for (int i = 1; i <= 6; i++) {
			System.out.println("尝试第："+i+"次登陆");
			
			if (!currentUser.isAuthenticated()) {// 登陆
				UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				token.setRememberMe(true);
				try {
					currentUser.login( token );// 用户登陆
				} catch ( UnknownAccountException uae ) {
					System.out.println("系统里没有"+username+"用户，请核查");
				    //username wasn't in the system, show them an error message?
				} catch ( IncorrectCredentialsException ice ) {
				    //password didn't match, try again?
					System.out.println("输入密码不匹配，请重试");
				} catch ( LockedAccountException lae ) {
				    //account for that username is locked - can't login.  Show them a message?
					System.out.println("该账户被锁定，请联系管理员");
				} catch ( ExcessiveAttemptsException er ) {
					System.out.println("密码重试超过5次，该账户已被锁定");
				} catch ( AuthenticationException ae ) {
					System.out.println("未知错误，请联系管理员");
				}  catch (MyException e) {
					// TODO: Custom exception
					System.out.println("这是自定义异常");
				}
				//... more types exceptions to check if you want ...
			}
		}
	}
	
	/**
	 * Shiro提供了完整的企业级会话管理功能，不依赖于底层容器（如web容器tomcat），
	 * 不管JavaSE还是JavaEE环境都可以使用，提供了会话管理、会话事件监听、会话存储/持久化、
	 * 容器无关的集群、失效/过期支持、对Web的透明支持、SSO单点登录的支持等特性。即直接使用Shiro
	 * 的会话管理可以直接替换如Web容器的会话管理。
	 * @param session
	 */
	public void sessionManageTest(Session session){
		System.out.println("---------------sessionManageTest begin----------------");
		System.out.println("获取当前会话的唯一标识:"+session.getId());
		System.out.println("获取当前Subject的主机地址，该地址是通过HostAuthenti"
				+ "cationToken.getHost()提供的:"+session.getHost());
//		session.setTimeout(50000);设置当前Session的过期时间
		System.out.println("获取当前Session的过期时间,如果不设置默认是会话管理器的全局过期时间:"+session.getTimeout());
		System.out.println("获取会话的启动时间及最后访问时间；如果是JavaSE应用需要自己定期调用session.touch()去更新最后"
				+ "访问时间；如果是Web应用，每次进入ShiroFilter都会自动调用session.touch()来更新最后访问时间:"
				+session.getStartTimestamp()+"  "+session.getLastAccessTime());

		// 更新会话最后访问时间及销毁会话；当Subject.logout()时会自动调用stop方法来销毁会话。如果在web中，
		// 调用javax.servlet.http.HttpSession. invalidate()也会自动调用Shiro Session.stop方法进行
		// 销毁Shiro的会话
		session.touch();  
		session.stop();  
		
		// 设置/获取/删除会话属性；在整个会话范围内都可以对这些属性进行操作
		session.setAttribute("key", "123");  
		System.out.println("获取key的值："+session.getAttribute("key"));
//		Assert.assertEquals("123", session.getAttribute("key"));  
		session.removeAttribute("key");  
		
		System.out.println("---------------sessionManageTest end----------------");
	}
	
	/**
	 * 为了测试缓存
	 * 获取EhCache缓存的authorization和authentication
	 */
	public void getEhCacheAuthorInfo(){
		SharedEhCacheManagerFactory cacheManagerFactory = new SharedEhCacheManagerFactory();
		CacheManager cacheManager = cacheManagerFactory.getInstance();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) cacheManager.getCache("authenticationCache");
		AuthorizationInfo authorizationInfo = (AuthorizationInfo) cacheManager.getCache("authenticationCache");
		System.out.println("缓存获取authenticationInfo："+authenticationInfo.toString()+" 缓存获取authorizationInfo："+authorizationInfo.toString());
		cacheManager.clearAllStartingWith("auth");// 清除缓存
	}
	
}
