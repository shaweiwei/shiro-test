package com.shiro.dao;

import com.shiro.entity.RoleInfo;

public interface RoleDao {

    public RoleInfo createRole(RoleInfo role);
    public void deleteRole(int roleId);

    public void correlationPermissions(int roleId, int... permissionIds);
    public void uncorrelationPermissions(int roleId, int... permissionIds);

}
