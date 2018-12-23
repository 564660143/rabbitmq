package com.qiyexue.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
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
        // 设置Message属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("name", "七夜雪");
        messageProperties.getHeaders().put("age", 18);
        Message message = new Message("听雪楼中听雪落".getBytes(), messageProperties);
        // 使用Send方法必须传入message类型
        rabbitTemplate.send("topic001", "biluo.test", message);

        // 使用convertAndSend方法, 可以使用String类型, 或者Object类型消息, 会自动转换
        rabbitTemplate.convertAndSend("topic002", "huangquan.test", "上穷碧落下黄泉", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("----------添加额外设置------------");
                message.getMessageProperties().getHeaders().put("name", "黄泉");
                message.getMessageProperties().getHeaders().put("company", "听雪楼");
                return message;
            }
        });


    }

}