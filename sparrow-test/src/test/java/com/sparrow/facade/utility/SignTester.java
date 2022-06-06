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

package com.sparrow.facade.utility;

import com.sparrow.cryptogram.MessageSignature;
import com.sparrow.utility.QueryStringParser;
import com.sparrow.utility.StringUtility;

import java.util.Map;
import java.util.TreeMap;

/**
 * SignTester
 *
 * @author harry
 */
public class SignTester {
    public static void main(String[] args) {
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", "wxa3847feeb20c8d59");
        parameters.put("mch_id", "1246154201");
        parameters.put("device_info", "WEB");
        parameters.put("nonce_str", "f03pafc2eszizde7llvts8fydfqqet1m");
        parameters.put("body", "智利百内国家公园W线徒步之旅");
        parameters.put("detail", "智利百内国家公园W线徒步之旅");

        parameters.put("total_fee", "1");
        parameters.put("spbill_create_ip", "127.0.0.1");
        parameters.put("notify_url", "http://in-www.joinaturelife.com/wechat/notify.do");
        parameters.put("trade_type", "NATIVE");
        parameters.put("product_id", "1");
        parameters.put("out_trade_no", "JNL-93-1-20160125133108");
        String pairParameters =QueryStringParser.serial(parameters, false);
        String sign = getSignature("appid=wxa3847feeb20c8d59&body=智利百内国家公园W线徒步之旅&detail=" +
            "智利百内国家公园W线徒步之旅&device_info=WEB&mch_id=1246154201&nonce_str=dweivsgo" +
            "e9r31f9v0lpf6e3jkn3t5dmp&notify_url=http://in-www.joinaturelife.com/wechat/notif" +
            "y.do&out_trade_no=JNL-93-1-20160125133108&product_id=1&spbill_create_ip=127.0.0." +
            "1&total_fee=1&trade_type=NATIVE");
        System.out.print(sign);
    }

    public static String getSignature(String parameters) {
        return MessageSignature.getInstance().md5(parameters + "&key=" + "774b4516222cef2447e27238585d30da");
    }
}
