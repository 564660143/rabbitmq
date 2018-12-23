package com.qiyexue.annotation;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiyexue.bean.Order;
import com.qiyexue.bean.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 测试消息发送
 *
 * @author 七夜雪
 * @date 2018-12-23 10:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RabbitConfig.class})
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
        rabbitTemplate.send("topic002", "huangquan.test", message);
    }

    /**
     * 测试json消息
     * @throws Exception
     */
    @Test
    public void testJsonMessage() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        String json = JSON.toJSONString(order);
        System.out.println("JSON --> " + json);
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "biluo.test", message);
        TimeUnit.SECONDS.sleep(2);
    }


    /**
     * 测试java消息
     * @throws Exception
     */
    @Test
    public void testMappingMessage() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "order");
        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        String json = JSON.toJSONString(order);
        System.out.println("order --> " + json);
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "biluo.test", message);


        Packaged pack = new Packaged();
        pack.setId("002");
        pack.setName("包裹消息");
        pack.setDescription("包裹描述信息");
        String json2 = JSON.toJSONString(pack);
        System.out.println("Packaged --> " + json2);

        messageProperties.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "biluo.test", message2);

        TimeUnit.SECONDS.sleep(2);
    }


    /**
     * 测试java消息
     * @throws Exception
     */
    @Test
    public void testJavaMessage() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.qiyexue.bean.Order");
        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        String json = JSON.toJSONString(order);
        System.out.println("JSON --> " + json);
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "biluo.test", message);
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testSendExtConverterMessage() throws Exception {
			byte[] body = Files.readAllBytes(Paths.get("E:/", "1.jpg"));
			MessageProperties messageProperties = new MessageProperties();
			messageProperties.setContentType("image/png");
			messageProperties.getHeaders().put("extName", "png");
			Message message = new Message(body, messageProperties);
			rabbitTemplate.send("topic001", "biluo.test", message);

//        byte[] body = Files.readAllBytes(Paths.get("d:/002_books", "mysql.pdf"));
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/pdf");
//        Message message = new Message(body, messageProperties);
//        rabbitTemplate.send("", "pdf_queue", message);
        TimeUnit.SECONDS.sleep(2);
    }


}
