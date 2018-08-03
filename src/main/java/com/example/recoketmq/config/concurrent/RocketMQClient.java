package com.example.recoketmq.config.concurrent;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RocketMQClient {
    /**
     * 生产者的组名
     */
    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    DefaultMQProducer defaultMQProducer = null;

    private static Logger logger = LoggerFactory.getLogger(RocketMQClient.class);

    @PostConstruct
    public void defaultMQProducer() {
        // 生产者的组名
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        // 指定NameServer地址，多个地址以 ; 隔开
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        try {
            System.out.println("启动生产者开始");
            logger.info("启动生产者开始");
            defaultMQProducer.start();
            logger.info("启动生产者完成");
        } catch (MQClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /* try {
            *//**
               * Producer对象在使用之前必须要调用start初始化，初始化一次即可 注意：切记不可以在每次发送消息时，都调用start方法
               *//*
                defaultMQProducer.start();
                
                // 创建一个消息实例，包含 topic、tag 和 消息体
                // 如下：topic 为 "TopicTest"，tag 为 "push"
                Message message =
                new Message("TopicTest", "push", "发送消息----zhisheng-----".getBytes(RemotingHelper.DEFAULT_CHARSET));
                
                StopWatch stop = new StopWatch();
                stop.start();
                
                for (int i = 0; i < 100; i++) {
                SendResult result = producer.send(message);
                System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
                }
                stop.stop();
                System.out.println("----------------发送一万条消息耗时：" + stop.getTotalTimeMillis());
                } catch (Exception e) {
                e.printStackTrace();
                } finally {
                
                producer.shutdown();
                }*/
    }

    public SendResult sendMessage(String topic, String tag, String msg) {
        Message message = new Message(topic, tag, msg.getBytes());
        SendResult sendResult = null;
        try {
            sendResult = defaultMQProducer.send(message);
            logger.info("生产者" + sendResult.toString());

        } catch (MQClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemotingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MQBrokerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sendResult;
    }
}
