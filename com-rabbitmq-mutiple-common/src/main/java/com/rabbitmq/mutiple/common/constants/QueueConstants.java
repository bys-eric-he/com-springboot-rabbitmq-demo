package com.rabbitmq.mutiple.common.constants;

import lombok.Getter;

/**
 * 消息队列常量
 */
public class QueueConstants {

    /**
     * 登录操作
     */
    public interface LoginType {
        Integer LOGIN = 1;// 登录
        Integer LOGOUT = 2;// 登出
    }

    /**
     * 登录日志
     */
    public interface QueueLoginLog {
        /**
         * 交换机名称
         */
        String LOGIN_LOG_EXCHANGE_NAME = "RabbitMQ.DirectExchange.LoginLog";
        /**
         * 队列名称
         */
        String LOGIN_LOG_QUEUE_NAME = "RabbitMQ.DirectQueue.LoginLog";
        /**
         * 路由键
         */
        String LOGIN_LOG_ROUTING_KEY_NAME = "RabbitMQ.RouteKey.LoginLog";
    }

    /**
     * 消息投递状态
     */
    public interface MessageLogStatus {
        /**
         * 消息投递中
         */
        Integer DELIVERING = 0;
        /**
         * 投递成功
         */
        Integer DELIVER_SUCCESS = 1;
        /**
         * 投递失败
         */
        Integer DELIVER_FAIL = 2;
        /**
         * 已消费
         */
        Integer CONSUMED_SUCCESS = 3;
    }

    /**
     * 订单队列常量
     */
    public interface QueueOrderCancel {
        /**
         * 交换机名称
         */
        String EXCHANGE = "RabbitMQ.DirectExchange.OrderCancel";
        /**
         * 队列名称
         */
        String QUEUE = "RabbitMQ.DirectQueue.OrderCancel";
        /**
         * 路由键
         */
        String ROUTEKEY = "RabbitMQ.RouteKey.OrderCancel";
    }

    /**
     * 消息通知队列
     */
    @Getter
    public enum QueueOrderCancelEnum {
        QUEUE_ORDER_CANCEL(QueueConstants.QueueOrderCancel.EXCHANGE, QueueConstants.QueueOrderCancel.QUEUE,
                QueueConstants.QueueOrderCancel.ROUTEKEY);
        /**
         * 交换机名称
         */
        private String exchange;
        /**
         * 队列名称
         */
        private String queue;
        /**
         * 路由键
         */
        private String routeKey;

        QueueOrderCancelEnum(String exchange, String queue, String routeKey) {
            this.exchange = exchange;
            this.queue = queue;
            this.routeKey = routeKey;
        }
    }

}
