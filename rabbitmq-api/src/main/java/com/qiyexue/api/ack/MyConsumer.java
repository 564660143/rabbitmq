package com.qiyexue.api.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义消费者
 * 设置手动ack和重回队列
 * @author 七夜雪
 * @date 2018-12-16 8:20
 */
public class MyConsumer extends DefaultConsumer {
    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("-------------自定义消费者------------");
        System.out.println("consumerTag : " + consumerTag);
        System.out.println("envelope : " + envelope);
        System.out.println("properties : " + properties);
        System.out.println("body : " + new String(body));
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ((Integer)properties.getHeaders().get("num") == 0) {
            // 三个参数 : DeliveryTag, 是否批量拒绝, 是否可以重回队列
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }

    }
}
