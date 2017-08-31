package com.shiro.service;

import java.util.List;
import java.util.Set;

import com.shiro.entity.UserInfo;
import com.shiro.entity.UserRolePermission;

public interface UserService {
	/**
     * 创建用户
     * @param user
     */
    public UserInfo createUser(UserInfo user);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    public void changePassword(int userId, String newPassword);

    /**
     * 添加用户-角色关系
     * @param userId
     * @param roleIds
     */
    public void correlationRoles(int userId, int... roleIds);


    /**
     * 移除用户-角色关系
     * @param userId
     * @param roleIds
     */
    public void uncorrelationRoles(int userId, int... roleIds);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public UserInfo findByUsername(String username);

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
//    public Set<String> findRoles(String username);

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    public List<UserRolePermission> findUserRolePermissions(String username);
    
    /**
     * 设置用户是否锁定
     * @param username
     * @return
     */
    public void setUserIsLock(UserInfo user);
}
