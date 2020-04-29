package com.rabbitmq.mutiple.producer.task;

import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.consumer.pojo.MessageLog;
import com.rabbitmq.mutiple.consumer.proxy.helper.MessageHelper;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ResendMessage {
    @Autowired
    private MessageLogService msgLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 最大投递次数
    private static final int MAX_TRY_COUNT = 3;

    /**
     * 每30s拉取投递失败的消息, 保证消息100%投递成功并被消费.
     * 实际应用场景中, 可能由于网络原因, 或者消息未被持久化MQ就宕机了, 使得投递确认的回调方法ConfirmCallback没有被执行,
     * 从而导致数据库该消息状态一直是投递中的状态, 此时就需要进行消息重投, 即使也许消息已经被消费了。
     * 定时任务只是保证消息100%投递成功, 而多次投递的消费幂等性需要消费端自己保证。
     * 我们可以将回调和消费成功后更新消息状态的代码注释掉, 开启定时任务, 查看是否重投。
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void resend() {
        log.info("开始执行定时任务(重新投递消息)");

        List<MessageLog> msgLogs = msgLogService.selectTimeoutMsg();
        msgLogs.forEach(msgLog -> {
            String msgId = msgLog.getMsgId();
            if (msgLog.getTryCount() >= MAX_TRY_COUNT) {
                msgLogService.updateStatus(msgId, QueueConstants.MessageLogStatus.DELIVER_FAIL);
                log.info("超过最大重试次数, 消息投递失败, msgId: {}", msgId);
            } else {
                // 投递次数+1
                msgLogService.updateTryCount(msgId, msgLog.getNextTryTime());

                CorrelationData correlationData = new CorrelationData(msgId);
                // 重新投递
                rabbitTemplate.convertAndSend(msgLog.getExchange(), msgLog.getRoutingKey(),
                        MessageHelper.objToMsg(msgLog.getMsg()), correlationData);

                log.info("第 " + (msgLog.getTryCount() + 1) + " 次重新投递 MsgID:" + msgId + "的消息!");
            }
        });

        log.info("定时任务执行结束(重新投递消息)");
    }

}
