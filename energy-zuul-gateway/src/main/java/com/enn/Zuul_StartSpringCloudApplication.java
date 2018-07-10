package com.enn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
@SpringBootApplication
@EnableZuulProxy
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Zuul_StartSpringCloudApplication {
	public static void main(String[] args) {
		SpringApplication.run(Zuul_StartSpringCloudApplication.class, args);
	}
}




























