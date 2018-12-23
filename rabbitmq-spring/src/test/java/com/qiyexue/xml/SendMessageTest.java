package com.qiyexue.xml;

import com.qiyexue.annotation.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * 测试消息发送
 *
 * @author 七夜雪
 * @date 2018-12-23 10:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class SendMessageTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 测试RabbitTemplate
     */
    @Test
    public void testTemplate() throws InterruptedException {
        // 设置Message属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("听雪楼中听雪落".getBytes(), messageProperties);
        // 使用Send方法必须传入message类型
        rabbitTemplate.send("topic001", "biluo.test", message);
        TimeUnit.SECONDS.sleep(2);

    }

    @Test
    public void testSendMessage4Text() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("听雪楼中听雪落...".getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "biluo.test", message);
    }

}
