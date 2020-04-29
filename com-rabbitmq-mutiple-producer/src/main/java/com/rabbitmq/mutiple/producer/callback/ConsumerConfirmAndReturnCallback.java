package com.rabbitmq.mutiple.producer.callback;

import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * 消息发送确认的回调
 * 实现接口：implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback
 * ConfirmCallback：只确认消息是否正确到达交换机中,不管是否到达交换机,该回调都会执行;
 * ReturnCallback：如果消息从交换机未正确到达队列中将会执行，正确到达则不执行;
 */
@Slf4j
@Component
public class ConsumerConfirmAndReturnCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MessageLogService messageLogService;

    /**
     * PostConstruct: 用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化.
     */
    @PostConstruct
    public void init() {
        //指定 ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        //指定 ReturnCallback
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 消息从交换机成功到达队列，则returnedMessage方法不会执行;
     * 消息从交换机未能成功到达队列，则returnedMessage方法会执行;
     * 需要开启 return 确认机制
     * spring.rabbitmq.publisher-returns=true
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("returnedMessage回调方法->" + new String(message.getBody(), StandardCharsets.UTF_8) + ",\n replyCode:" + replyCode
                + "\n replyText:" + replyText + "\n exchange:" + exchange + ",\\n routingKey:" + routingKey);
    }

    /**
     * 消息找不到对应的Exchange会先触发此方法
     * 如果消息没有到达交换机,则该方法中isSendSuccess = false,error为错误信息;
     * 如果消息正确到达交换机,则该方法中isSendSuccess = true;
     * 需要开启 confirm 确认机制
     * spring.rabbitmq.publisher-confirms=true
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean isSendSuccess, String error) {
        if (correlationData != null) {
            log.info("confirm回调方法->回调消息ID为: " + correlationData.getId());
            if (isSendSuccess) {
                log.info("confirm回调方法->消息成功发送到交换机！");
                String msgId = correlationData.getId();
                messageLogService.updateStatus(msgId, QueueConstants.MessageLogStatus.DELIVER_SUCCESS);
            } else {
                log.info("confirm回调方法->消息[{}]发送到交换机失败！，原因 : [{}]", correlationData, error);
            }
        }
    }
}
