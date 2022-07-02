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

import com.sparrow.mq.MQClient;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.Pair;
import com.sparrow.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UnknownFormatConversionException;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMQConsumer implements ContainerAware {
    private static Logger log = LoggerFactory.getLogger(RocketMQConsumer.class);

    private String instanceName;

    private String groupName;

    private MessageListenerConcurrently messageListener;

    private List<TopicTagPair> topicConfigList;

    private String topicConfig;

    private String messageModel = "CLUSTERING";

    private String consumeFromLastOffset = "CONSUME_FROM_LAST_OFFSET";

    private String nameServerAddress;

    private Integer threadCount;

    private int batchSize = 1;
    private int pullSize;

    private long pullInterval = 0;

    /**
     * 消费线程的数量
     */
    protected static final Integer DEFAULT_CONSUMER_THREAD_NUM = 20;

    public Integer getThreadCount() {
        if (threadCount == null) {
            this.threadCount = DEFAULT_CONSUMER_THREAD_NUM;
        }
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMessageListener(MessageListenerConcurrently messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * 格式 topic1:tag1,tag1,tag3|topic2:tag1,tag2,tag3
     *
     * @param topicConfig
     */
    public void setTopicConfig(String topicConfig) {
        this.topicConfig = topicConfig;
    }

    public void setMessageModel(String messageModel) {
        this.messageModel = messageModel;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setPullSize(int pullSize) {
        this.pullSize = pullSize;
    }

    public void setPullInterval(long pullInterval) {
        this.pullInterval = pullInterval;
    }

    public List<TopicTagPair> getTopicConfigList() {
        if (this.topicConfigList != null) {
            return this.topicConfigList;
        }
        this.topicConfigList = new ArrayList<TopicTagPair>();
        if (!topicConfig.contains(Symbol.COLON)) {
            throw new UnknownFormatConversionException("format error for example topic1:tag1,tag1,tag3|topic2:tag1,tag2,tag3");
        }
        String[] topicArray = topicConfig.split("\\|");
        for (String topic : topicArray) {
            TopicTagPair topicTagPair = new TopicTagPair();
            Pair<String, String> topicTagsPair = Pair.split(topic, Symbol.COLON);
            topicTagPair.setTopic(topicTagsPair.getFirst());
            String[] tagArray = topicTagsPair.getSecond().split(",");
            topicTagPair.setTags(Arrays.asList(tagArray));
            this.topicConfigList.add(topicTagPair);
        }
        return topicConfigList;
    }

    public void setConsumeFromLastOffset(String consumeFromLastOffset) {
        this.consumeFromLastOffset = consumeFromLastOffset;

    }

    public String getNameServerAddress() {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public String getInstanceName() {
        if (this.instanceName == null) {
            this.instanceName = MQClient.INSTANCE_NAME;
        }
        return instanceName;
    }

    private volatile MQPushConsumer consumer;

    public synchronized void start() {
        try {
            consumer = createConsumer(this.batchSize, this.pullSize);
            log.info("begin start ROCKET MQ client, group={}, nameServerAddr {}, topic={},config={}", groupName, nameServerAddress, topicConfig, consumer);
            consumer.start();
        } catch (Exception e) {
            log.error("start mq " + this.getClass().getName() + " failed：", e);
            throw new RuntimeException("failed start ROCKET MQ client, Exception: ", e);
        }
    }

    protected MQPushConsumer createConsumer() {
        return createConsumer(null, null);
    }

    private void setConsumeThread(DefaultMQPushConsumer consumer) {
        consumer.setConsumeThreadMax(this.getThreadCount());
        consumer.setConsumeThreadMin(this.getThreadCount());
    }

    protected MQPushConsumer createConsumer(Integer batchSize, Integer pullSize) {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
            MessageModel messageModel = MessageModel.CLUSTERING;
            if (this.messageModel != null) {
                messageModel = MessageModel.valueOf(this.messageModel);
            }
            defaultMQPushConsumer.setMessageModel(messageModel);
            ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
            if (this.consumeFromLastOffset != null) {
                consumeFromWhere = ConsumeFromWhere.valueOf(this.consumeFromLastOffset);
            }
            defaultMQPushConsumer.setConsumeFromWhere(consumeFromWhere);
            defaultMQPushConsumer.setNamesrvAddr(nameServerAddress);
            if (batchSize != null && batchSize > 0) {
                defaultMQPushConsumer.setConsumeMessageBatchMaxSize(batchSize);
            }
            if (pullSize != null && pullSize > 0) {
                System.err.println("pull size:" + pullSize);
                defaultMQPushConsumer.setPullBatchSize(pullSize);
            }
            //消费一批消息，最大数。因为一批消息如果有一个失败，都会失败，所以这里设置为1
            //defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            //setConsumeThread(defaultMQPushConsumer);
            //订阅多个topic
            for (TopicTagPair topicTagPair : this.getTopicConfigList()) {
                defaultMQPushConsumer.subscribe(topicTagPair.getTopic(), StringUtility.join(topicTagPair.getTags(), Symbol.VERTICAL_LINE));
            }
            defaultMQPushConsumer.setInstanceName(getInstanceName() + Symbol.UNDERLINE + groupName);
            defaultMQPushConsumer.registerMessageListener(messageListener);
            defaultMQPushConsumer.setPullInterval(this.pullInterval);
            defaultMQPushConsumer.setConsumeThreadMin(this.threadCount);
            log.info("finished rocket mq client start!");
            return defaultMQPushConsumer;
        } catch (Exception e) {
            log.error("start " + this.getClass().getName() + " failed：", e);
            throw new RuntimeException("failed start rocket mq client, Exception : ", e);
        }
    }

    public void setPullSize(int batchSize, int pullSize) {
        this.consumer.shutdown();
        this.consumer = this.createConsumer(batchSize, pullSize);
    }

    public synchronized void shutdown() {
        try {
            consumer.shutdown();
        } catch (Exception e) {
            log.error("shutdown rocket mq fail", e);
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        this.start();
    }

    public void setTopicConfigList(List<TopicTagPair> topicConfigList) {
        this.topicConfigList = topicConfigList;
    }
}
