package com.example.recoketmq.config.transaction;

import java.net.UnknownHostException;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 改造发送事务消息 接口 将发送half 消息 本地方法执行 发送操作状态消息 三者 分离 让程序员自己控制是否消息
 * 
 * @author luog
 * @date 2018年12月1日
 */
public class TransactionMQProducer22 extends TransactionMQProducer {
    public void ss() {
        SendResult sendResult = new SendResult();
        LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
        Throwable localException = new Throwable();
        try {
            this.defaultMQProducerImpl.endTransaction(sendResult, localTransactionState, localException);
        } catch (UnknownHostException e) {
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
    }

}
