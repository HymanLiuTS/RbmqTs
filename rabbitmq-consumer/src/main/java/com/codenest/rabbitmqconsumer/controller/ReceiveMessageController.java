package com.codenest.rabbitmqconsumer.controller;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReceiveMessageController {


    @Autowired
    OriginalRbmqService originalRbmqService;

    @GetMapping("/receiveMessage")
    public String receiveMessage() throws IOException {
        return "ok";
    }

}
