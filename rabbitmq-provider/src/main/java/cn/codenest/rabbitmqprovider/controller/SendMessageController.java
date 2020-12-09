package cn.codenest.rabbitmqprovider.controller;

import cn.codenest.rabbitmqprovider.service.OriginalRbmqService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/7/16 11:07
 * @description：
 * @modified By：
 * @version: $
 */
@RestController
public class SendMessageController {

    int i = 0;

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Autowired
    @Qualifier(("TestDirectExchange"))
    DirectExchange testDirectExchange;

    @Autowired
    OriginalRbmqService originalRbmqService;

    @GetMapping("/sendDefaultDirectMessage")
    public String sendDefaultDirectMessage() throws IOException {
        originalRbmqService.sendDefaultDirectMsg();
        return "OK";
    }

    @GetMapping("/sendCustomerDirectMessage")
    public String sendCustomerDirectMessage() throws IOException {
        originalRbmqService.sendCustomerDirectMsg();
        return "OK";
    }

    @GetMapping("/sendTopicMsg")
    public String sendTopicMsg() throws IOException {
        originalRbmqService.sendTopicMsg();
        return "OK";
    }

    @GetMapping("/sendFanoutMsg")
    public String sendFanoutMsg() throws IOException {
        originalRbmqService.sendFanoutMsg();
        return "OK";
    }

}
