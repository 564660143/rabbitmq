package com.qiyexue.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topic模式生产者
 *
 * @author 七夜雪
 * @create 2018-12-14 8:07
 */
public class ProducerByTopic {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建channel
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "tingxuelou.biluo";
        String routingKey2 = "tingxuelou.hongchen";
        String routingKey3 = "tingxuelou.hufa.zimo";

        String msg = "test topic By routingKey : ";
        channel.basicPublish(exchangeName, routingKey1, null, (msg + routingKey1).getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, (msg + routingKey2).getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, (msg + routingKey3).getBytes());

        // 关闭连接
        channel.close();
        connection.close();

    }

}
