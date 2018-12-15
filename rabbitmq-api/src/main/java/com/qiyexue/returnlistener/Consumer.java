package com.qiyexue.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Return Listener模式消费者
 *
 * @author 七夜雪
 * @create 2018-12-15 20:07
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
        String exchangeName = "test_return_exchange";
        String exchangeType = "topic";
        String routingKey = "return.*";
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        // 5. 声明消息队列
        String queueName = "test_return_queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // 6. 绑定队列和Exchange
        channel.queueBind(queueName, exchangeName, routingKey);

        // 7. 创建一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 8. 设置消费者从哪个队列开始消费, 设置自动ACK
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println(msg);
        }

    }

}
