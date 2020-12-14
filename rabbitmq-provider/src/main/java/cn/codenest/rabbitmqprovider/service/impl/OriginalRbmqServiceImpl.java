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
        Channel channel = null;
        try {
            //用来追踪发送失败的消息
            ConcurrentHashSet delever_failed_msgs = new ConcurrentHashSet();
            connection = factory.newConnection();
            channel = connection.createChannel();
            /*方法一  开启事务*/
            channel.txSelect();
            /*方法二  使用发送->确认模式*/
            channel.confirmSelect();
            /*增加一个确认的监听器，收到ack时会调用监听器中注册的回调函数*/
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
            /*bpro可以附加一些其他信息，比如在rpc调用框架中，可以把通过replayto把等待接收的队列名称放上去*/
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().build();
            for (int i = 0; i < 3; i++) {
                delever_failed_msgs.add(Convert.toLong(i + 1));
                //exchange设置空字符串，表明使用默认的交换机，默认名称AMQP default
                channel.basicPublish("", "defaultqueue", bpro, msg.getBytes("UTF-8"));
                 /* 等待接受ack*/
                channel.waitForConfirms();
            }
            System.out.println(JSONUtil.toJsonStr(delever_failed_msgs));
            /* 提交事务 */
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
             /* 回滚事务 */
            channel.txRollback();
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
            //创建一个默认的私有队列，并获取名称，用于接受消费者发送过来的回执消息
            String replyQueueName = channel.queueDeclare().getQueue();
            String collectionId = UUID.randomUUID().toString();
            // 存入回调队列名与collectionId
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().correlationId(collectionId).replyTo(replyQueueName)
                    .build();
            channel.queueDeclare("hello-queue", true, false, false, null);
            channel.queueBind("hello-queue", "hello-exchange", "");
            channel.basicPublish("hello-exchange", "", bpro, msg.getBytes("UTF-8"));
            channel.basicConsume(replyQueueName, true, new Consumer() {
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
                    String msg=new String(body);
                    System.out.println(String.format("%s: %s", envelope.getDeliveryTag(), msg));
                }
            });
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
            //这里路由键中点不能少
            channel.basicPublish("topic-exchange", "info.", bpro, info.getBytes("UTF-8"));
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
    public void sendFanoutMsg() throws IOException {
        // 创建连接和信道
        String msg = "fanout images message";
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
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().build();
            channel.basicPublish("fanout-exchange", "", bpro, msg.getBytes("UTF-8"));
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
    public void sendClusterMsg() throws IOException, TimeoutException {
        // 创建连接和信道
        String msg = "cluster message";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("48.92.2.151");
        factory.setPort(5681);
        factory.setConnectionTimeout(30000);
        factory.setUsername("admin");
        factory.setPassword("123");
        factory.setVirtualHost("/");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("cluster-exchange", "direct", true, false, null);
            //创建一个默认的私有队列，并获取名称，用于接受消费者发送过来的回执消息
            String replyQueueName = channel.queueDeclare().getQueue();
            String collectionId = UUID.randomUUID().toString();
            // 存入回调队列名与collectionId
            AMQP.BasicProperties bpro = new AMQP.BasicProperties().builder().correlationId(collectionId).replyTo(replyQueueName)
                    .build();
            channel.queueDeclare("cluster-queue", true, false, false, null);
            channel.queueBind("cluster-queue", "cluster-exchange", "");
            channel.basicPublish("cluster-exchange", "", bpro, msg.getBytes("UTF-8"));
            channel.basicConsume(replyQueueName, true, new Consumer() {
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
                    String msg=new String(body);
                    System.out.println(String.format("%s: %s", envelope.getDeliveryTag(), msg));
                }
            });
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
