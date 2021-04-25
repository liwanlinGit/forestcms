package org.forestcms.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysRole;
import org.forestcms.system.entity.SysRolePermissions;
import org.forestcms.system.mapper.SysRoleMapper;
import org.forestcms.system.service.SysRolePermissionsService;
import org.forestcms.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	public SysRolePermissionsService sysRolePermissionsService;

	@Override
	public List<SysRole> getUserRole(Map<String, Object> map) {
		return sysRoleMapper.getUserRole(map);
	}

	@Override
	public List<SysRole> getRoleByUserId(Map<String, Object> map) {
		return sysRoleMapper.getRoleByUserId(map);
	}

	@Override
	public int saveRolePermiss(SysRolePermissions sysRolePermissions) {
		int result = 0;
		if (!StringUtils.isEmpty(sysRolePermissions.getPermissionsIds())) {
			QueryWrapper<SysRolePermissions> wrapper = new QueryWrapper<SysRolePermissions>();
			wrapper.eq("role_id", sysRolePermissions.getRoleId());
			sysRolePermissionsService.remove(wrapper);
			String[] permissionsIds = sysRolePermissions.getPermissionsIds().split(",");
			SysRolePermissions srp = null;
			List<SysRolePermissions> entityList = new ArrayList<SysRolePermissions>();
			for (String permissionsId : permissionsIds) {
				srp = new SysRolePermissions();
				srp.setPermissionsId(Long.parseLong(permissionsId));
				srp.setRoleId(sysRolePermissions.getRoleId());
				entityList.add(srp);
			}
			boolean saveBatch = sysRolePermissionsService.saveBatch(entityList);
			result = saveBatch ? 1 : 0;
		}
		return result;
	}

}
