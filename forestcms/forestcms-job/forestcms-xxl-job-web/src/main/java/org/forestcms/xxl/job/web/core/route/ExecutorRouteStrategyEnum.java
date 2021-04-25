package org.forestcms.xxl.job.web.core.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteBusyover;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteConsistentHash;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteFailover;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteFirst;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteLFU;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteLRU;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteLast;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteRandom;
import org.forestcms.xxl.job.web.core.route.strategy.ExecutorRouteRound;
import org.forestcms.xxl.job.web.core.util.I18nUtil;

/**
 * Created by xuxueli on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {

    FIRST(I18nUtil.getString("jobconf_route_first"), new ExecutorRouteFirst()),
    LAST(I18nUtil.getString("jobconf_route_last"), new ExecutorRouteLast()),
    ROUND(I18nUtil.getString("jobconf_route_round"), new ExecutorRouteRound()),
    RANDOM(I18nUtil.getString("jobconf_route_random"), new ExecutorRouteRandom()),
    CONSISTENT_HASH(I18nUtil.getString("jobconf_route_consistenthash"), new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED(I18nUtil.getString("jobconf_route_lfu"), new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED(I18nUtil.getString("jobconf_route_lru"), new ExecutorRouteLRU()),
    FAILOVER(I18nUtil.getString("jobconf_route_failover"), new ExecutorRouteFailover()),
    BUSYOVER(I18nUtil.getString("jobconf_route_busyover"), new ExecutorRouteBusyover()),
    SHARDING_BROADCAST(I18nUtil.getString("jobconf_route_shard"), null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private String title;
    private ExecutorRouter router;

    public String getTitle() {
        return title;
    }
    public ExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
  
   public static List<Map<String, Object>> executorRouteStrategyEnumMap(){
	   List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
	   ExecutorRouteStrategyEnum[] values = ExecutorRouteStrategyEnum.values();
	   for (ExecutorRouteStrategyEnum executorRouteStrategyEnum : values) {
		   Map<String, Object> e=new HashMap<String, Object>();
		   e.put(executorRouteStrategyEnum.name(), executorRouteStrategyEnum.title);
		   list.add(e);
	   }
	   return list;
   }

}
