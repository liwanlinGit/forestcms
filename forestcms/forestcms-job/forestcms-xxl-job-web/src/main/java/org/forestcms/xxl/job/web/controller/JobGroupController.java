package org.forestcms.xxl.job.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.xxl.job.web.core.model.XxlJobGroup;
import org.forestcms.xxl.job.web.dao.XxlJobGroupDao;
import org.forestcms.xxl.job.web.dao.XxlJobInfoDao;
import org.forestcms.xxl.job.web.dao.XxlJobRegistryDao;

@RequestMapping("/job/jobGroup")
@RestController
@SysLogs(desc = "执行器管理")
public class JobGroupController {
	@Autowired
	public XxlJobInfoDao xxlJobInfoDao;
	
	@Autowired 
	public XxlJobGroupDao xxlJobGroupDao;
	 
	@Autowired 
	private XxlJobRegistryDao xxlJobRegistryDao;
	
	@RequestMapping("list")
	@SysLogs(desc = "执行器列表")
	public Map<String, Object> getList(Integer page,Integer pageSize,String name,String appName){
		List<XxlJobGroup> list = xxlJobGroupDao.pageList((page-1)*pageSize, pageSize, name, appName);
		int list_count = xxlJobGroupDao.pageListCount(page, pageSize, name, appName);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("data", list);  	
		maps.put("page", page);
		return maps;
	}
	
	@RequestMapping("/save")
	@SysLogs(desc = "添加执行器")
	public Map<String, Object> save(XxlJobGroup xxlJobGroup){
		int ret = xxlJobGroupDao.save(xxlJobGroup);
		Map<String, Object> map=new HashMap<String, Object>();
		if(ret>0) {
			map.put("status", "success");
		}else {
			map.put("status", "error");
		}
		return map;
	}
	
	@RequestMapping("/delete")
	@SysLogs(desc = "删除执行器")
	public Map<String, Object> delete(Integer id){
		int ret = xxlJobGroupDao.remove(id);
		Map<String, Object> map=new HashMap<String, Object>();
		if(ret>0) {
			map.put("status", "success");
		}else {
			map.put("status", "error");
		}
		return map;
	}
	
	@RequestMapping("/update")
	@SysLogs(desc = "修改执行器")
	public Map<String, Object> update(XxlJobGroup xxlJobGroup){
		int ret = xxlJobGroupDao.update(xxlJobGroup);
		Map<String, Object> map=new HashMap<String, Object>();
		if(ret>0) {
			map.put("status", "success");
		}else {
			map.put("status", "error");
		}
		return map;
	}
	
	@RequestMapping("/getById")
	public Map<String, Object> getById(Integer id){
		Map<String, Object> map=new HashMap<String, Object>();
		XxlJobGroup load = xxlJobGroupDao.load(id);
		map.put("data", load);
		return map;
	}
}
