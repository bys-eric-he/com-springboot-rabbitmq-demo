package com.rabbitmq.mutiple.consumer.proxy;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface BaseConsumer {
    /**
     * 消息消费入口
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    void consume(Message message, Channel channel) throws IOException;
}