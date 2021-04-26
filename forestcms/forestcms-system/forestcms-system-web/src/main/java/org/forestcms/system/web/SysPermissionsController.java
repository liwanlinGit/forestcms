package org.forestcms.system.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.core.util.UserUtil;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.system.entity.SysPermissions;
import org.forestcms.system.service.SysPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/sys/permission")
@SysLogs(desc = "菜单管理")
public class SysPermissionsController {

	@Autowired
	private SysPermissionsService sysPermissionsService;

	@RequestMapping("/getPermissionsByUserId")
	public CommonResult getPermissionsByUserId(HttpServletRequest request) {
		UserDTO user = UserUtil.getUser(request);
		return CommonResult.success(sysPermissionsService.getPermissionByUserId(user.getId()));
	}

	@RequestMapping("/list")
	@SysLogs(desc = "菜单列表")
	public CommonResult listfirstLevel(Long page, Long pageSize, String name, Long parentId) {
		if (parentId != null) {
			QueryWrapper<SysPermissions> queryWrapper = new QueryWrapper<SysPermissions>();
			queryWrapper.eq("parent_id", parentId);
			List<SysPermissions> selectList = sysPermissionsService.list(queryWrapper);
			return CommonResult.success(selectList);
		}
		Page<SysPermissions> pages = new Page<SysPermissions>(page, pageSize);
		QueryWrapper<SysPermissions> queryWrapper = new QueryWrapper<SysPermissions>();
		queryWrapper.eq("tree_depth", 1);
		if (!StringUtils.isEmpty(name)) {
			queryWrapper.like("name", name);
		}
		IPage<SysPermissions> selectPage = sysPermissionsService.page(pages, queryWrapper);
		return CommonResult.success(selectPage);
	}

	@RequestMapping("/add")
	@SysLogs(desc = "添加菜单")
	public CommonResult save(SysPermissions sysPermissions) {
		int resultNum = sysPermissionsService.savePermissions(sysPermissions);
		if (resultNum > 0) {
			return CommonResult.success();
		}
		return CommonResult.failed();

	}

	@RequestMapping("/delete")
	@SysLogs(desc = "删除菜单")
	public CommonResult delete(Long id) {
		int resultNum = sysPermissionsService.deletePermissions(id);
		if (resultNum > 0) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/update")
	@SysLogs(desc = "修改菜单")
	public CommonResult update(SysPermissions sysPermissions) {
		boolean updateById = sysPermissionsService.updateById(sysPermissions);
		if (updateById) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/getById")
	public CommonResult getById(Long id) {
		return CommonResult.success(sysPermissionsService.getById(id));
	}
	
	
	
	@RequestMapping("/button")
	public CommonResult button(HttpServletRequest request, String code) {
		UserDTO user = UserUtil.getUser(request);
		QueryWrapper<SysPermissions> queryWrapper = new QueryWrapper<SysPermissions>();
		queryWrapper.eq("url", code.replaceAll("/", ""));
		SysPermissions sysPer = sysPermissionsService.getOne(queryWrapper);
		if (sysPer != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("parentId", sysPer.getId());
			List<SysPermissions> roleButton = sysPermissionsService.getRoleButton(map);
			Map<String, String> collect = roleButton.stream()
					.collect(Collectors.toMap(SysPermissions::getUrlType, SysPermissions::getName));
			return CommonResult.success(collect);
		}
		return CommonResult.failed();
	}
	
	@RequestMapping("/getPermissionUrlByRoleCode")
	  public Map<String,String> getPermissionUrlByRoleCode(String roleCode) {
	    return sysPermissionsService.getPermissionUrlByRoleCode(roleCode);
	  }
}
