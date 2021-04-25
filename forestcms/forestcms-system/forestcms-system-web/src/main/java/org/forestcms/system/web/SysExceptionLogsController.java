package org.forestcms.system.web;

import java.util.Map;

import org.forestcms.common.core.result.CommonResult;
import org.forestcms.system.entity.SysExceptionLogs;
import org.forestcms.system.entity.SysLogs;
import org.forestcms.system.service.SysExceptionLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/sys/exceptionLogs")
public class SysExceptionLogsController {

	@Autowired
	private SysExceptionLogsService sysExceptionLogsService;

	@RequestMapping("list")
	public CommonResult getList(Long page, Long pageSize, String name) {
		Page<SysExceptionLogs> pages = new Page<SysExceptionLogs>(page, pageSize);
		QueryWrapper<SysExceptionLogs> queryWrapper = new QueryWrapper<SysExceptionLogs>();
		if (!StringUtils.isEmpty(name)) {
			queryWrapper.like("user_name", name).or().like("login_name", name);
		}
		queryWrapper.orderByDesc("create_time");
		IPage<SysExceptionLogs> selectPage = sysExceptionLogsService.page(pages, queryWrapper);
		return CommonResult.success(selectPage);
	}
	
	@RequestMapping("add")
	public boolean add(@RequestBody SysExceptionLogs sysExceptionLogs) {
	    return sysExceptionLogsService.save(sysExceptionLogs);
	}
	
	@RequestMapping("getById")
	public CommonResult add(Long id) {
		
		return CommonResult.success(sysExceptionLogsService.getById(id));
	}

}
