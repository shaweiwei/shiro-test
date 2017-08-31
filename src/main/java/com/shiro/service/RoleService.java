package com.shiro.service;

import com.shiro.entity.RoleInfo;

public interface RoleService {
	public RoleInfo createRole(RoleInfo role);
    public void deleteRole(int roleId);

    /**
     * 添加角色-权限之间关系
     * @param roleId
     * @param permissionIds
     */
    public void correlationPermissions(int roleId, int... permissionIds);

    /**
     * 移除角色-权限之间关系
     * @param roleId
     * @param permissionIds
     */
    public void uncorrelationPermissions(int roleId, int... permissionIds);
}
