package com.rabbitmq.mutiple.consumer.service;

import com.rabbitmq.mutiple.consumer.pojo.MessageLog;

import java.util.Date;
import java.util.List;

/**
 * 消息业务接口
 */
public interface MessageLogService {
    /**
     * 插入一条消息处理记录
     *
     * @param msgLog
     */
    void insert(MessageLog msgLog);

    /**
     * 更新消息处理记录状态
     *
     * @param msgId
     * @param status
     */
    void updateStatus(String msgId, Integer status);

    /**
     * 根据消息编号获取消息处理记录
     *
     * @param msgId
     * @return
     */
    MessageLog selectByMsgId(String msgId);

    /**
     * 获取需要重新投递的消息列表
     *
     * @return
     */
    List<MessageLog> selectTimeoutMsg();

    /**
     * 更新消息投递重试次数
     *
     * @param msgId
     * @param tryTime
     */
    void updateTryCount(String msgId, Date tryTime);
}
