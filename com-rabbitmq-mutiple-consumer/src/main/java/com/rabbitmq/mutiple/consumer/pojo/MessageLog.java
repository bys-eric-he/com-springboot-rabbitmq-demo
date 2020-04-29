package com.rabbitmq.mutiple.consumer.pojo;

import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.common.utils.JodaTimeUtil;
import com.rabbitmq.mutiple.common.utils.JsonUtil;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息体
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageLog extends BaseEntity {
    /**
     * 消息编号
     */
    @Column(length = 40, nullable = false)
    private String msgId;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 消息交换机
     */
    private String exchange;
    /**
     * 消息路由键
     */
    private String routingKey;
    /**
     * 消息状态
     */
    private Integer status;
    /**
     * 重试次数
     */
    private Integer tryCount;
    /**
     * 下次重试时间
     */
    private Date nextTryTime;

    /**
     * 实例化消息日志
     *
     * @param msgId
     * @param msg
     * @param exchange
     * @param routingKey
     */
    public MessageLog(String msgId, Object msg, String exchange, String routingKey) {
        this.msgId = msgId;
        this.msg = JsonUtil.objToStr(msg);
        ;
        this.exchange = exchange;
        this.routingKey = routingKey;

        this.status = QueueConstants.MessageLogStatus.DELIVERING;
        this.tryCount = 0;

        Date currentDate = new Date();
        this.setCreateTime(currentDate);
        this.setUpdateTime(currentDate);
        this.nextTryTime = (JodaTimeUtil.plusMinutes(currentDate, 1));
    }
}
