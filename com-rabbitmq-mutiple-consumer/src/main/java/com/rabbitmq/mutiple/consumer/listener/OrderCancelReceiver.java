package com.rabbitmq.mutiple.consumer.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumer;
import com.rabbitmq.mutiple.consumer.proxy.BaseConsumerProxy;
import com.rabbitmq.mutiple.consumer.proxy.consumer.OrderCancelConsumer;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单取消消息监听接受器
 */
@Slf4j
@Component
public class OrderCancelReceiver {
    @Autowired
    private OrderCancelConsumer orderCancelConsumer;

    @Autowired
    private MessageLogService messageLogService;

    @RabbitListener(queues = QueueConstants.QueueOrderCancel.QUEUE)
    public void process(Message message, Channel channel) {
        try {
            log.info("consumer->DirectReceiver消费者收到消息  : " + message.toString());
            BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(orderCancelConsumer, messageLogService);
            BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
            if (null != proxy) {
                proxy.consume(message, channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
