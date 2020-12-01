package com.codenest.rabbitmqconsumer.controller;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author ：Hyman
 * @date ：Created in 2020/7/16 11:07
 * @description：
 * @modified By：
 * @version: $
 */
@RestController
public class ReceiveMessageController {


    @Autowired
    OriginalRbmqService originalRbmqService;

    @GetMapping("/receiveMessage")
    public String receiveMessage() throws IOException {
        originalRbmqService.getMsg();
        return "ok";
    }

}
