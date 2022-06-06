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

import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.kafka.MessageListener;
import com.sparrow.mq.MQ_CLIENT;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by harry on 2017/6/14.
 */
public class RocketMQConsumer implements ContainerAware {
    private static Logger log = LoggerFactory.getLogger(RocketMQConsumer.class);

    private String instanceName;

    private String groupName;

    private MessageListener messageListener;

    private List<String> topicList;

    private String nameServerAddress;

    private Integer threadCount;


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

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public String getNameServerAddress() {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public String getInstanceName() {
        if (this.instanceName == null) {
            this.instanceName = MQ_CLIENT.INSTANCE_NAME;
        }
        return instanceName;
    }


    private volatile KafkaConsumer consumer;

    public synchronized void start() {
        try {
            consumer = createConsumer();
            consumer.subscribe(this.topicList);
            consumer.poll(1000L);

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.threadCount, this.threadCount,
                    5L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(2000),
                    new SparrowThreadFactory.Builder().namingPattern("kafka-consumer-thread-%d").daemon(true).build());


            ConsumerRecords<String, String> records = consumer.poll(1000L);

            for (TopicPartition partition : records.partitions()) {
                final List<ConsumerRecord<String, String>> recordOfPartition = records.records(partition);

                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        RocketMQConsumer.this.messageListener.consumeMessage(recordOfPartition);
                    }
                });
            }
            log.info("begin start ROCKET MQ client, group={}, nameServerAddr,config={}", groupName, nameServerAddress);
        } catch (Exception e) {
            log.error("start mq " + this.getClass().getName() + " failed：", e);
            throw new RuntimeException("failed start ROCKET MQ client, Exception: ", e);
        }
    }

    protected KafkaConsumer createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.nameServerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupName);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);

        return consumer;
    }

    public synchronized void shutdown() {
        try {
            consumer.close();
        } catch (Exception e) {
            log.error("shutdown rocket mq fail", e);
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        this.start();
    }
}
