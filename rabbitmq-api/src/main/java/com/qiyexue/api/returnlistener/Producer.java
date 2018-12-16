package com.qiyexue.api.returnlistener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Return Listener模式生产者
 *
 * @author 七夜雪
 * @create 2018-12-15 19:56
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

        // 4. 设置Return Listener监听
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("--------------Return Listener--------------");
                System.out.println(replyCode);
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);
                System.out.println(properties);
                System.out.println(new String(body));
            }
        });

        String exchangeName = "test_return_exchange";
        String routingKey = "return.qiye";
        String routingErrorKey = "error.qiye";

        // 发送消息
        String msg = "Send Msg And Return Listener By RoutingKey : ";
        channel.basicPublish(exchangeName, routingKey, true, null, (msg + routingKey).getBytes());
        channel.basicPublish(exchangeName, routingErrorKey,true, null, (msg + routingErrorKey).getBytes());

    }

}
