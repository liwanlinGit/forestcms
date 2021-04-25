package org.forestcms.common.core.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.forestcms.common.core.dto.UserDTO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

public class UserUtil {

	public static HttpServletRequest getRequest() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	public static UserDTO getUser(HttpServletRequest request) {

		JsonNode readTree = JsonUtil.readTree(request.getHeader("user"));
		byte[] decoded = Base64.getDecoder().decode(readTree.path("realName").asText());
		String realName = "";
		try {
			realName = new String(decoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<String> roles = JsonUtil.toObject(readTree.path("authorities").toString(),
				new TypeReference<List<String>>() {
				});
		return new UserDTO(readTree.path("id").asLong(), readTree.path("user_name").asText(),
				readTree.path("password").asText(), null, realName, roles);
	}

	public static UserDTO getUser() {

		JsonNode readTree = JsonUtil.readTree(getRequest().getHeader("user"));
		byte[] decoded = Base64.getDecoder().decode(readTree.path("realName").asText());
		String realName = "";
		try {
			realName = new String(decoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<String> roles = JsonUtil.toObject(readTree.path("authorities").toString(),
				new TypeReference<List<String>>() {
				});
		return new UserDTO(readTree.path("id").asLong(), readTree.path("user_name").asText(),
				readTree.path("password").asText(), null, realName, roles);
	}

}
