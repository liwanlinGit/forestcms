package org.forestcms.xxl.job.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.forestcms.xxl.job.web.core.model.XxlJobGroup;
import org.forestcms.xxl.job.web.core.model.XxlJobInfo;
import org.forestcms.xxl.job.web.core.model.XxlJobLog;
import org.forestcms.xxl.job.web.core.scheduler.XxlJobScheduler;
import org.forestcms.xxl.job.web.dao.XxlJobGroupDao;
import org.forestcms.xxl.job.web.dao.XxlJobInfoDao;
import org.forestcms.xxl.job.web.dao.XxlJobLogDao;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.xxl.job.core.biz.ExecutorBiz;
import org.forestcms.xxl.job.core.biz.model.LogParam;
import org.forestcms.xxl.job.core.biz.model.LogResult;
import org.forestcms.xxl.job.core.biz.model.ReturnT;
import org.forestcms.xxl.job.core.util.DateUtil;


@RestController
@RequestMapping("/job/joblog")
@SysLogs(desc = "调度日志管理")
public class JobLogController {
	private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	public XxlJobInfoDao xxlJobInfoDao;
	@Resource
	public XxlJobLogDao xxlJobLogDao;
	
	
	@RequestMapping("/list")
	@SysLogs(desc = "调度日志列表")
	public Map<String, Object> getList(Integer page,Integer pageSize,int jobGroup, int jobId, int logStatus, String filterTime){
		Date triggerTimeStart = null;
		Date triggerTimeEnd = null;
		if (filterTime!=null && filterTime.trim().length()>0) {
			String[] temp = filterTime.split(" - ");
			if (temp.length == 2) {
				triggerTimeStart = DateUtil.parseDateTime(temp[0]);
				triggerTimeEnd = DateUtil.parseDateTime(temp[1]);
			}
		}
		
		// page query
		List<XxlJobLog> list = xxlJobLogDao.pageList((page-1)*pageSize, pageSize, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
		int list_count = xxlJobLogDao.pageListCount((page-1)*pageSize, pageSize, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("data", list);  					// 分页列表
	    maps.put("page", page);
		return maps;
	}
	
	
	@RequestMapping("/data")
	public Map<String,Object> data(){
		Map<String, Object> map=new HashMap<String, Object>();
		// 执行器列表
	    List<XxlJobGroup> jobGroupList =  xxlJobGroupDao.findAll();
		
	    List<XxlJobInfo> jobsInfoAll = xxlJobInfoDao.getJobsInfoAll(null);
	    
	    map.put("jobGroupList", jobGroupList);
	    map.put("jobsInfoList", jobsInfoAll);
	    return map;
	}
	
	
	@RequestMapping("/logDetailCat")
	@SysLogs(desc = "调度日志详情")
	public  ReturnT<LogResult> logDetailPage(int id,int fromLineNum){
		XxlJobLog jobLog = xxlJobLogDao.load(id);
		try {
			ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
			ReturnT<LogResult> logResult = executorBiz.log(new LogParam(jobLog.getTriggerTime().getTime(), jobLog.getId(), fromLineNum));

			// is end
            if (logResult.getContent()!=null && logResult.getContent().getFromLineNum() > logResult.getContent().getToLineNum()) {
                jobLog = xxlJobLogDao.load(jobLog.getId());
                if (jobLog.getHandleCode() > 0) {
                    logResult.getContent().setEnd(true);
                }
            }

			return logResult;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<LogResult>(ReturnT.FAIL_CODE, e.getMessage());
		}
	}
	
}
