package org.forestcms.system.service;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysRole;
import org.forestcms.system.entity.SysRolePermissions;

import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRole> {
	
	public List<SysRole> getUserRole(Map<String,Object> map);
	
	public List<SysRole> getRoleByUserId(Map<String, Object> map);
	
	public int saveRolePermiss(SysRolePermissions sysRolePermissions);

}
