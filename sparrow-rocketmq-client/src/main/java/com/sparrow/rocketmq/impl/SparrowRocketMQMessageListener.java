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

import com.sparrow.cache.Key;
import com.sparrow.mq.*;
import com.sparrow.rocketmq.MessageConverter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SparrowRocketMQMessageListener implements MessageListenerConcurrently {
    private static Logger logger = LoggerFactory.getLogger(SparrowRocketMQMessageListener.class);

    public SparrowRocketMQMessageListener() {
        logger.info("init spring rocket mq message listener");
    }

    private EventHandlerMappingContainer queueHandlerMappingContainer = MQContainerProvider.getContainer();
    private MessageConverter messageConverter;
    private MQIdempotent mqIdempotent;

    public void setQueueHandlerMappingContainer(EventHandlerMappingContainer queueHandlerMappingContainer) {
        this.queueHandlerMappingContainer = queueHandlerMappingContainer;
    }

    public void setMqIdempotent(MQIdempotent mqIdempotent) {
        this.mqIdempotent = mqIdempotent;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    protected boolean duplicate(MQEvent event, Key consumerKey, String keys) {
        return mqIdempotent != null && mqIdempotent.duplicate(keys);
    }


    protected long getLockMills() {
        return 1000 * 60;
    }

    protected int getIdempotentSeconds() {
        return 60 * 60 * 72;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        logger.info("thread-name:{},message-size:{}", Thread.currentThread().getName(), list.size());
        for (MessageExt message : list) {
            String type = message.getProperties().get(MQClient.CLASS_NAME);
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("receive msg:" + message);
                }
                MQHandler handler = queueHandlerMappingContainer.get(type);
                if (handler == null) {
                    logger.error("handler of this type [{}] not found", type);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                MQEvent event = messageConverter.fromMessage(message);
                if (!this.mqIdempotent.tryLock(message.getKeys(), this.getLockMills())) {
                    if (this.mqIdempotent.duplicate(message.getKeys())) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } else {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                handler.handle(event);
                this.mqIdempotent.consumed(message.getKeys(), this.getIdempotentSeconds());
            } catch (Throwable e) {
                logger.error("process failed, msg : " + message, e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
