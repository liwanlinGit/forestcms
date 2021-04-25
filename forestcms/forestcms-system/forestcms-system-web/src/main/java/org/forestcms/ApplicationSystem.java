package org.forestcms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.forestcms.system.mapper")
@EnableTransactionManagement
@EnableFeignClients
public class ApplicationSystem {
    public static void main( String[] args ){
        
    	SpringApplication.run(ApplicationSystem.class, args);
    	
    	
    }
}
