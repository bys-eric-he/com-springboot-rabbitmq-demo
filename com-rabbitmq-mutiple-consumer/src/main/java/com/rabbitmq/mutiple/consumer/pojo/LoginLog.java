package com.rabbitmq.mutiple.consumer.pojo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 登录日志
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginLog extends BaseEntity {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户类型
     */
    private Integer type;
    /**
     * 登录操作描述
     */
    private String description;
    /**
     * 消息id
     */
    @Column(length = 40, nullable = false)
    private String msgId;
}
