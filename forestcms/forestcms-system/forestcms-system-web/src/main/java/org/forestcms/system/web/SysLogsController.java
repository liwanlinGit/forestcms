package org.forestcms.system.web;

import org.forestcms.common.core.result.CommonResult;
import org.forestcms.system.entity.SysLogs;
import org.forestcms.system.service.SysLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


@RestController
@RequestMapping("/sys/logs")
public class SysLogsController {

	@Autowired
	private SysLogsService sysLogsService;

	@RequestMapping("list")
	public CommonResult getList(Long page, Long pageSize, String name) {
		Page<SysLogs> pages = new Page<SysLogs>(page, pageSize);
		QueryWrapper<SysLogs> queryWrapper = new QueryWrapper<SysLogs>();
		if (!StringUtils.isEmpty(name)) {
			queryWrapper.like("user_name", name).or().like("login_name", name);
		}
		queryWrapper.orderByDesc("create_time");
		IPage<SysLogs> selectPage = sysLogsService.page(pages, queryWrapper);
		return CommonResult.success(selectPage);
	}
	
	@RequestMapping("add")
	public boolean add(@RequestBody SysLogs sysLogs) {
	    return sysLogsService.save(sysLogs);
	}
}
