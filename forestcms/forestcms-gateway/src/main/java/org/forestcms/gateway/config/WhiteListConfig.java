package org.forestcms.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 白名单配置
 */
@Configuration
@ConfigurationProperties(prefix = "whitelist")
public class WhiteListConfig {

    private  List<String> urls;

	public   List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

    
}
