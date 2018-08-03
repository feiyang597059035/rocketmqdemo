package com.example.recoketmq.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.recoketmq.config.concurrent.RocketMQClient;
import com.example.recoketmq.config.order.RocketMQClientOrder;
import com.example.recoketmq.config.transaction.RocketMQClientTransaction;
import com.example.recoketmq.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RocketMQClient rocketMQClient;
    @Autowired
    private RocketMQClientOrder rocketMQClientOrder;
    @Autowired
    private RocketMQClientTransaction rocketMQClientTransaction;
    @Autowired
    private UserService userService;

    /**
     * 发送普通消息
     * 
     * @return
     */
    @RequestMapping("/sendMessage")
    public String sendMessage() {
        for (int i = 0; i < 100; i++) {
            SendResult sendResult = rocketMQClient.sendMessage("TopicTest", "taaa", i + "");
        }

        return "成功";
    }

    /**
     * 发送局部循序消息
     * 
     * @return
     */
    @RequestMapping("/sendOrderMessage")
    public String sendOrderMessage() {
        for (int i = 100; i < 200; i++) {
            SendResult sendResult = rocketMQClientOrder.sendMessage("order", "ordertest", i + "", Long.valueOf(i));
        }

        return "成功";
    }

    @RequestMapping("/sendTransactionMessage")
    public String sendTransactionMessage() throws Exception {
        userService.sendMessage();

        return "成功";
    }

}
