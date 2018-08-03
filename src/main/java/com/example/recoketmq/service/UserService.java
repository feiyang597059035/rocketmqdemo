package com.example.recoketmq.service;

import org.apache.rocketmq.client.producer.SendResult;

public interface UserService {
    public SendResult sendMessage() throws Exception;

}
