package com.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.shiro.entity.UserInfo;
import com.shiro.service.UserService;

/**
 * 其实shiro自带的SimpleAuthenticationInfo已经完全满足需求，这边因为我有点个性化的需求，
 * 需要把UserInfo从MyRealm传到RetryLimitHashedCredentialsMatcher的doCredentialsMatch方法里
 * @author ko
 *
 */
public class MyAuthenticationInfo extends SimpleAuthenticationInfo {
	
	public MyAuthenticationInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt,
			String realmName) {
		super(principal, hashedCredentials, credentialsSalt, realmName);
		// TODO Auto-generated constructor stub
	}

	public MyAuthenticationInfo(Object principal, Object credentials, String realmName) {
		super(principal, credentials, realmName);
		// TODO Auto-generated constructor stub
	}

	public MyAuthenticationInfo(PrincipalCollection principals, Object hashedCredentials, ByteSource credentialsSalt) {
		super(principals, hashedCredentials, credentialsSalt);
		// TODO Auto-generated constructor stub
	}

	public MyAuthenticationInfo(PrincipalCollection principals, Object credentials) {
		super(principals, credentials);
		// TODO Auto-generated constructor stub
	}

	private UserInfo user;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
	
}
