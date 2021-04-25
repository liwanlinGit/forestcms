package org.forestcms.xxl.job.web.core.alarm;

import org.forestcms.xxl.job.web.core.model.XxlJobInfo;
import org.forestcms.xxl.job.web.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
