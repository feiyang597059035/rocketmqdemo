/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.example.recoketmq.config.transaction;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.recoketmq.mapper.UserMapper;
import com.example.recoketmq.model.User;

/**
 * 本地事物执行器
 * 
 * @author luog
 * @date 2018年7月20日
 */

@Component
public class TransactionExecuterImpl implements LocalTransactionExecuter {
    @Autowired
    private UserMapper userMapper;
    private AtomicInteger transactionIndex = new AtomicInteger(1);

    // 4.2.0 版本需要增加实现事物方法
    /* @Override
    public LocalTransactionState executeLocalTransactionBranch(final Message msg, final Object arg) {
        int value = transactionIndex.getAndIncrement();
    
        if (value == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    
        return LocalTransactionState.UNKNOW;
    }*/
    /**
     * 主要是在这么判断是否 需要消息回滚 在业务执行完后 再发送消息 再判断事物是否执行成功， 如果成功给rocketmq 发送成功标识。 先将mq 消息插入到
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalTransactionState executeLocalTransactionBranch(final Message msg, final Object arg) {

        try {

            System.out.println("本地逻辑执行了。。。");
            // Message Body
            // Order order = FastJsonUtil.jsonToBean(new String(msg.getBody(), "utf-8"), Order.class);

            // order.setState(Order.COMPLETED);
            // 将之前的订单 根据用户id和订单号 修改为已确认发送状态
            // this.orderMapper.update(order);
            User user = new User();
            user.setPassword("ewe33e");
            user.setUserName("88");
            user.setId(36);
            int i = userMapper.updateByPrimaryKey(user);
            if (i > 0) {
                // 成功通知MQ消息变更 该消息变为：<确认发送>
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            System.out.println(i);
            return LocalTransactionState.ROLLBACK_MESSAGE;

            // 这里方便测试 自定义事务回查 所以返回 未知状态
            // return LocalTransactionState.UNKNOW;

        } catch (Exception e) {
            System.out.println(3333);
            e.printStackTrace();
            // 失败回滚
            return LocalTransactionState.ROLLBACK_MESSAGE;

        }

    }

}
