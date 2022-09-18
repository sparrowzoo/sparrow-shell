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

package com.sparrow.rocketmq.spring;

import com.sparrow.cache.Key;
import com.sparrow.constant.cache.key.KeyCMS;
import com.sparrow.mq.MQPublisher;
import com.sparrow.rocketmq.protocol.event.HelloEvent;
import com.sparrow.concurrent.latch.DistributedCountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by harry on 2017/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring_rocket_mq_product.xml"})
public class PublisherTest {

    @Autowired
    private MQPublisher mqPublisher;

    @Autowired
    private DistributedCountDownLatch distributedCountDownLatch;

    @Test
    public void publish() {
        HelloEvent helloEvent = new HelloEvent();
        Key productKey=new Key.Builder().business(KeyCMS.CMS_ALL_CRAWL).businessId(1).build();
        helloEvent.setMessage("msg");
        try {
            while (true){
                mqPublisher.publish(helloEvent,productKey);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
