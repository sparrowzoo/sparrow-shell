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

package com.sparrow.kafka.impl;

import com.sparrow.constant.cache.KEY;
import com.sparrow.container.Container;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.kafka.MessageConverter;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQMessageSendException;
import com.sparrow.mq.MQPublisher;
import com.sparrow.mq.MQ_CLIENT;
import com.sparrow.concurrent.latch.DistributedCountDownLatch;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by harry on 2017/6/14.
 */
public class SparrowKafkaMQPublisher implements MQPublisher {
    protected static Logger logger = LoggerFactory.getLogger(SparrowKafkaMQPublisher.class);
    private String nameServerAddress;
    private String group;
    private String topic;
    private DistributedCountDownLatch distributedCountDownLatch;
    private KafkaProducer<String, String> producer;
    private MessageConverter messageConverter;
    private Integer retryTimesWhenSendAsyncFailed = 5;
    private Boolean isAsync = false;

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
        String key = UUID.randomUUID().toString();
        ProducerRecord<String, String> msg = this.messageConverter.createMessage(topic, key, event);
        if (productKey != null) {
            msg.headers().add(MQ_CLIENT.CONSUMER_KEY, productKey.key().getBytes());
        }
        logger.info("event {} ,monitor key {},msgKey {}", JsonFactory.getProvider().toString(event), productKey == null ? "" : productKey.key(), key);
        if (this.isAsync) {
            int retryTimes = 0;
            while (retryTimes < retryTimesWhenSendAsyncFailed) {
                retryTimes++;
                if (retryTimes > 2) {
                    logger.warn("event {} retry times {}", event, retryTimes);
                }
                try {
                    RecordMetadata recordMetadata = producer.send(msg).get();
                    if (recordMetadata != null) {
                        this.after(event, productKey, key);
                    }
                    break;
                } catch (Throwable e) {
                    logger.warn(e.getClass().getSimpleName() + " retry", e);
                    if (retryTimes == retryTimesWhenSendAsyncFailed - 1) {
                        throw new MQMessageSendException("client exception", e);
                    }
                }
            }
            return;
        }

        //todo 异步发送超时处理
        producer.send(msg, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if(exception==null) {
                    SparrowKafkaMQPublisher.this.after(event, productKey, key);
                }
                else {
                    logger.error("send error",exception);
                }
            }
        });
    }

    public void start() throws MQClientException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, nameServerAddress);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
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
