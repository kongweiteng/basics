package com.enn.energy.business.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 获取配置文件中以prefix值开头的配置信息
 * @author kongweiteng
 */
@Component
@ConfigurationProperties(prefix = "apiUrl")
public class ApplicationConfig {


}
