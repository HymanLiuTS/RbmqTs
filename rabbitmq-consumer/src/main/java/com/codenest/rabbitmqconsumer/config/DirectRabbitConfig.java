package com.codenest.rabbitmqconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：Hyman
 * @date ：Created in 2020/7/20 15:39
 * @description：
 * @modified By：
 * @version: $
 */
@Configuration
public class DirectRabbitConfig {
    //队列 起名：TestDirectQueue
    @Bean
    public Queue TestDirectQueue() {
        return new Queue("TestDirectQueue", true);
    }

    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange("TestDirectExchange");
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
    }

    @Autowired
    ApplicationContext applicationContext;

    //批量增加队列
    @Bean
    public String addBeans() {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (int i = 0; i < 300; i++) {
            Queue queue = new Queue("TestDirectQueue" + i, true);
            defaultListableBeanFactory.registerSingleton("TestDirectQueue" + i, queue);
        }
        return "";
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        for (int i = 2; i < 100; i++) {
            container.addQueueNames("TestDirectQueue" + i);
        }
        container.setMessageListener((MessageListener) message -> {
            System.out.println("DirectReceiver消费者收到消息  : " + message.toString());
        });
        return container;
    }

}
