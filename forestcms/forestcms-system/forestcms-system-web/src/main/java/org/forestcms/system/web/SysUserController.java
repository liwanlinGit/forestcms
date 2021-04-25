package org.forestcms.system.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.core.util.JsonUtil;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.system.entity.SysUser;
import org.forestcms.system.entity.SysUserRole;
import org.forestcms.system.entity.SysRole;
import org.forestcms.system.service.SysRoleService;
import org.forestcms.system.service.SysUserRoleService;
import org.forestcms.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;




@RestController
@RequestMapping("/sys/user")
@SysLogs(desc = "用户管理")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	

	@RequestMapping("/list")
	@SysLogs(desc = "用户列表")
	public CommonResult getList(Long page, Long pageSize, Long typeId, String name) {
		Page<SysUser> ipage = new Page<SysUser>(page, pageSize);
		QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>();
		wrapper.orderByDesc("id");
		if (typeId != null) {
			wrapper.eq("type_id", typeId);
		}
		if (!StringUtils.isEmpty(name)) {
			wrapper.like("login_name", name).or().like("name", name);
		}
		IPage<SysUser> selectPage = sysUserService.page(ipage, wrapper);
		List<SysUser> records = selectPage.getRecords();
		records.forEach(e -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", e.getId());
			List<SysRole> roleByUserId = sysRoleService.getUserRole(map);
			if (!CollectionUtils.isEmpty(roleByUserId)) {
				e.setRoleNames(roleByUserId.stream().map(t -> t.getRoleName()).collect(Collectors.joining(",")));
			}
		});

		long endtime=System.currentTimeMillis();
		return CommonResult.success(selectPage);
	}

	@RequestMapping("/add")
	@SysLogs(desc = "添加用户")
	public CommonResult save(SysUser user) {
		int resultNum = sysUserService.saveUser(user);
		if (resultNum > 0) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/update")
	@SysLogs(desc = "修改用户")
	public CommonResult update(SysUser user) {
		int resultNum = sysUserService.updateUser(user);
		if (resultNum > 0) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/getById")
	public CommonResult getById(Long id) {
		return CommonResult.success(sysUserService.getByUserId(id));
	}
	@RequestMapping("/delete")
	@SysLogs(desc = "删除用户")
	public CommonResult delete(Long id) {
		return CommonResult.success(sysUserService.removeById(id));
	}
	

	@RequestMapping("/saveRole")
	@SysLogs(desc = "设置角色")
	  public CommonResult saveRole(SysUserRole sysUserRole) {
		int resultNum = sysUserRoleService.saveRole(sysUserRole);
		if(resultNum>0) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	  }
	
	@RequestMapping("/username")
	public UserDTO getUserByUserName(String userName) {
		return sysUserService.getUserByUserName(userName);

	}

	@RequestMapping("/editPass")
	@SysLogs(desc = "修改密码")
	public int editPass(Long userId, String password) {
		return sysUserService.editPass(userId, password);
	}
	
	@RequestMapping("/getRoleByUserId")
	public CommonResult getRoleByUserId(Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return CommonResult.success(sysRoleService.getRoleByUserId(map));
	}
	
	@RequestMapping(value = "/listAll")
	public CommonResult listAll() {
		return CommonResult.success(sysRoleService.list(null));
	}


}
