package com.rabbitmq.mutiple.consumer.proxy.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.consumer.pojo.LoginLog;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumer;
import com.rabbitmq.mutiple.consumer.proxy.helper.MessageHelper;
import com.rabbitmq.mutiple.consumer.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录日志业务
 */
@Component
@Slf4j
public class LoginLogConsumer implements BaseConsumer {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 消息消费入口
     *
     * @param message
     * @param channel
     */
    @Override
    public void consume(Message message, Channel channel) {
        log.info("收到消息: {}", message.toString());
        LoginLog loginLog = MessageHelper.msgToObj(message, LoginLog.class);
        loginLogService.insert(loginLog);
    }
}
