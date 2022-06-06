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

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.kafka.MessageConverter;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQ_CLIENT;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class SparrowJsonMessageConverter implements MessageConverter<String> {

    private static Logger logger = LoggerFactory.getLogger(SparrowJsonMessageConverter.class);

    @Override
    public MQEvent fromMessage(ConsumerRecord<String, String> message) {
        String json = message.value();
        Iterable<Header> itb = message.headers().headers(MQ_CLIENT.CLASS_NAME);
        if (itb == null) {
            logger.error("class not defined");
            return null;
        }

        Iterator<Header> it = itb.iterator();

        if (!it.hasNext()) {
            logger.error("class not defined");
            return null;
        }
        String className = new String(it.next().value());
        try {
            return (MQEvent) JsonFactory.getProvider().parse(json, Class.forName(className));
        } catch (ClassNotFoundException e) {
            logger.error("class{} not found", className);
            return null;
        }
    }

    @Override
    public ProducerRecord<String, String> createMessage(String topic, String k, MQEvent event) {
        String jsonString = JsonFactory.getProvider().toString(event);
        ProducerRecord<String, String> message = new ProducerRecord<>(topic, k, jsonString);
        message.headers().add(MQ_CLIENT.CLASS_NAME, event.getClass().getName().getBytes());
        return message;
    }
}