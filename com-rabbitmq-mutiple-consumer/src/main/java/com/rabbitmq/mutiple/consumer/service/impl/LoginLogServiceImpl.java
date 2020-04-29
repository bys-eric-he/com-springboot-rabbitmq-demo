package com.rabbitmq.mutiple.consumer.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rabbitmq.mutiple.consumer.pojo.LoginLog;
import com.rabbitmq.mutiple.consumer.pojo.QLoginLog;
import com.rabbitmq.mutiple.consumer.repository.LoginLogRepository;
import com.rabbitmq.mutiple.consumer.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * 插入用户登录日志消息
     *
     * @param loginLog
     */
    @Override
    public void insert(LoginLog loginLog) {
        loginLogRepository.save(loginLog);
        log.info("LoginLog->保存登录消息成功！");
    }

    /**
     * 根据消息ID获取一条登录日志消息
     *
     * @param msgId
     * @return
     */
    @Override
    public LoginLog selectByMsgId(String msgId) {
        QLoginLog qLoginLog = QLoginLog.loginLog;
        return jpaQueryFactory.selectFrom(qLoginLog).where(qLoginLog.msgId.eq(msgId)).fetchOne();
    }
}
