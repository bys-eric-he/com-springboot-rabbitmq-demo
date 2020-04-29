package com.rabbitmq.mutiple.consumer.repository;

import com.rabbitmq.mutiple.consumer.pojo.MessageLog;
import com.rabbitmq.mutiple.consumer.pojo.QMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long>,
        QuerydslPredicateExecutor<MessageLog>,
        QuerydslBinderCustomizer<QMessageLog> {
    default void customize(QuerydslBindings bindings, QMessageLog messageLog) {
    }
}
