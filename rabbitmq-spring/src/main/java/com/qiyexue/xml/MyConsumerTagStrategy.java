package com.qiyexue.xml;

import org.springframework.amqp.support.ConsumerTagStrategy;

import java.util.UUID;

/**
 * 自定义ConsumerTag
 *
 * @author 七夜雪
 * @date 2018-12-23 8:27
 */
public class MyConsumerTagStrategy implements ConsumerTagStrategy {

    @Override
    public String createConsumerTag(String s) {
        return s + "_" + UUID.randomUUID().toString();
    }
}
