package com.qiyexue.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
 *
 * @author 七夜雪
 * @date 2018-12-15 20:07
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂并设置属性
        ConnectionFactory factory = new ConnectionFactory();;
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = factory.newConnection();

        // 3. 创建channel
        Channel channel = connection.createChannel();

        // 4. 声明死信队列Exchange和Queue
        channel.exchangeDeclare("dlx.exchange", "topic");
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

        // 5. 声明普通Exchange
        String exchangeName = "test_dlx_exchange";
        String exchangeType = "topic";
        String routingKey = "dlx.*";
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        // 6. 声明消息队列, 指定死信队列为dlx.exchange
        String queueName = "test_dlx_queue";
        Map<String, Object> arguments = new HashMap<>();
        // x-dead-leeter-exchange属性用于指定死信队列为dlx.exchange
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        channel.queueDeclare(queueName, true, false, false, arguments);

        // 6. 绑定队列和Exchange
        channel.queueBind(queueName, exchangeName, routingKey);

    }

}
