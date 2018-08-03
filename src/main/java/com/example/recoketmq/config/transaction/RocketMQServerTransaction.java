package com.example.recoketmq.config.transaction;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RocketMQServerTransaction {

    /**
     * 消费者的组名
     */
    @Value("${apache.rocketmq.consumer.PushConsumerTransaction}")
    private String consumerGroup;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void defaultMQPushConsumer() {
        // 消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        // 指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            // 订阅PushTopic下Tag为push的消息
            consumer.subscribe("transaction", "transaction");

            // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            // 如果非第一次启动，那么按照上次消费的位置继续消费
            // 3.1 CONSUME_FROM_LAST_OFFSET：第一次启动从队列最后位置消费，后续再启动接着上次消费的进度开始消费
            // 3.2 CONSUME_FROM_FIRST_OFFSET：第一次启动从队列初始位置消费，后续再启动接着上次消费的进度开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

            consumer.registerMessageListener((MessageListenerConcurrently)(list, context) -> {

                try {
                    for (MessageExt messageExt : list) {

                        System.out.println("messageExt: " + messageExt);// 输出消息内容

                        String messageBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);

                        System.out.println("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + messageBody);// 输出消息内容
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; // 稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 消费成功
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}