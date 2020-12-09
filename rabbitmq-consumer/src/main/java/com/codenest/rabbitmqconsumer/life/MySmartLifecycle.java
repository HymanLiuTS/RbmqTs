package com.codenest.rabbitmqconsumer.life;

import com.codenest.rabbitmqconsumer.service.OriginalRbmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ：Hyman
 * @date ：Created in 2020/10/16 19:44
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public class MySmartLifecycle implements SmartLifecycle {

    @Autowired
    OriginalRbmqService originalRbmqService;

    @Override
    public void start() {
        try {
            originalRbmqService.getDefaultDirectMsg();
            originalRbmqService.getCustomerDirectMsg();
            originalRbmqService.getTopicErrorMsg();
            originalRbmqService.getTopicInfoMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据该方法的返回值决定是否执行start方法。<br/>
     * 返回true时start方法会被自动执行，返回false则不会。
     */
    @Override
    public boolean isAutoStartup() {
        // 默认为false
        return true;
    }

    /**
     * 1. 只有该方法返回false时，start方法才会被执行。<br/>
     * 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
     */
    @Override
    public boolean isRunning() {
        // 默认返回false
        return false;
    }

    @Override
    public void stop() {

    }
}
