package com.shiro.service;

import com.shiro.entity.Permission;

public interface PermissionService {
	public Permission createPermission(Permission permission);
    public void deletePermission(int permissionId);
}
