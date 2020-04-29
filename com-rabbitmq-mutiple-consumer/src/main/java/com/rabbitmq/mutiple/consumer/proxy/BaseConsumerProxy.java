package com.rabbitmq.mutiple.consumer.proxy;


import com.rabbitmq.client.Channel;
import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.consumer.pojo.MessageLog;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 在Consumer中, 真正的业务逻辑其实只是保存消息到各自的数据表中, 但我们又不得不在调用consume方法之前校验消费幂等性, 发送后, 还要更新消息状态为"已消费"状态, 并手动ack。
 * 实际项目中, 可能还有很多生产者-消费者的应用场景, 如记录日志, 发送短信等等, 都需要rabbitmq, 如果每次都写这些重复的公用代码, 没必要, 也难以维护。
 * 所以, 我们可以将公共代码抽离出来, 让核心业务逻辑只关心自己的实现, 而不用做其他操作, 其实就是AOP。
 * <p>
 * 为达到这个目的, 有很多方法, 可以用spring aop, 可以用拦截器, 可以用静态代理, 也可以用动态代理, 在这里用的是动态代理。
 */
@Slf4j
public class BaseConsumerProxy {

    /**
     * 代理对象
     */
    private Object target;

    /**
     * 消息业务操作对象
     */
    private MessageLogService msgLogService;

    public BaseConsumerProxy(Object target, MessageLogService msgLogService) {
        this.target = target;
        this.msgLogService = msgLogService;
    }

    /**
     * 使用动态代理实现消费端幂等性验证和消费确认(ack)
     *
     * @return
     */
    public Object getProxy() {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();

        //Lambda表达式方式实现InvocationHandler接口
        return Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {
            Message message = (Message) args[0];
            Channel channel = (Channel) args[1];

            String correlationId = getCorrelationId(message);

            // 消费幂等性, 防止消息被重复消费
            // 重启服务器, 由于有一条未被ack的消息, 所以重启后监听到消息, 进行消费, 但是由于消费前会判断该消息的状态是否未被消费, 发现status=3, 即已消费,
            // 所以, 直接return, 这样就保证了消费端的幂等性, 即使由于网络等原因投递成功而未触发回调, 从而多次投递, 也不会重复消费进而发生业务异常。
            if (isConsumed(correlationId)) {
                log.info("重复消费, correlationId: {}", correlationId);
                return null;
            }

            MessageProperties properties = message.getMessageProperties();
            long tag = properties.getDeliveryTag();

            try {
                // 真正消费的业务逻辑
                Object result = method.invoke(target, args);
                msgLogService.updateStatus(correlationId, QueueConstants.MessageLogStatus.CONSUMED_SUCCESS);
                // 消费确认 虽然消息确实被消费了, 但是由于是手动确认模式, 而最后又没手动确认, 所以, 消息仍被rabbitmq保存。
                // 所以, 手动ack能够保证消息一定被消费, 但一定要记得basicAck。
                channel.basicAck(tag, false);
                return result;
            } catch (Exception e) {
                log.error("getProxy error", e);
                channel.basicNack(tag, false, true);
                return null;
            }
        });
    }

    /**
     * 使用动态代理实现消费端幂等性验证和消费确认(ack)
     *
     * @return
     */
    public Object getProxy(Object target, MessageLogService msgLogService) {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();

        //匿名类方式实现InvocationHandler接口
        return Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Message message = (Message) args[0];
                Channel channel = (Channel) args[1];

                String correlationId = getCorrelationId(message);

                // 消费幂等性, 防止消息被重复消费
                // 重启服务器, 由于有一条未被ack的消息, 所以重启后监听到消息, 进行消费, 但是由于消费前会判断该消息的状态是否未被消费, 发现status=3, 即已消费,
                // 所以, 直接return, 这样就保证了消费端的幂等性, 即使由于网络等原因投递成功而未触发回调, 从而多次投递, 也不会重复消费进而发生业务异常。
                if (isConsumed(correlationId)) {
                    log.info("重复消费, correlationId: {}", correlationId);
                    return null;
                }

                MessageProperties properties = message.getMessageProperties();
                long tag = properties.getDeliveryTag();

                try {
                    // 真正消费的业务逻辑
                    Object result = method.invoke(target, args);
                    msgLogService.updateStatus(correlationId, QueueConstants.MessageLogStatus.CONSUMED_SUCCESS);
                    // 消费确认 虽然消息确实被消费了, 但是由于是手动确认模式, 而最后又没手动确认, 所以, 消息仍被rabbitmq保存。
                    // 所以, 手动ack能够保证消息一定被消费, 但一定要记得basicAck。
                    channel.basicAck(tag, false);
                    return result;
                } catch (Exception e) {
                    log.error("getProxy error", e);
                    channel.basicNack(tag, false, true);
                    return null;
                }
            }
        });
    }

    /**
     * 获取CorrelationId
     *
     * @param message
     * @return
     */
    private String getCorrelationId(Message message) {
        String correlationId = null;

        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        for (Map.Entry entry : headers.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.equals("spring_returned_message_correlation")) {
                correlationId = value;
            }
        }

        return correlationId;
    }

    /**
     * 消息是否已被消费
     *
     * @param correlationId
     * @return
     */
    private boolean isConsumed(String correlationId) {
        MessageLog msgLog = msgLogService.selectByMsgId(correlationId);
        return null == msgLog || msgLog.getStatus().equals(QueueConstants.MessageLogStatus.CONSUMED_SUCCESS);
    }
}