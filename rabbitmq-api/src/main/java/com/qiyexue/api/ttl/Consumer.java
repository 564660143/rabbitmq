package com.qiyexue.api.ttl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者， 设置队列过期时间
 *
 * @author 七夜雪
 * @date 2018-12-13 20:57
 */
public class Consumer {

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

        // 4. 声明(创建)一个队列
        String queueName = "test_ttl_queue";

        Map<String, Object> arguments = new HashMap<>();
        // 设置队列超时时间为10秒
        arguments.put("x-message-ttl", 10000);
        channel.queueDeclare(queueName,true, false, false, arguments);

        // 5. 创建消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 6. 设置channel
        channel.basicConsume(queueName, true, consumer);
        while (true) {
            // 7. 获取消息
            Delivery delivery = consumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
            // 获取head中内容
            System.out.println(delivery.getProperties().getHeaders().get("name"));
        }
        
    }

}
