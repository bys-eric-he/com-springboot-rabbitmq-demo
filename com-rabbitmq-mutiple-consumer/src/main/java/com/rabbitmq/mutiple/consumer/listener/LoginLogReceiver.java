package com.rabbitmq.mutiple.consumer.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumer;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumerProxy;
import com.rabbitmq.mutiple.consumer.proxy.consumer.LoginLogConsumer;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录日志监听
 */
@Component
@Slf4j
public class LoginLogReceiver {
    @Autowired
    private LoginLogConsumer loginLogConsumer;

    @Autowired
    private MessageLogService messageLogService;

    /**
     * 消息接收处理
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = QueueConstants.QueueLoginLog.LOGIN_LOG_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(loginLogConsumer, messageLogService);
        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
        if (null != proxy) {
            proxy.consume(message, channel);
        }
    }
}
