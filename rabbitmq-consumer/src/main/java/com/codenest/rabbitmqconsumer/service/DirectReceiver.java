package com.codenest.rabbitmqconsumer.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ：Hyman
 * @date ：Created in 2020/7/20 15:45
 * @description：
 * @modified By：
 * @version: $
 */
@Component
@RabbitListener(queues ={"TestDirectQueue","TestDirectQueue1","TestDirectQueue2"} )//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
