package com.qiyexue.annotation;

import com.qiyexue.adapter.MessageDelegate;
import com.qiyexue.bean.Order;
import com.qiyexue.bean.Packaged;
import com.qiyexue.convert.ImageMessageConverter;
import com.qiyexue.convert.PDFMessageConverter;
import com.qiyexue.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 基于Java类的方式配置
 *
 * @author 七夜雪
 * @date 2018-12-19 20:28
 */
@Configuration
@ComponentScan("com.qiyexue.*")
public class RabbitConfig {

    /**
     * 配置ConnectionFactory
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("192.168.72.138:5672");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        return factory;
    }

    /**
     * 声明RabbitAdmin
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 需要将AutoStartup设置为true
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        TopicExchange exchange = new TopicExchange("topic001", false, false);
        return exchange;
    }

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue queue001() {
        return new Queue("queue001", false);
    }

    /**
     * 声明绑定
     *
     * @return
     */
    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("biluo.*");
    }

    /**
     * RabbitTemplate配置
     * RabbitTemplate主要用于消息发送
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     *
     * 配置MessageListener容器
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 添加要监听的队列
//        container.setQueues(queue001());
        // 添加要监听的队列, 传入队列名, 和setQueues使用其中一个即可
        container.setQueueNames("queue001","queue002", "queue003");
        // 设置当前消费者格式
        container.setConcurrentConsumers(1);
        // 设置最大消费者个数
        container.setMaxConcurrentConsumers(5);
        // 是否重回队列
        container.setDefaultRequeueRejected(false);
        // 设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        // 设置消费者标签
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

        // 设置监听MessageListener
/*
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.out.println(msg);
            }
        });
*/

        /**
         * 适配器方式 : 默认的方法名字的：handleMessage
         * 可以自己指定方法名
         * 也可以添加一个转换器, 将字节数组转换为String, 默认简单消息也是会转换成String的
         */
/*        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);*/


        /**
         * 适配器模式, 队列与方法名绑定
         */
/*        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String, String> queueToMethodMap = new HashMap<>();
        queueToMethodMap.put("queue001", "queue1Method");
        queueToMethodMap.put("queue002", "queue2Method");
        adapter.setQueueOrTagToMethodName(queueToMethodMap);
        container.setMessageListener(adapter);*/

        // 1.Json消息
/*        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter jsonCoverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jsonCoverter);
        container.setMessageListener(adapter);*/

//        2. DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        converter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);*/


        // 3. DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
/*        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
        idClassMapping.put("order", Order.class);
        idClassMapping.put("packaged", Packaged.class);

        javaTypeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);*/

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);


        adapter.setMessageConverter(convert);
        container.setMessageListener(adapter);


        return container;
    }


}
