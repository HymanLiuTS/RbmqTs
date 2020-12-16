package com.codenest.rabbitmqconsumer.service.impl;

import com.codenest.rabbitmqconsumer.service.SpringRbmqService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author ：Hyman
 * @date ：Created in 2020/12/16 17:15
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class SpringRbmqServiceImpl implements SpringRbmqService {

    @RabbitListener(queues = "TestDirectQueue")
    @Override
    public void getDirectMsg(String msg) {
        System.out.println(String.format("TestDirectQueue:" + msg));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "TestTopicQueue", durable = "true"),
            exchange = @Exchange(value = "orderTopicExchange", type = ExchangeTypes.TOPIC),
            key = "TestTopicRouting")
    )
    @Override
    public void getTopicMsg(String msg) {
        System.out.println(String.format("TestTopicQueue:" + msg));
    }

    @RabbitListener(queues = "TestFantQueue")
    @Override
    public void getFanoutMsg(String msg) {
        System.out.println(String.format("TestFantQueue:" + msg));
    }
}
