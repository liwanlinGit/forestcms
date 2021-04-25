package org.forestcms.system.api;

import java.util.Map;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="forestcms-system")
public interface UsersFeignService {

	@RequestMapping("/sys/user/username")
	UserDTO getUserByUsername(@RequestParam("userName") String userName);
	
	@RequestMapping("/sys/user/editPass")
	int editPass(@RequestParam("userId")Long userId,@RequestParam("password")String password);
	
	@RequestMapping("/sys/permission/getPermissionUrlByRoleCode")
	Map<String,String> getPermissionUrlByRoleCode(@RequestParam("roleCode")String roleCode);
	
	
}
