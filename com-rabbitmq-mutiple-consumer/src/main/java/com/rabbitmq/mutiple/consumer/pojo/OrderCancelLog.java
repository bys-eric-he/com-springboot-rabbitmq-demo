package com.rabbitmq.mutiple.consumer.pojo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单取消
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCancelLog extends BaseEntity {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 订单描述
     */
    private String description;
    /**
     * 消息id
     */
    @Column(length = 40, nullable = false)
    private String msgId;
}
