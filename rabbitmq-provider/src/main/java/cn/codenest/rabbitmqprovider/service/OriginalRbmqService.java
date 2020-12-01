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
    public void sendMsg(String msg) throws IOException;
}
