package org.forestcms.common.web.remote;

import java.util.Map;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="forestcms-system")
public interface LogFeignService {

	
	@RequestMapping(value="/sys/exceptionLogs/add",consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean addExceptionLogs(Map<String, ?> map);
	
	@RequestMapping(value="/sys/logs/add",consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean addLogs(Map<String, ?> map);
}
