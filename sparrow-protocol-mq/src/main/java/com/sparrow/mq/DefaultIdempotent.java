///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.sparrow.mq;
//
//import com.sparrow.protocol.constant.magic.Digit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class DefaultIdempotent implements MQIdempotent {
//    private static Logger logger = LoggerFactory.getLogger(DefaultIdempotent.class);
//
//    @Override
//    public boolean tryLock(String keys, Long expireMills) {
//        //todo 判断重试N次后报警
//        int times = 0;
//        while (true) {
//            Key consumeKey = new Key.Builder().business(IDEMPOTENT).businessId(keys).build();
//            try {
//                times++;
//                boolean successful = cacheClient.string().setIfNotExistWithMills(consumeKey, Digit.ZERO, expireMills);
//                if (successful) {
//                    return true;
//                }
//                return false;
//            } catch (CacheConnectionException e) {
//                logger.error("connection error", e);
//                if (times > 3) {
//                    throw e;
//                }
//
//                try {
//                    Thread.sleep(100 + times);
//                } catch (InterruptedException ex) {
//                    logger.error("try lock consumer lock error", e);
//                }
//            }
//        }
//    }
//
//    /**
//     * if(get(msgId)=="OK"){
//     *     return "SUCCESS"
//     * }
//     * if(setNx(msgId,5s){
//     *    dobusiness("业务保证幂等");
//     *    set(msgId,"OK",1h)
//     *    return "SUCCESS";
//     * }
//     * @param keys
//     * @return
//     */
//    @Override
//    public boolean duplicate(String keys) {
//        try {
//            Integer value = cacheClient.string().get(key, Integer.class);
//            return value != null && value.equals(Digit.ONE);
//        } catch (CacheConnectionException e) {
//            logger.error("consumable connection break ", e);
//            return false;
//        }
//    }
//
//    @Override
//    public boolean consumed(String keys,Integer seconds) {
//        try {
//            Key consumeKey = new Key.Builder().business(IDEMPOTENT).businessId(keys).build();
//            return jedis.setex(key.key(), seconds, value.toString());
//        } catch (CacheConnectionException e) {
//            logger.error("consumable connection break ", e);
//            return false;
//        }
//    }
//}
