package com.enn.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by kexing on 2018/4/14.
 */
@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.pool.min-idle}")
    private Integer minidle;

    @Value("${spring.redis.pool.max-idle}")
    private Integer maxidle;

    @Value("${spring.redis.pool.max-wait}")
    private Integer maxWait;

    /**
     * 连接redis的工厂类
     *
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setTimeout(timeout);
        factory.setPassword(password);
        factory.setDatabase(database);

        factory.setUsePool(true);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMinIdle(minidle);
        config.setMaxIdle(maxidle);
        config.setMaxWaitMillis(maxWait);
        factory.setPoolConfig(config);

        return factory;
    }


    /**
     * 配置RedisTemplate
     * 设置添加序列化器
     * key 使用string序列化器
     * value 使用Json序列化器
     * 还有一种简答的设置方式，改变defaultSerializer对象的实现。
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        //StringRedisTemplate的构造方法中默认设置了stringSerializer
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        //设置开启事务
        redisTemplate.setEnableTransactionSupport(true);
        //set key serializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new RedisObjectSerializer()); // value的序列化类型
        //redisTemplate.setHashValueSerializer(new RedisObjectSerializer()); // value的序列化类型
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



}
