package cn.codenest.rabbitmqprovider.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <h3>springbootts</h3>
 * <p></p>
 *
 * @author : Hyman
 * @date : 2020-12-01 12:29
 **/
public interface OriginalRbmqService {
    public void sendDefaultDirectMsg() throws IOException;
    public void sendCustomerDirectMsg() throws IOException;
    public void sendTopicMsg() throws IOException;
    public void sendFanoutMsg() throws IOException;
    public void sendClusterMsg() throws IOException, TimeoutException;
    public void sendExpireMessage() throws IOException, TimeoutException;
}
