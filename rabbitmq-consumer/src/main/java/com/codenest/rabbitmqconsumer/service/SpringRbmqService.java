package com.codenest.rabbitmqconsumer.service;

import org.springframework.messaging.handler.annotation.Payload;

/**
 * <h3>rbmqts</h3>
 * <p></p>
 *
 * @author : Hyman
 * @date : 2020-12-16 17:14
 **/
public interface SpringRbmqService {

    public void getDirectMsg(@Payload String msg);
    public void getTopicMsg(@Payload String msg);
    public void getFanoutMsg(@Payload String msg);

}
