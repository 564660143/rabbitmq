package com.qiyexue;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试
 *
 * @author 七夜雪
 * @date 2018-12-17 21:29
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        RabbitAdmin rabbitAdmin = (RabbitAdmin)context.getBean("rabbitAdmin");
        RabbitTemplate rabbitTemplate = rabbitAdmin.getRabbitTemplate();
        Message message = new Message("spring message".getBytes(), null);
        rabbitTemplate.send("direct-exchange", "test_spring_direct_queue", message);
    }

}
