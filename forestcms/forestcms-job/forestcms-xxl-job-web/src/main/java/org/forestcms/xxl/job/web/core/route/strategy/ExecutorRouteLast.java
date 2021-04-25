package org.forestcms.xxl.job.web.core.route.strategy;

import java.util.List;

import org.forestcms.xxl.job.web.core.route.ExecutorRouter;
import org.forestcms.xxl.job.core.biz.model.ReturnT;
import org.forestcms.xxl.job.core.biz.model.TriggerParam;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
