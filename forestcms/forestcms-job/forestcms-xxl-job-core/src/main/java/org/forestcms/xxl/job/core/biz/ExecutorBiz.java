package org.forestcms.xxl.job.core.biz;

import org.forestcms.xxl.job.core.biz.model.IdleBeatParam;
import org.forestcms.xxl.job.core.biz.model.KillParam;
import org.forestcms.xxl.job.core.biz.model.LogParam;
import org.forestcms.xxl.job.core.biz.model.LogResult;
import org.forestcms.xxl.job.core.biz.model.ReturnT;
import org.forestcms.xxl.job.core.biz.model.TriggerParam;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     * @return
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     * @param killParam
     * @return
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam
     * @return
     */
    public ReturnT<LogResult> log(LogParam logParam);

}
