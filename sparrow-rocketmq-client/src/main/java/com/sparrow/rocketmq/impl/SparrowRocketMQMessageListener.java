/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.rocketmq.impl;

import com.sparrow.constant.cache.KEY;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.mq.MQClient;
import com.sparrow.mq.MQContainerProvider;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQHandler;
import com.sparrow.mq.MQIdempotent;
import com.sparrow.rocketmq.MessageConverter;
import com.sparrow.support.latch.DistributedCountDownLatch;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparrowRocketMQMessageListener implements MessageListenerConcurrently {
    private static Logger logger = LoggerFactory.getLogger(SparrowRocketMQMessageListener.class);

    public SparrowRocketMQMessageListener() {
        logger.info("init spring rocket mq message listener");
    }

    private EventHandlerMappingContainer queueHandlerMappingContainer = MQContainerProvider.getContainer();
    private MessageConverter messageConverter;
    private DistributedCountDownLatch distributedCountDownLatch;

    private MQIdempotent mqIdempotent;

    public void setQueueHandlerMappingContainer(EventHandlerMappingContainer queueHandlerMappingContainer) {
        this.queueHandlerMappingContainer = queueHandlerMappingContainer;
    }

    public void setDistributedCountDownLatch(DistributedCountDownLatch distributedCountDownLatch) {
        this.distributedCountDownLatch = distributedCountDownLatch;
    }

    public void setMqIdempotent(MQIdempotent mqIdempotent) {
        this.mqIdempotent = mqIdempotent;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    protected boolean duplicate(MQEvent event, KEY consumerKey, String keys) {
        return mqIdempotent != null && mqIdempotent.duplicate(keys);
    }

    protected void consumed(MQEvent event, KEY consumerKey, String keys) {
        //must be idempotent
        if (mqIdempotent == null) {
            return;
        }
        //must be consume successful
        if (!mqIdempotent.consumed(keys)) {
            //如果未消费成功，说明断网，或者已经被消费过
            return;
        }
        //如果在这里断电，则count 数会少减,使用事务或脚本保证原子
        //count down
        if (distributedCountDownLatch != null && consumerKey != null) {
            distributedCountDownLatch.consume(consumerKey, keys);
        }
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        System.err.println(String.format("thread-name:%s,message-size:%s", Thread.currentThread().getName(), list.size()));
        logger.info("thread-name:{},message-size:{}", Thread.currentThread().getName(), list.size());
        for (MessageExt message : list) {
            String type = message.getProperties().get(MQClient.CLASS_NAME);
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("receive msg:" + message.toString());
                }
                MQHandler handler = queueHandlerMappingContainer.get(type);
                if (handler == null) {
                    logger.warn("handler of this type [{}] not found", type);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                MQEvent event = messageConverter.fromMessage(message);
                KEY consumerKey = KEY.parse(message.getProperties().get(MQClient.CONSUMER_KEY));
                if (this.duplicate(event, consumerKey, message.getKeys())) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                handler.handle(event);
                this.consumed(event, consumerKey, message.getKeys());
            } catch (Throwable e) {
                logger.error("process failed, msg : " + message, e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
