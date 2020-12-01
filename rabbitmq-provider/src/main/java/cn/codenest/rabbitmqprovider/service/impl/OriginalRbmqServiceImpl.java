package cn.codenest.rabbitmqprovider.service.impl;

import cn.codenest.rabbitmqprovider.service.OriginalRbmqService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public void sendMsg(String msg) {

    }

    @Override
    public void createDirectExchange() throws IOException, TimeoutException {
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
            Channel channel= connection.createChannel();
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
