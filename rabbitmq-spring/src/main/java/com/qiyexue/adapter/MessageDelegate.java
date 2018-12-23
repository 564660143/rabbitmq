package com.qiyexue.adapter;

import com.qiyexue.bean.Order;
import com.qiyexue.bean.Packaged;

import java.io.File;
import java.util.Map;

/**
 * @author 七夜雪
 * @date 2018-12-23 10:27
 */
public class MessageDelegate {

    public void handleMessage(byte[] messageBody) {
        String msg = new String(messageBody);
        System.out.println("默认handleMessage方法, 消息:" + msg);
    }

    public void consumeMessage(String messageBody) {
        System.out.println("自定义方法, 消息:" + messageBody);
    }

    public void handleMessage(String messageBody) {
        System.out.println("字符串方法, 消息内容:" + messageBody);
    }

    public void queue1Method(String messageBody) {
        System.out.println("队列queue001方法 : " + messageBody);
    }

    public void queue2Method(String messageBody) {
        System.out.println("队列queue002方法 : " + messageBody);
    }

    /**
     * Json消息,Java对象消息使用Map接收
     * @param messageBody
     */
    public void consumeMessage(Map messageBody) {
        System.out.println("Json消息 :" + messageBody);
    }

    public void consumeMessage(Order order) {
        System.out.println("Java消息, Order : " + order);
    }

    public void consumeMessage(Packaged packaged) {
        System.out.println("Java消息, Packaged : " + packaged);
    }

    public void consumeMessage(File file) {
        System.err.println("文件对象 方法, 消息内容:" + file.getName());
    }

}
