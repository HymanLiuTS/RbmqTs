package cn.codenest.rabbitmqprovider.controller;

import cn.codenest.rabbitmqprovider.service.OriginalRbmqService;
import cn.codenest.rabbitmqprovider.service.impl.SpringRbmqServiceImpl;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/7/16 11:07
 * @description：
 * @modified By：
 * @version: $
 */
@RestController
public class SendMessageController {

    @Autowired
    OriginalRbmqService originalRbmqService;

    @Autowired
    SpringRbmqServiceImpl springRbmqService;

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

    @GetMapping("/sendClusterMsg")
    public String sendClusterMsg() throws IOException, TimeoutException {
        originalRbmqService.sendClusterMsg();
        return "OK";
    }

    @GetMapping("/sendExpireMessage")
    public String sendExpireMessage() throws IOException, TimeoutException {
        originalRbmqService.sendExpireMessage();
        return "OK";
    }

    @GetMapping("/sendMsg/{type}")
    public String sendMsg(@PathVariable Integer type) throws IOException, TimeoutException {
        springRbmqService.sendMsg(type);
        return "OK";
    }

}
