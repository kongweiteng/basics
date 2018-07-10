package com.enn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by zxj on 2018/4/9.
 */
@SpringBootApplication
@EnableEurekaClient
@ServletComponentScan
@EnableFeignClients(basePackages = {"com.enn.service"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class EnergyWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnergyWebApplication.class, args);
	}
}
