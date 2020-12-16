package cn.codenest.rabbitmqprovider.service.impl;

import cn.codenest.rabbitmqprovider.service.SpringRbmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/12/16 16:45
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class SpringRbmqServiceImpl implements SpringRbmqService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendMsg(Integer type) throws IOException {
        /*直连也一定要用Routing key*/
        if (type == 1) {
            rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting","TestDirectMessage");
        } else if (type == 2) {
            rabbitTemplate.convertAndSend("TestTopicExchange", "TestTopicRouting", "TestTopicMessage");
        }else {
            rabbitTemplate.convertAndSend("TestFanoutExchange", "","TestFanoutMessage");
        }

    }
}
