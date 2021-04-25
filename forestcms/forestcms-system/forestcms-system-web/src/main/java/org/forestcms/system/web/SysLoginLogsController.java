package org.forestcms.system.web;

import org.forestcms.common.core.result.CommonResult;
import org.forestcms.system.entity.SysLoginLogs;
import org.forestcms.system.service.SysLoginLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/sys/loginLogs")
public class SysLoginLogsController {

	@Autowired
	private SysLoginLogsService sysLoginLogsService;

	@RequestMapping("/list")
	public CommonResult getList(Long page, Long pageSize, String name) {
		Page<SysLoginLogs> pages = new Page<SysLoginLogs>(page, pageSize);
		QueryWrapper<SysLoginLogs> queryWrapper = new QueryWrapper<SysLoginLogs>();
		if (!StringUtils.isEmpty(name)) {
			queryWrapper.like("user_name", name).or().like("login_name", name);
		}
		queryWrapper.orderByDesc("create_time");
		IPage<SysLoginLogs> selectPage = sysLoginLogsService.page(pages, queryWrapper);
		return CommonResult.success(selectPage);
	}

}
