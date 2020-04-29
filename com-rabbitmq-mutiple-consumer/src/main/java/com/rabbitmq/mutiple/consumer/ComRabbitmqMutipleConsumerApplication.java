package com.rabbitmq.mutiple.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.rabbitmq.mutiple.consumer.*", "com.rabbitmq.mutiple.common.*"})
public class ComRabbitmqMutipleConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComRabbitmqMutipleConsumerApplication.class, args);
    }

}
