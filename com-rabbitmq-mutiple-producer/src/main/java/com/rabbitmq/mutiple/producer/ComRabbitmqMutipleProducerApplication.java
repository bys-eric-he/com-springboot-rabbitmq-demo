package com.rabbitmq.mutiple.producer;

import com.rabbitmq.mutiple.common.constants.QueueConstants;
import com.rabbitmq.mutiple.common.utils.JodaTimeUtil;
import com.rabbitmq.mutiple.common.utils.RandomUtil;
import com.rabbitmq.mutiple.consumer.pojo.LoginLog;
import com.rabbitmq.mutiple.consumer.pojo.MessageLog;
import com.rabbitmq.mutiple.consumer.pojo.OrderCancelLog;
import com.rabbitmq.mutiple.consumer.proxy.helper.MessageHelper;
import com.rabbitmq.mutiple.consumer.service.MessageLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.UUID;

/**
 * 由于引用了consumer模块,且包名和当前模块(producer)不一致, 因此在启动producer模块时，无法自动扫描到consumer模块的注解
 * 因为SpringBoot有一个规则，默认扫描的是启动器所在的路徑下的包和文件，所以无法扫描到其它模块中的实例.
 * 需要手动在启动类加上@EntityScan(扫描Entity)、@EnableJpaRepositories(扫描Repository)、@ComponentScan(扫描Service及Component)注解
 * 这样才能将其它引入的模块的实例注入到Spring容器
 */
@SpringBootApplication
@ComponentScan({"com.rabbitmq.mutiple.producer.*", "com.rabbitmq.mutiple.consumer.*"})
@EnableJpaRepositories("com.rabbitmq.mutiple.consumer.repository")
@EntityScan("com.rabbitmq.mutiple.consumer.pojo")
@EnableScheduling
public class ComRabbitmqMutipleProducerApplication {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageLogService messageLogService;

    public static void main(String[] args) {
        SpringApplication.run(ComRabbitmqMutipleProducerApplication.class, args);
    }

    /**
     * 每隔15秒发送一次消息
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendOrderCancelMessage() {

        String msgId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(msgId);

        OrderCancelLog orderCancelLog = new OrderCancelLog();
        orderCancelLog.setUserId(RandomUtil.random(100, 1));
        orderCancelLog.setOrderId(RandomUtil.UUID36());
        Date date = new Date();
        orderCancelLog.setDescription("用户" + orderCancelLog.getUserId() + ", 在" + JodaTimeUtil.dateToStr(date) + " " +
                "取消了订单!");
        orderCancelLog.setCreateTime(date);
        orderCancelLog.setUpdateTime(date);
        orderCancelLog.setMsgId(msgId);

        try {
            rabbitTemplate.convertAndSend(QueueConstants.QueueOrderCancelEnum.QUEUE_ORDER_CANCEL.getExchange(),
                    QueueConstants.QueueOrderCancelEnum.QUEUE_ORDER_CANCEL.getRouteKey(),
                    MessageHelper.objToMsg(orderCancelLog),
                    correlationData);

            MessageLog msgLog = new MessageLog(msgId, orderCancelLog,
                    QueueConstants.QueueOrderCancelEnum.QUEUE_ORDER_CANCEL.getExchange(),
                    QueueConstants.QueueOrderCancelEnum.QUEUE_ORDER_CANCEL.getRouteKey());
            messageLogService.insert(msgLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每隔10秒发送一次消息
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void sendLoginMessage() {


        String msgId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(msgId);

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(RandomUtil.random(100, 1));
        loginLog.setType(QueueConstants.LoginType.LOGIN);
        Date date = new Date();
        loginLog.setDescription("用户" + loginLog.getUserId() + ", 在" + JodaTimeUtil.dateToStr(date) + " 登录了系统!");
        loginLog.setCreateTime(date);
        loginLog.setUpdateTime(date);
        loginLog.setMsgId(msgId);

        try {
            rabbitTemplate.convertAndSend(QueueConstants.QueueLoginLog.LOGIN_LOG_EXCHANGE_NAME,
                    QueueConstants.QueueLoginLog.LOGIN_LOG_ROUTING_KEY_NAME, MessageHelper.objToMsg(loginLog),
                    correlationData);

            MessageLog msgLog = new MessageLog(msgId, loginLog, QueueConstants.QueueLoginLog.LOGIN_LOG_EXCHANGE_NAME,
                    QueueConstants.QueueLoginLog.LOGIN_LOG_ROUTING_KEY_NAME);
            messageLogService.insert(msgLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
