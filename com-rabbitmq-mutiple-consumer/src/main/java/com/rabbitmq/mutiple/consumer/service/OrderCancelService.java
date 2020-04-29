package com.rabbitmq.mutiple.consumer.service;

import com.rabbitmq.mutiple.consumer.pojo.OrderCancelLog;

/**
 * 订单取消
 */
public interface OrderCancelService {
    /**
     * 插入订单取消消息
     *
     * @param orderCancelLog
     */
    void insert(OrderCancelLog orderCancelLog);

    /**
     * 根据消息ID获取一条订单取消消息
     *
     * @param msgId
     * @return
     */
    OrderCancelLog selectByMsgId(String msgId);
}
