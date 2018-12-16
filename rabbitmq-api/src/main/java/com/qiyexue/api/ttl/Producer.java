package com.qiyexue.api.ttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者
 *
 * @author 七夜雪
 * @date 2018-12-13 20:43
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = factory.newConnection();

        // 3. 使用connection创建channel
        Channel channel = connection.createChannel();

        // 4. 通过channel发送消息
        String msg = "hello rabbitmq!";
        AMQP.BasicProperties properties = new AMQP.BasicProperties();
        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put("name", "七夜雪");
        properties = properties.builder()
                // 设置编码为UTF8
                .contentEncoding("UTF-8")
                // 设置自定义Header
                .headers(headers)
                // 设置消息失效时间
                .expiration("5000").build();

        // 设置了消息超时时间为5秒, 5秒后消息自动删除
        channel.basicPublish("", "test_ttl_queue", properties, msg.getBytes());
        // 没有设置消息存活时间,消息存活时间根据队列来决定
        channel.basicPublish("", "test_ttl_queue", null, msg.getBytes());

        // 5. 关闭连接
        channel.close();
        connection.close();
    }

}
