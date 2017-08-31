package com.shiro.dao;

import java.util.List;
import java.util.Set;

import com.shiro.entity.UserInfo;
import com.shiro.entity.UserRolePermission;

public interface UserDao{

    public UserInfo createUser(UserInfo user);
    public void updateUser(UserInfo user);
    public void deleteUser(int userId);

    public void correlationRoles(int userId, int... roleIds);
    public void uncorrelationRoles(int userId, int... roleIds);

    UserInfo findOne(int userId);

    UserInfo findByUsername(String username);

//    Set<String> findRoles(String username);

    /**
     * 获取用户对应的权限和角色
     * @param username
     * @return
     */
    List<UserRolePermission> findUserRolePermissions(String username);
}
