package org.forestcms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.forestcms.generator.dao")
@EnableFeignClients
public class GeneratorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GeneratorApplication.class, args);
	}

}
