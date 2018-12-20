package com.qiyexue.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RabbitAdmin测试类
 *
 * @author 七夜雪
 * @date 2018-12-19 8:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class RabbitAdminTest {
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testRabbitAdmin() {
        // 创建Direct模式Exchange
        rabbitAdmin.declareExchange(new DirectExchange("test_spring_declare_exchange", false, false));
        // 创建Topic模式Exchange
        rabbitAdmin.declareExchange(new TopicExchange("test_spring_topic_exchange", false, false));
        // 创建Fanout
        rabbitAdmin.declareExchange(new FanoutExchange("test_spring_fanout_exchange", false, false));
        // 创建队列
        rabbitAdmin.declareQueue(new Queue("test_spring_direct_queue", false));
        rabbitAdmin.declareQueue(new Queue("test_spring_topic_queue", false));
        rabbitAdmin.declareQueue(new Queue("test_spring_fanout_queue", false));

        // 可以直接创建绑定, new的Queue和Exchange必须在上面声明过
        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test_spring_topic_queue", false))
                        .to(new TopicExchange("test_spring_topic_exchange", false, false))
                        .with("sping.*"));
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test_spring_fanout_queue", false))
                        .to(new FanoutExchange("test_spring_fanout_exchange", false, false)));

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试RabbitTemplate
     */
    @Test
    public void testTemplate(){
        System.out.println(rabbitTemplate);
    }

}