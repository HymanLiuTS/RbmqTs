package com.codenest.rabbitmqconsumer.service;

import java.io.IOException;

/**
 * <h3>rbmqts</h3>
 * <p></p>
 *
 * @author : Hyman
 * @date : 2020-12-01 17:36
 **/
public interface OriginalRbmqService {

    public void getDefaultDirectMsg() throws IOException;
    public void getCustomerDirectMsg() throws IOException;

}
