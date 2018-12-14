package com.qiyexue.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Fanout模式消费者
 *
 * @author 七夜雪
 * @create 2018-12-14 20:40
 */
public class ConsumerByFanout {

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

        // 4. 声明Exchange
        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        channel.exchangeDeclare(exchangeName, exchangeType);

        // 5. 声明消息队列
        String routingKey = "";
        String queueName = "test_fanout_queue";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 6. 创建消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息 : " + msg);
        }

    }

}
