package com.rabbitmq.mutiple.producer.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.common.constants.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息监听接受器
 */
@Slf4j
@Component
@RabbitListener(queues = QueueConstants.QueueOrderCancel.QUEUE)
public class DirectReceiver {
    @RabbitHandler
    public void process(Message message, Channel channel) {
        try {
            log.info("producer->DirectReceiver消费者收到消息: {}", message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
