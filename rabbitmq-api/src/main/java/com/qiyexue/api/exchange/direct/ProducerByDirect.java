package com.qiyexue.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Direct模式的生产者
 *
 * @author 七夜雪
 * @date 2018-12-13 22:00
 */
public class ProducerByDirect {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 2. 获取连接
        Connection connection = factory.newConnection();

        // 3. 创建channel
        Channel channel = connection.createChannel();

        // 4. 声明
        String exchangeName = "test_direct_exchange";
        // Direct模式必须和消费者保持一致才能发送消息, 不然消息会被丢弃
        String routingKey = "test.direct";

        // 5. 发送消息
        String msg = "Hello RabbitMQ By Direct";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        // 6. 关闭连接
        channel.close();
        connection.close();
    }

}
