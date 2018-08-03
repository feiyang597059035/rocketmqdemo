package com.example.recoketmq.config.transaction;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 产生事物消息
 * 
 * @author luog
 * @date 2018年7月20日
 */
@Component
public class RocketMQClientTransaction {
    /**
     * 生产者的组名
     */
    @Value("${apache.rocketmq.producer.producerGroupTransaction}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    TransactionMQProducer defaultMQProducer = null;

    private static Logger logger = LoggerFactory.getLogger(RocketMQClientTransaction.class);

    @PostConstruct
    public void defaultMQProducer() {
        // 生产者的组名
        defaultMQProducer = new TransactionMQProducer(producerGroup);

        TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        defaultMQProducer.setTransactionCheckListener(transactionCheckListener);
        // 指定NameServer地址，多个地址以 ; 隔开
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setCheckThreadPoolMinSize(2);
        defaultMQProducer.setCheckThreadPoolMaxSize(2);
        defaultMQProducer.setCheckRequestHoldMax(2000);
        try {

            logger.info("启动order生产者开始");
            defaultMQProducer.start();
            logger.info("启动order生产者完成");
        } catch (MQClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*  String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();
        for (int i = 0; i < 100; i++) {
            try {
                Message msg = new Message("transaction", "transaction", "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = defaultMQProducer.sendMessageInTransaction(msg, tranExecuter, null);
                System.out.printf("%s%n", sendResult);
        
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/
    }

    // 发送事物消息
    public SendResult sendMessage(String topic, String tag, String msg, Long orderId,
        TransactionExecuterImpl tranExecuter) throws Exception {
        Message message = new Message(topic, tag, orderId.toString(), msg.getBytes());
        SendResult sendResult = null;

        sendResult = defaultMQProducer.sendMessageInTransaction(message, tranExecuter, null);
        logger.info("生产者：" + orderId + " " + sendResult.toString());

        return sendResult;
    }
}
