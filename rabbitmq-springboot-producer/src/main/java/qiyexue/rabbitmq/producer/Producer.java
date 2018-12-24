package qiyexue.rabbitmq.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 生产者
 *
 * @author 七夜雪
 * @date 2018-12-24 20:30
 */
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 实现RabbitTemplate.ConfirmCallback接口, 这里使用lambda表达式实现
    // confirm(CorrelationData correlationData, boolean ack, String cause)
    private final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.out.println("correlationData : " + correlationData);
        System.out.println("ack : " + ack);
        if (!ack) {
            System.err.println("ConfirmCallback发生异常....");
        }
    };

    // 实现RabbitTemplate.ReturnCallback接口, 这里使用lambda表达式实现
    /*public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey)*/
    private final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText,
                                                                  exchange, routingKey) -> {
        System.out.println("message : " + message);
        System.out.println("replyCode : " + replyCode);
        System.out.println("replyText : " + replyText);
        System.out.println("exchange : " + exchange);
        System.out.println("routingKey : " + routingKey);
    };

    public void sendMsg(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData("123456789");
        rabbitTemplate.convertAndSend("springboot", "springboot.test", msg, correlationData);
    }

}
