package cn.codenest.rabbitmqprovider.service.impl;

import cn.codenest.rabbitmqprovider.service.OriginalRbmqService;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/12/1 12:31
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
    public void sendDefaultDirectMsg() throws IOException {
        // 创建连接和信道
        String msg = "sendDefaultDirectMsg";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.rabbitMqHost);
        factory.setPort(this.rabbitMqPort);
        factory.setConnectionTimeout(this.rabbitMqTimeOut);
        factory.setUsername(this.rabbitMqUsername);
        factory.setPassword(this.rabbitMqPassword);
        factory.setVirtualHost("/");
        Connection connection = null;
        try {
            //用来追踪发送失败的消息
            ConcurrentHashSet delever_failed_msgs = new ConcurrentHashSet();
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.confirmSelect();
            channel.addConfirmListener(new ConfirmListener() {

                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("handleAck deliveryTag=" + deliveryTag);
                    if (delever_failed_msgs.contains(deliveryTag)) {
                        delever_failed_msgs.remove(deliveryTag);
                    }
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("handleNack deliveryTag=" + deliveryTag);
                }
            });
            String collectionId = UUID.randomUUID().toString();
            System.out.println("collectionId=" + collectionId);
            //这里可以生成一个用于接受消费者处理后消息的队列，传递到消费者处
            String replyQueueName = channel.queueDeclare().getQueue();
            // 存入回调队列名与collectionId
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().correlationId(collectionId).replyTo(replyQueueName)
                    .build();
            //exchange设置空字符串，表明使用默认的交换机，默认名称AMQP default

            for (int i = 0; i < 3; i++) {
                delever_failed_msgs.add(Convert.toLong(i + 1));
                channel.basicPublish("", "defaultqueue", bpro, msg.getBytes("UTF-8"));
                channel.waitForConfirms();
            }
            System.out.println(JSONUtil.toJsonStr(delever_failed_msgs));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        }
    }

    @Override
    public void sendCustomerDirectMsg() throws IOException {
        // 创建连接和信道
        String msg = "sendCustomerDirectMsg";
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
            String replyQueueName = channel.queueDeclare().getQueue();
            String collectionId = UUID.randomUUID().toString();
            // 存入回调队列名与collectionId
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().correlationId(collectionId).replyTo(replyQueueName)
                    .build();
            //exchange设置空字符串，表明使用默认的交换机，默认名称AMQP default
            channel.basicPublish("hello-exchange", "hello-channel", bpro, msg.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        }
    }

    @Override
    public void sendTopicMsg() throws IOException {
        // 创建连接和信道
        String error = "error message";
        String info = "info message";
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
            channel.exchangeDeclare("topic-exchange", "topic", true, false, null);
            channel.queueDeclare("info", true, false, false, null);
            channel.queueBind("info", "topic-exchange", "info.*");
            channel.queueDeclare("error", true, false, false, null);
            channel.queueBind("error", "topic-exchange", "*.error");
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().build();
            channel.basicPublish("topic-exchange", "info.error", bpro, error.getBytes("UTF-8"));
            //channel.basicPublish("topic-exchange", "info", bpro, info.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        }
    }
}
