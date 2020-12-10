package com.codenest.rabbitmqconsumer.service.impl;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            Consumer consumer = new MyConsumer("hello-queue", channel);
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

    @Override
    public void getTopicInfoMsg() throws IOException {
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
            //通过自创建的直连交换机获取消息
            channel.basicConsume("info", true, new MyConsumer("info", null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }
    }

    @Override
    public void getFanoutInfoMsg() throws IOException {
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

            channel.exchangeDeclare("fanout-exchange", "fanout", true, false, null);
            for (int i = 0; i < 5; i++) {
                String queueName = "fanout_queue" + i;
                Consumer consumer = new MyConsumer(queueName, channel);
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, "fanout-exchange", "");
                channel.basicConsume(queueName, false, consumer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }
    }

    @Override
    public void getTopicErrorMsg() throws IOException {
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
            //通过自创建的直连交换机获取消息
            channel.basicConsume("error", false, new MyConsumer("error", channel));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                //connection.close();
            }
        }
    }

    public class MyConsumer implements Consumer {

        String queue;

        Channel channel;

        public MyConsumer(String queue, Channel channel) {
            this.queue = queue;
            this.channel = channel;
        }

        @Override
        public void handleConsumeOk(String consumerTag) {
            System.out.println(consumerTag + "handleConsumeOk");
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
            System.out.println(String.format("%s - %s: %s", queue, envelope.getDeliveryTag(), hello));
            //如果是自动确认，不能调用channel.basicAck，所以需要设置channel为null
            //参数multiple表示是否确认其他消息，如果设置成true，则改消费者确认成功改消息后，会一并确认其他所有消息
            if (this.channel != null) {
                //在手动确认模式下，如果不手动确认，后面该消费者将不会收到后续的消息，消息会一直阻塞到队列里
                this.channel.basicAck(envelope.getDeliveryTag(), false);
            }
            String rp = properties.getReplyTo();
            if (this.channel != null && StringUtils.isEmpty(rp) == false) {
                this.channel.basicPublish("", rp, properties, "我收到啦！".getBytes());
            }

        }
    }

}
