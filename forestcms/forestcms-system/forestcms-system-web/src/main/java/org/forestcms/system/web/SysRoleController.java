package org.forestcms.system.web;

import java.util.HashMap;
import java.util.Map;


import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.system.entity.SysRole;
import org.forestcms.system.entity.SysRolePermissions;
import org.forestcms.system.service.SysPermissionsService;
import org.forestcms.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


@RestController
@RequestMapping("/sys/role")
@SysLogs(desc = "角色管理")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysPermissionsService sysPermissionsService;

	@RequestMapping(value = "/list")
	@SysLogs(desc = "角色列表")
	public CommonResult getList(Long page, Long pageSize, String name) {
		Page<SysRole> pages = new Page<SysRole>(page, pageSize);
		QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>();
		if (!StringUtils.isEmpty(name)) {
			wrapper.like("role_name", name).or().like("role_code", name);
		}
		IPage<SysRole> selectPage = sysRoleService.page(pages, wrapper);
		return CommonResult.success(selectPage);
	}

	@RequestMapping(value = "/add")
	@SysLogs(desc = "添加角色")
	public CommonResult getList(SysRole sysRole) {
		boolean save = sysRoleService.save(sysRole);
		if (save) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping(value = "/getById")
	public CommonResult getList(Long id) {

		return CommonResult.success(sysRoleService.getById(id));
	}

	@RequestMapping("/delete")
	@SysLogs(desc = "删除角色")
	public CommonResult delete(Long id) {
		boolean delete = sysRoleService.removeById(id);
		if (delete) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/update")
	@SysLogs(desc = "修改角色")
	public CommonResult update(SysRole role) {
		boolean updateById = sysRoleService.updateById(role);
		if (updateById) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	
	@RequestMapping("/saveRolePermiss")
	@SysLogs(desc = "设置权限")
	  public CommonResult saveRolePermiss(SysRolePermissions sysRolePermissions) {
		int saveRolePermiss = sysRoleService.saveRolePermiss(sysRolePermissions);
		if (saveRolePermiss>0) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	  }
	@RequestMapping("/getPermissByRoleId")
	  public CommonResult getPermissByRoleId(Long roleId) {
	    Map<String, Object> map=new HashMap<String, Object>();
	    map.put("roleId",roleId);
	    return CommonResult.success(sysPermissionsService.getPermissionByRoleId(map));
	  }
}
