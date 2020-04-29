package com.rabbitmq.mutiple.consumer.repository;

import com.rabbitmq.mutiple.consumer.pojo.OrderCancelLog;
import com.rabbitmq.mutiple.consumer.pojo.QOrderCancelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCancelRepository extends JpaRepository<OrderCancelLog, Long>,
        QuerydslPredicateExecutor<OrderCancelLog>,
        QuerydslBinderCustomizer<QOrderCancelLog> {
    default void customize(QuerydslBindings bindings, QOrderCancelLog orderCancelLog) {
    }
}
