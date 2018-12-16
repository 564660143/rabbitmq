package com.qiyexue.api.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消费者，关闭自动ack
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

        // 4. 声明Exchange
        String exchangeName = "test_ack_exchange";
        String exchangeType = "topic";
        String routingKey = "ack.*";
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        // 5. 声明消息队列
        String queueName = "test_ack_queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // 6. 绑定队列和Exchange
        channel.queueBind(queueName, exchangeName, routingKey);

        // 7. 设置消费者为自定义的消费者, 将autoAck设置为false
        channel.basicConsume(queueName, false, new MyConsumer(channel));

    }

}
