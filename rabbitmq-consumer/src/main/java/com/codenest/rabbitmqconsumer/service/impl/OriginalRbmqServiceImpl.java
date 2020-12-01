package com.codenest.rabbitmqconsumer.service.impl;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/12/1 17:37
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class OriginalRbmqServiceImpl implements OriginalRbmqService {

    private String rabbitMqHost = "127.0.0.1";
    private String rabbitMqUsername = "guest";
    private String rabbitMqPassword = "guest";
    private Integer rabbitMqPort = 5672;
    private Integer rabbitMqTimeOut = 10;

    @Override
    public void getMsg() throws IOException {
        // 创建连接和信道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.rabbitMqHost);
        factory.setPort(this.rabbitMqPort);
        factory.setConnectionTimeout(this.rabbitMqTimeOut);
        factory.setUsername(this.rabbitMqUsername);
        factory.setPassword(this.rabbitMqPassword);
        factory.setVirtualHost("/");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("hello-exchange", "direct", true, false, null);
            channel.queueDeclare("hello-queue", true, false, false, null);
            channel.queueBind("hello-queue", "hello-exchange", "hello-channel");
            Consumer consumer = new MyConsumer();
            channel.basicConsume("hello-queue", true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }

    }

    public class MyConsumer implements Consumer {

        @Override
        public void handleConsumeOk(String consumerTag) {
            System.out.println(consumerTag);
        }

        @Override
        public void handleCancelOk(String consumerTag) {
            System.out.println(consumerTag);
        }

        @Override
        public void handleCancel(String consumerTag) throws IOException {
            System.out.println(consumerTag);
        }

        @Override
        public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
            System.out.println(consumerTag);
        }

        @Override
        public void handleRecoverOk(String consumerTag) {
            System.out.println(consumerTag);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String hello=new String(body);
            System.out.println(hello);
        }
    }

}
