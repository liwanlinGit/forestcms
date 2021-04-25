package org.forestcms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.forestcms.xxl.job.web.dao")
@EnableFeignClients
public class XxlJobApplication {
  public static void main(String[] args) {
	SpringApplication.run(XxlJobApplication.class, args);
  }
}
