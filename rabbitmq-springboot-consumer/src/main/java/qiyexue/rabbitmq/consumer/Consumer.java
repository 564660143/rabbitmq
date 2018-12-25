package qiyexue.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import qiyexue.rabbitmq.bean.Order;

import java.util.Map;

/**
 * 消费者监听
 *
 * @author 七夜雪
 * @date 2018-12-25 19:00
 */
@Component
public class Consumer {

    @RabbitListener(bindings =
        @QueueBinding(value = @Queue(value = "springboot", durable = "true"),
                     exchange = @Exchange(value = "springboot",
                             durable = "true", type = "topic", ignoreDeclarationExceptions = "true"
            ), key = "springboot.*")
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("message : " + message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        // 手工ACK
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(bindings =
    @QueueBinding(value = @Queue(value = "${spring.rabbitmq.listener.queue.name}",
            durable = "${spring.rabbitmq.listener.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.exchange.name}",
            durable = "${spring.rabbitmq.listener.exchange.durable}",
            type = "${spring.rabbitmq.listener.exchange.type}",
            ignoreDeclarationExceptions = "${spring.rabbitmq.listener.exchange.ignoreDeclarationExceptions}"
            ),
            key = "${spring.rabbitmq.listener.key}")
    )
    @RabbitHandler
    public void onMessage(@Payload Order order, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        System.out.println("message : " + order);
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手工ACK
        channel.basicAck(deliveryTag, false);
    }

}
