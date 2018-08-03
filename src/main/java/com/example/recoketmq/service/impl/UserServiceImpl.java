package com.example.recoketmq.service.impl;

import javax.annotation.Resource;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recoketmq.config.concurrent.RocketMQClient;
import com.example.recoketmq.config.transaction.RocketMQClientTransaction;
import com.example.recoketmq.config.transaction.TransactionExecuterImpl;
import com.example.recoketmq.mapper.UserMapper;
import com.example.recoketmq.model.User;
import com.example.recoketmq.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private RocketMQClientTransaction rocketMQClientTransaction;

    @Autowired
    private RocketMQClient rocketMQClient;

    @Resource
    private UserMapper userMapper;
    @Autowired
    private TransactionExecuterImpl transactionExecuterImpl;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SendResult sendMessage() throws Exception {
        User user = new User();
        user.setPassword("ewee");
        user.setUserName("ttt");
        userMapper.insert(user);

        for (int i = 100; i < 101; i++) {
            SendResult sendResult = rocketMQClientTransaction.sendMessage("transaction", "transaction", i + "",
                Long.valueOf(i), transactionExecuterImpl);
        }

        for (int i = 0; i < 5; i++) {
            SendResult sendResult = rocketMQClient.sendMessage("TopicTest", "taaa", i + "");
        }

        /*  throw new RuntimeException();*/
        return null;

    }

}
