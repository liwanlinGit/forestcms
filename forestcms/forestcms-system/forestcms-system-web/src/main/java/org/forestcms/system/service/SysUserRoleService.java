package org.forestcms.system.service;

import org.forestcms.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserRoleService extends IService<SysUserRole> {

	public int saveRole(SysUserRole sysUserRole);
}
