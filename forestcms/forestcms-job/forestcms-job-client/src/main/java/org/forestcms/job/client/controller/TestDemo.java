package org.forestcms.job.client.controller;

import java.util.concurrent.TimeUnit;

import org.forestcms.xxl.job.core.biz.model.ReturnT;
import org.forestcms.xxl.job.core.handler.annotation.XxlJob;
import org.forestcms.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;



@Component
public class TestDemo {

	
	
	/**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
    	System.out.println("aaaaaaaaaa");
        XxlJobLogger.log("XXL-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
            XxlJobLogger.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        return ReturnT.SUCCESS;
    }
}
