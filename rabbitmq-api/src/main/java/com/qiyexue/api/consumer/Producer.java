package com.qiyexue.api.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 生产者
 *
 * @author 七夜雪
 * @create 2018-12-15 19:56
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        // 1. 创建ConnectionFactory, 并设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = factory.newConnection();

        // 3. 创建channel
        Channel channel = connection.createChannel();


        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.qiye";

        // 发送消息
        String msg = "自定义消费者, 消息发送 : Hello, 七夜雪";
        channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());

        // 关闭连接
        channel.close();
        connection.close();

    }

}
