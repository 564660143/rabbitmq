package com.qiyexue.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author 七夜雪
 * @create 2018-12-13 20:43
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
        for (int i = 0; i < 5; i++) {
            // 不指定exchange的情况下, 使用默认的exchange, routingKey与队列名相等
            channel.basicPublish("", "test01", null, msg.getBytes());
        }

        // 5. 关闭连接
        channel.close();
        connection.close();
    }

}
