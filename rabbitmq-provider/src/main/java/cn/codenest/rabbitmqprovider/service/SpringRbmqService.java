package cn.codenest.rabbitmqprovider.service;

import java.io.IOException;

/**
 * <h3>rbmqts</h3>
 * <p></p>
 *
 * @author : Hyman
 * @date : 2020-12-16 16:44
 **/
public interface SpringRbmqService {
    public void sendMsg(Integer type) throws IOException;
}
