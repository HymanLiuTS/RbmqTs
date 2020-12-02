package com.codenest.rabbitmqconsumer.service.impl;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
    public void getDefaultDirectMsg() throws IOException {
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
            Consumer consumer = new Consumer() {
                @Override
                public void handleConsumeOk(String consumerTag) {

                }

                @Override
                public void handleCancelOk(String consumerTag) {

                }

                @Override
                public void handleCancel(String consumerTag) throws IOException {

                }

                @Override
                public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

                }

                @Override
                public void handleRecoverOk(String consumerTag) {

                }

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    String hello=new String(body);
                    System.out.println(hello);
                    System.out.println("begin sleep");
                    System.out.println(envelope.getDeliveryTag());
                    System.out.println("end sleep");
                }
            };
            //通过默认的直连交换机获取消息
            channel.queueDeclare("defaultqueue", true, false, false, null);
            channel.basicConsume("defaultqueue", false, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }

    }

    @Override
    public void getCustomerDirectMsg() throws IOException {
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
            Consumer consumer = new MyConsumer(channel);
            //通过自创建的直连交换机获取消息
            channel.exchangeDeclare("hello-exchange", "direct", true, false, null);
            channel.queueDeclare("hello-queue", true, false, false, null);
            channel.queueBind("hello-queue", "hello-exchange", "hello-channel");
            channel.basicConsume("hello-queue", false, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }

    }

    public class MyConsumer implements Consumer {

        Channel channel;

        public MyConsumer(Channel channel) {
            this.channel = channel;
        }

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
            System.out.println("begin sleep");
            System.out.println(envelope.getDeliveryTag());
            System.out.println("end sleep");
        }
    }

}
