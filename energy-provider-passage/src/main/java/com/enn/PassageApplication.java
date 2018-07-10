package com.enn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.enn.service"})
@ServletComponentScan
@MapperScan({"com.enn.energy.passage.dao"})
@EnableScheduling
public class PassageApplication {
	public static void main(String[] args) {
		SpringApplication.run(PassageApplication.class, args);
	}
}
