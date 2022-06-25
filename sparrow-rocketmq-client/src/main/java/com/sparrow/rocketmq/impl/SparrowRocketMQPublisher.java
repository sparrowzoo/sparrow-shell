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
import com.sparrow.container.Container;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.mq.MQClient;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQMessageSendException;
import com.sparrow.mq.MQPublisher;
import com.sparrow.rocketmq.MessageConverter;
import com.sparrow.support.latch.DistributedCountDownLatch;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by harry on 2017/6/14.
 */
public class SparrowRocketMQPublisher implements MQPublisher {
    protected static Logger logger = LoggerFactory.getLogger(SparrowRocketMQPublisher.class);
    private String nameServerAddress;
    private String group;
    private String topic;
    private String tag;
    private DistributedCountDownLatch distributedCountDownLatch;
    private Boolean debug;
    private MQProducer producer;
    private MessageConverter messageConverter;
    private Integer retryTimesWhenSendAsyncFailed = 5;

    public void setDistributedCountDownLatch(DistributedCountDownLatch distributedCountDownLatch) {
        this.distributedCountDownLatch = distributedCountDownLatch;
    }

    public String getNameServerAddress() {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public Integer getRetryTimesWhenSendAsyncFailed() {
        return retryTimesWhenSendAsyncFailed;
    }

    public void setRetryTimesWhenSendAsyncFailed(Integer retryTimesWhenSendAsyncFailed) {
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    /**
     * 发送后count+1
     * 如果有多个consumer 则+n
     * 每个consumer 由不同的应用（进程处理）
     *
     * @param event
     * @param productKey
     * @param msgKey
     */
    public void after(MQEvent event, KEY productKey, String msgKey) {
        if (distributedCountDownLatch == null || productKey == null) {
            return;
        }
        distributedCountDownLatch.product(productKey, msgKey);
    }

    @Override
    public void publish(MQEvent event) {
        this.publish(event, null);
    }


    @Override
    public void publish(MQEvent event, KEY productKey) {
        Message msg = this.messageConverter.createMessage(topic, tag, event);
        String key = UUID.randomUUID().toString();
        msg.setKeys(Collections.singletonList(key));
        if (productKey != null) {
            msg.getProperties().put(MQClient.CONSUMER_KEY, productKey.key());
        }
        logger.info("event {} ,monitor key {},msgKey {}", JsonFactory.getProvider().toString(event), productKey == null ? "" : productKey.key(), key);
        SendResult sendResult = null;
        int retryTimes = 0;
        while (retryTimes < retryTimesWhenSendAsyncFailed) {
            retryTimes++;
            if (retryTimes > 2) {
                logger.warn("event {} retry times {}", event, retryTimes);
            }
            try {
                sendResult = producer.send(msg);
                /**
                 * , new MessageQueueSelector() {
                 *                     @Override
                 *                     public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                 *                         list.get(0).getQueueId()
                 *                         return null;
                 *                     }
                 *                 },event
                 */
                if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new MQMessageSendException(sendResult.toString());
                }
                this.after(event, productKey, key);
                break;
            } catch (Throwable e) {
                logger.warn(e.getClass().getSimpleName() + " retry", e);
                if (retryTimes == retryTimesWhenSendAsyncFailed - 1) {
                    throw new MQMessageSendException("client exception", e);
                }
            }
        }
    }

    public void start() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(group);
        producer.setNamesrvAddr(nameServerAddress);
        producer.setInstanceName(MQClient.INSTANCE_NAME);
        //for product
        if (this.debug == null || !this.debug) {
            producer.setCreateTopicKey(this.getTopic());
        }

        int maxMessageSize = 1024000;
        producer.setMaxMessageSize(maxMessageSize);
        this.producer = producer;
        producer.start();
    }

    @Override
    public void aware(Container container, String beanName) {
        try {
            this.start();
        } catch (MQClientException e) {
            logger.error("mq client exception", e);
        }
    }
}
