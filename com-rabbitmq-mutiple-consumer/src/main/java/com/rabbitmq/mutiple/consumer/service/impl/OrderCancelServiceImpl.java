package com.rabbitmq.mutiple.consumer.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rabbitmq.mutiple.consumer.pojo.OrderCancelLog;
import com.rabbitmq.mutiple.consumer.pojo.QOrderCancelLog;
import com.rabbitmq.mutiple.consumer.repository.OrderCancelRepository;
import com.rabbitmq.mutiple.consumer.service.OrderCancelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单取消业务类
 */
@Service
@Slf4j
public class OrderCancelServiceImpl implements OrderCancelService {

    @Autowired
    private OrderCancelRepository orderCancelRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * 插入订单取消消息
     *
     * @param orderCancelLog
     */
    @Override
    public void insert(OrderCancelLog orderCancelLog) {
        orderCancelRepository.save(orderCancelLog);
        log.info("OrderCancel->保存订单取消消息成功！");
    }

    /**
     * 根据消息ID获取一条订单取消消息
     *
     * @param msgId
     * @return
     */
    @Override
    public OrderCancelLog selectByMsgId(String msgId) {
        QOrderCancelLog qOrderCancelLog = QOrderCancelLog.orderCancelLog;

        return jpaQueryFactory.selectFrom(qOrderCancelLog).where(qOrderCancelLog.msgId.eq(msgId)).fetchOne();
    }
}
