package com.enn.energy.business.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kexing on 2018/4/14.
 */
@Configuration
public class RabbitConfiguration {
    /*
    * *********************** deleted
    public static final String DEFAULT_DIRECT_EXCHANGE = "prontera.direct";
    public static final String TRADE_QUEUE = "funds";
    public static final String TRADE_ROUTE_KEY = "trading";

    @Bean
    public DirectExchange pronteraExchange() {
        return new DirectExchange(DEFAULT_DIRECT_EXCHANGE, true, true);
    }

    @Bean
    public Queue tradeQueue() {
        return new Queue(TRADE_QUEUE, true, false, true);
    }

    @Bean
    public Binding tradeBinding() {
        return BindingBuilder.bind(tradeQueue()).to(pronteraExchange()).with(TRADE_ROUTE_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
     */


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

//    final static String alarmInput = "etsp-alarm-test";
//    final static String alarmOutput = "fanneng-etsp-alarm-test-v1";

//    @Bean
//    public Queue queueInput() {
//        return new Queue(RabbitConfiguration.alarmInput);
//    }
//
//    @Bean
//    public Queue queueOutput() {
//        return new Queue(RabbitConfiguration.alarmOutput);
//    }

//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("alarmExchange");
//    }

//    @Bean
//    Binding bindingExchangeMessage(Queue queueInput, TopicExchange exchange) {
//        return BindingBuilder.bind(queueInput).to(exchange).with("topic.message");
//    }

//    @Bean
//    Binding bindingExchangeMessages(Queue queueOutput, TopicExchange exchange) {
//        return BindingBuilder.bind(queueOutput).to(exchange).with("topic.#");
//    }


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        //factory.setVirtualHost("test");
        factory.setHost(host);
         factory.setPort(port);
        factory.setPublisherConfirms(true);// 保证消息的事务性处理rabbitmq默认的处理方式为auto
        // ack，这意味着当你从消息队列取出一个消息时，ack自动发送，mq就会将消息删除。而为了保证消息的正确处理，我们需要将消息处理修改为手动确认的方式
        return factory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
