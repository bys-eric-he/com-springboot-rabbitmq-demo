package com.rabbitmq.mutiple.consumer.repository;

import com.rabbitmq.mutiple.consumer.pojo.LoginLog;
import com.rabbitmq.mutiple.consumer.pojo.QLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long>,
        QuerydslPredicateExecutor<LoginLog>,
        QuerydslBinderCustomizer<QLoginLog> {
    default void customize(QuerydslBindings bindings, QLoginLog loginLog) {
    }
}
