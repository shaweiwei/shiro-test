package com.shiro.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.shiro.dao.UserDao;
import com.shiro.entity.UserInfo;
import com.shiro.entity.UserRolePermission;
import com.shiro.session.dao.BaseDao;

public class UserDaoImpl extends BaseDao implements UserDao{

	public UserInfo createUser(UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateUser(UserInfo user) {
		String sql = "update user set uname=?,islock=?,password=? where id = ? ";  
        List<Object> params = new ArrayList<Object>();  
        params.add(user.getUname());
        params.add(user.getIslock());
        params.add(user.getPassword());
        params.add(user.getId());
        UserInfo userInfo = null;  
        try {  
            jdbcHelper.updateByPreparedStatement(sql, params);
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		
	}

	public void deleteUser(int userId) {
		// TODO Auto-generated method stub
		
	}

	public void correlationRoles(int userId, int... roleIds) {
		// TODO Auto-generated method stub
		
	}

	public void uncorrelationRoles(int userId, int... roleIds) {
		// TODO Auto-generated method stub
		
	}

	public UserInfo findOne(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserInfo findByUsername(String username) {
		String sql = "select * from user where uname = ? ";  
        List<Object> params = new ArrayList<Object>();  
        params.add(username);
        UserInfo userInfo = null;  
        try {  
            userInfo = jdbcHelper.findSimpleRefResult(sql, params, UserInfo.class);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return userInfo;
	}

	public List<UserRolePermission> findUserRolePermissions(String username) {
		String sql = "SELECT a.uname,c.pname,e.rname FROM user as a LEFT JOIN gl_user_permission as b ON a.id = b.user_id LEFT JOIN permission as c ON b.permission_id = c.id LEFT JOIN gl_permission_role as d ON d.permission_id = c.id LEFT JOIN role as e ON e.id = d.role_id WHERE a.uname = ?";
		
		List<Object> params = new ArrayList<Object>();  
        params.add(username);  
        List<UserRolePermission> userRolePermissionList = null;
		try {
			userRolePermissionList = jdbcHelper.findMoreRefResult(sql, params, UserRolePermission.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userRolePermissionList;
	}

}
