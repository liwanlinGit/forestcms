package org.forestcms.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.core.util.JsonUtil;
import org.forestcms.common.web.util.SysLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/services")
@SysLogs(desc = "服务管理")
public class DiscoveryClientController {

	@Autowired
	private DiscoveryClient discoveryClient;
	
	
	@RequestMapping("/list")
	@SysLogs(desc = "服务列表")
	public CommonResult getServiceList(String name) {
	    List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		List<String> services = discoveryClient.getServices();
		Map<String,Object> map=null;
		
		if(!StringUtils.isAllBlank(name)) {
			services=services.stream().filter(e->e.contains(name)).collect(Collectors.toList());
		}
		for (String serviceName : services) {
			
			int clusterCount=0;
			int healthyInstanceCount=0;
			Set<String> groupName=new HashSet<String>();
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
			for (ServiceInstance serviceInstance : serviceInstances) {
				clusterCount++;
				if(Boolean.parseBoolean(serviceInstance.getMetadata().get("nacos.healthy"))) {
					healthyInstanceCount++;
				}
				groupName.add(serviceInstance.getMetadata().get("nacos.instanceId").toString().split("#")[2]);
			}
			for (ServiceInstance serviceInstance : serviceInstances) {
				String serviceInstanceStr= JsonUtil.toJson(serviceInstance);
				map=new HashMap<String, Object>();
				map.put("name", serviceName);
				map.put("groupName", StringUtils.join(groupName.toArray(),","));
				map.put("clusterCount", clusterCount);
				map.put("healthyInstanceCount", healthyInstanceCount);
				map.put("healthyInstanceCount", healthyInstanceCount);
				map.put("ip", serviceInstance.getHost());
				map.put("port", serviceInstance.getPort());
				map.put("weight", serviceInstance.getMetadata().get("nacos.weight").toString());
				map.put("healthy", serviceInstance.getMetadata().get("nacos.healthy").toString());
				map.put("source", "preserved.register.source="+serviceInstance.getMetadata().get("preserved.register.source").toString());
				result.add(map);
				System.out.println(serviceInstanceStr);
			}
			
		}
		return  CommonResult.success(result);
		
	}
	
	
}
