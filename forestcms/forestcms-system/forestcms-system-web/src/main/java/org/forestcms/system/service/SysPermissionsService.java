package org.forestcms.system.service;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysPermissions;

import com.baomidou.mybatisplus.extension.service.IService;

public interface SysPermissionsService extends IService<SysPermissions> {

	public List<SysPermissions> getPermissionByRoleId(Map<String, Object> map);
	
	public List<SysPermissions> getPermissionByUserId(Long userId);
	
	public List<SysPermissions> getRoleButton(Map<String, Object> map);
	
	public int savePermissions(SysPermissions sysPermissions);
	
	public int deletePermissions(Long id);
	
	public Map<String,String> getPermissionUrlByRoleCode(String roleCode);
}
