package com.qiyexue.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * topic模式消费者
 *
 * @author 七夜雪
 * @create 2018-12-14 20:10
 */
public class ConsumerByTopic {

    public static void main(String[] args) throws Exception {
        // 创建工厂
        // 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.72.138");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建channel
        Channel channel = connection.createChannel();

        // 声明Exchange
        String exchangeName = "test_topic_exchange";
        String exchangetype = "topic";
        // tingxuelou.#
        String routingKey = "tingxuelou.*";
        channel.exchangeDeclare(exchangeName, exchangetype);

        // 声明队列
        String queueName = "test_topic_queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定队列
        channel.queueBind(queueName, exchangeName, routingKey);

        // 创建消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
        }
    }

}
