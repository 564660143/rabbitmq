package com.qiyexue.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * fanout模式生产者
 *
 * @author 七夜雪
 * @date 2018-12-14 20:36
 */
public class ProducerByFanout {

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 2. 获取连接
        Connection connection = factory.newConnection();

        // 3. 创建channel
        Channel channel = connection.createChannel();

        String exchangeName = "test_fanout_exchange";
        String routingKey = "无所谓";
        for (int i = 0; i < 5; i++) {
            String msg = "Fanout 模式消息..";
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        }

        // 关闭连接
        channel.close();
        connection.close();
    }

}
