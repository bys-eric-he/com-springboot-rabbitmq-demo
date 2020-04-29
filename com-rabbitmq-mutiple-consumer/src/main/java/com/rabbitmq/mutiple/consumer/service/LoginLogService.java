package com.rabbitmq.mutiple.consumer.service;

import com.rabbitmq.mutiple.consumer.pojo.LoginLog;

/**
 * 登录日志业务
 */
public interface LoginLogService {
    /**
     * 插入用户登录日志消息
     *
     * @param loginLog
     */
    void insert(LoginLog loginLog);

    /**
     * 根据消息ID获取一条登录日志消息
     *
     * @param msgId
     * @return
     */
    LoginLog selectByMsgId(String msgId);
}
