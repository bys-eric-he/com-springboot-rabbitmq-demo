package com.rabbitmq.mutiple.common.config;


import com.rabbitmq.mutiple.common.constants.QueueConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者申明一个direct类型(直连型)交换机，然后发送消息到这个交换机指定路由键。
 * 消费者指定消费这个交换机的这个路由键，即可接收到消息，其他消费者收不到。
 * 用户登录直连型交换机配置
 * 1. 声明Exchange交换器；
 * 2. 声明Queue队列；
 * 3. 绑定BindingBuilder绑定队列到交换器，并设置路由键；
 * 消费者单纯的使用，其实可以不用添加这个配置，直接建后面的监听就好，使用注解来让监听器监听对应的队列即可。
 * 配置上了的话，其实消费者也是生成者的身份，也能推送该消息。
 */
@Configuration
public class LoginLogExchangeConfig {
    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public DirectExchange loginLogDirectExchange() {
        return new DirectExchange(QueueConstants.QueueLoginLog.LOGIN_LOG_EXCHANGE_NAME);
    }

    /**
     * 创建队列 true表示是否持久
     *
     * @return
     */
    @Bean
    public Queue loginLogDirectQueue() {
        return new Queue(QueueConstants.QueueLoginLog.LOGIN_LOG_QUEUE_NAME, true);
    }

    /**
     * 将队列和交换机绑定,并设置用于匹配路由键
     *
     * @return
     */
    @Bean
    public Binding BindingLoginLogDirect() {
        return BindingBuilder.bind(loginLogDirectQueue()).to(loginLogDirectExchange()).with(QueueConstants.QueueLoginLog.LOGIN_LOG_ROUTING_KEY_NAME);
    }
}