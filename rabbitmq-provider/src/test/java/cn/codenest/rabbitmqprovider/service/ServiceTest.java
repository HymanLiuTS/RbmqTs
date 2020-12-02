package cn.codenest.rabbitmqprovider.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/12/1 17:12
 * @description：
 * @modified By：
 * @version: $
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    OriginalRbmqService originalRbmqService;

    @Test
    public void sendMsgTest() throws IOException {
        originalRbmqService.sendDefaultDirectMsg();
        originalRbmqService.sendCustomerDirectMsg();
    }

}
