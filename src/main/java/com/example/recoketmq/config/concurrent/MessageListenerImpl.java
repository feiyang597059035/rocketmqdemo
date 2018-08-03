package com.example.recoketmq.config.concurrent;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class MessageListenerImpl implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        // TODO Auto-generated method stub
        try {
            for (MessageExt messageExt : msgs) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("messageExt: " + messageExt);// 输出消息内容

                String messageBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);

                System.out.println("111消费响应：msgId : " + messageExt.getMsgId() + ", msgBody : " + messageBody);//
                // 输出消息内容
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER; // 稍后再试
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 消费成功
    }

}
