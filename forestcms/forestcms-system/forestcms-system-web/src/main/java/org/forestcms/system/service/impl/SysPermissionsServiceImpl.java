package org.forestcms.system.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.forestcms.system.entity.SysPermissions;
import org.forestcms.system.mapper.SysPermissionsMapper;
import org.forestcms.system.service.SysPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysPermissionsService")
public class SysPermissionsServiceImpl extends ServiceImpl<SysPermissionsMapper, SysPermissions> implements SysPermissionsService {

	@Autowired
	private SysPermissionsMapper sysPermissionsMapper;
	
	
	@Override
	public List<SysPermissions> getPermissionByUserId(Long userId) {
		return sysPermissionsMapper.getPermissionByUserId(userId);
	}


	@Override
	public List<SysPermissions> getRoleButton(Map<String, Object> map) {
		return sysPermissionsMapper.getRoleButton(map);
	}


	@Override
	public int savePermissions(SysPermissions sysPermissions) {
		if (sysPermissions.getParentId() != null) {
			SysPermissions sp = sysPermissionsMapper.selectById(sysPermissions.getParentId());
			if (sp != null) {
				sysPermissions.setTreeDepth(sp.getTreeDepth() + 1);
				if ("false".equals(sp.getIsParent())) {
					sp.setIsParent("true");
					sysPermissionsMapper.updateById(sp);
				}
			}
		}
		return sysPermissionsMapper.insert(sysPermissions);
	}


	@Override
	public int deletePermissions(Long id) {
		SysPermissions sp = sysPermissionsMapper.selectById(id);
		if(sp!=null) {
			int deleteById = sysPermissionsMapper.deleteById(id);
			if(deleteById>0) {
				QueryWrapper<SysPermissions> queryWrapper = new QueryWrapper<SysPermissions>();
				queryWrapper.eq("parent_id", sp.getParentId());
				List<SysPermissions> selectList = sysPermissionsMapper.selectList(queryWrapper);
				if(CollectionUtils.isEmpty(selectList)) {
					SysPermissions spp = sysPermissionsMapper.selectById(sp.getParentId());
					spp.setIsParent("false");
					sysPermissionsMapper.updateById(spp);
				}
				return deleteById;
			}
			
		}
		return 0;
	}


	@Override
	public List<SysPermissions> getPermissionByRoleId(Map<String, Object> map) {
		return sysPermissionsMapper.getPermissionByRoleId(map);
	}


	@Override
	public Map<String,String> getPermissionUrlByRoleCode(String roleCode) {
		Map<String,Object> param=new HashMap<String, Object>();
		if(!StringUtils.isAllBlank(roleCode)) {
			param.put("roleCodeList", Arrays.asList(roleCode.split(",")));
			List<SysPermissions> permissionUrlByRoleCode = sysPermissionsMapper.getPermissionUrlByRoleCode(param);
			return permissionUrlByRoleCode.stream().collect(Collectors.toMap(SysPermissions::getUrl,SysPermissions::getRoleCode));
		}
		return null;
		
	}

}
