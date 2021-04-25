package org.forestcms.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.forestcms.system.entity.SysUserRole;
import org.forestcms.system.mapper.SysUserRoleMapper;
import org.forestcms.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	public int saveRole(SysUserRole sysUserRole) {
		int result = 0;
		if (!StringUtils.isEmpty(sysUserRole.getRoleIds())) {
			QueryWrapper<SysUserRole> wrapper = new QueryWrapper<SysUserRole>();
			wrapper.eq("user_id", sysUserRole.getUserId());
			sysUserRoleMapper.delete(wrapper);
			String[] roleIds = sysUserRole.getRoleIds().split(",");
			SysUserRole userRole = null;
			List<SysUserRole> list = new ArrayList<SysUserRole>();
			for (String roleId : roleIds) {
				userRole = new SysUserRole();
				userRole.setUserId(sysUserRole.getUserId());
				userRole.setRoleId(Long.parseLong(roleId));
				list.add(userRole);
			}
			boolean saveBatch = this.saveBatch(list);
			result = saveBatch ? 1 : 0;
		}
		return result;
	}

}
