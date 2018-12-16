package com.qiyexue.api.ack;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者
 *
 * @author 七夜雪
 * @date 2018-12-15 19:56
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


        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.qiye";

        for (int i = 0; i < 5; i++) {
            // 发送消息
            String msg = "Hello, 七夜雪 " + i;
            Map<String, Object> hearder = new HashMap<String, Object>();
            hearder.put("num", i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .contentEncoding("UTF-8")
                    .headers(hearder).build();
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
        }


        // 关闭连接
        channel.close();
        connection.close();

    }

}
