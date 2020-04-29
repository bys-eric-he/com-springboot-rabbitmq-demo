package com.rabbitmq.mutiple.consumer.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rabbitmq.mutiple.consumer.pojo.MessageLog;
import com.rabbitmq.mutiple.consumer.pojo.QMessageLog;
import com.rabbitmq.mutiple.consumer.repository.MessageLogRepository;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息处理记录业务类
 */
@Service
@Slf4j
public class MessageLogServiceImpl implements MessageLogService {

    @Autowired
    private MessageLogRepository messageLogRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * 插入一条消息处理记录
     *
     * @param msgLog
     */
    @Override
    public void insert(MessageLog msgLog) {
        messageLogRepository.save(msgLog);

        log.info("MessageLog->保存消息投递记录完成!");
    }

    /**
     * 更新消息处理记录状态
     *
     * @param msgId
     * @param status
     */
    @Override
    public void updateStatus(String msgId, Integer status) {

        QMessageLog qMessageLog = QMessageLog.messageLog;

        MessageLog messageLog = jpaQueryFactory.selectFrom(qMessageLog).where(qMessageLog.msgId.eq(msgId)).fetchOne();

        if (messageLog != null) {
            messageLog.setStatus(status);
            messageLog.setUpdateTime(new Date());

            messageLogRepository.save(messageLog);

            log.info("MessageLog->消息投递记录状态更新完成!");
        }
    }

    /**
     * 根据消息编号获取消息处理记录
     *
     * @param msgId
     * @return
     */
    @Override
    public MessageLog selectByMsgId(String msgId) {
        QMessageLog qMessageLog = QMessageLog.messageLog;
        return jpaQueryFactory.selectFrom(qMessageLog).where(qMessageLog.msgId.eq(msgId)).fetchOne();
    }

    /**
     * 获取需要重新投递的消息列表
     *
     * @return
     */
    @Override
    public List<MessageLog> selectTimeoutMsg() {
        QMessageLog qMessageLog = QMessageLog.messageLog;
        Date date = new Date();

        return jpaQueryFactory.selectFrom(qMessageLog).where(qMessageLog.nextTryTime.after(date)).fetch();
    }

    /**
     * 更新消息投递重试次数
     *
     * @param msgId
     * @param tryTime
     */
    @Override
    public void updateTryCount(String msgId, Date tryTime) {

        QMessageLog qMessageLog = QMessageLog.messageLog;
        Date date = new Date();

        MessageLog messageLog = jpaQueryFactory.selectFrom(qMessageLog).where(qMessageLog.msgId.eq(msgId)).fetchOne();

        if (messageLog != null) {
            messageLog.setTryCount(messageLog.getTryCount() + 1);
            messageLog.setNextTryTime(tryTime);
            messageLog.setUpdateTime(date);

            messageLogRepository.save(messageLog);

            log.info("MessageLog->消息投递记录更新重试次数完成！");
        }
    }
}
