package com.shiro.service.impl;

import java.util.List;
import java.util.Set;

import com.shiro.dao.UserDao;
import com.shiro.entity.UserInfo;
import com.shiro.entity.UserRolePermission;
import com.shiro.service.UserService;

public class UserServiceImpl implements UserService {

	UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		super();
		this.userDao = userDao;
	}

	public UserInfo createUser(UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void changePassword(int userId, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	public void correlationRoles(int userId, int... roleIds) {
		// TODO Auto-generated method stub
		
	}

	public void uncorrelationRoles(int userId, int... roleIds) {
		// TODO Auto-generated method stub
		
	}

	public UserInfo findByUsername(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUsername(username);
	}

//	public Set<String> findRoles(String username) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public List<UserRolePermission> findUserRolePermissions(String username) {
		return userDao.findUserRolePermissions(username);
	}

	public void setUserIsLock(UserInfo user) {
		if (user.getIslock() != 0 || user.getIslock() != 1) throw new IllegalArgumentException("user.isLock参数不对");
		userDao.updateUser(user);
	}

}
