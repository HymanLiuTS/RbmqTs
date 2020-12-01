package cn.codenest.rabbitmqprovider.controller;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class SendMessageController {

    int i = 0;

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Autowired
    @Qualifier(("TestDirectExchange"))
    DirectExchange testDirectExchange;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(123);
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        System.out.println(map);
        return "ok";
    }

    @GetMapping("/sendMessage")
    public String sendMessage() {
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", "TestDirectQueue0");
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectQueue0", map);
        System.out.println(map);
        return "ok";
    }

    Random random = new Random();

    @GetMapping("/sendMessages")
    public String sendMessages() {
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        Integer i = random.nextInt(100);
        String queuename = "TestDirectQueue" + i;
        map.put("messageId", queuename);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //String routename = "TestDirectRouting" + (i) % 6;
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(queuename, map);
        System.out.println(map);
        return "ok";
    }

}
