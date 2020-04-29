package com.rabbitmq.mutiple.consumer.proxy.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.consumer.pojo.OrderCancelLog;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumer;
import com.rabbitmq.mutiple.consumer.proxy.helper.MessageHelper;
import com.rabbitmq.mutiple.consumer.service.OrderCancelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单取消处理
 */
@Component
@Slf4j
public class OrderCancelConsumer implements BaseConsumer {

    @Autowired
    private OrderCancelService orderCancelService;

    /**
     * 消息消费入口
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @Override
    public void consume(Message message, Channel channel) throws IOException {
        log.info("收到消息: {}", message.toString());
        OrderCancelLog orderCancelLog = MessageHelper.msgToObj(message, OrderCancelLog.class);
        orderCancelService.insert(orderCancelLog);
    }
}
