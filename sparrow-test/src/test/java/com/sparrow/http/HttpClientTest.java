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

package com.sparrow.http;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.HttpMethod;
import com.sparrow.jdk.hash.Obj;
import com.sparrow.json.Json;
import com.sparrow.utility.HttpClient;
import java.util.HashMap;
import java.util.Map;

public class HttpClientTest {
    public static void main(String[] args) {
        while (true) {
            Map<String, String> header = new HashMap<>();
            //提示请登录 刚刚登录获取的token
            header.put("X-Sugar-Token", "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl9tZW1iZXJfa2V5IjoiNmJkNTlhMmQtM2M0Yi00MmExLTk2OTUtODMzOWU3YzIxMDM2In0.6PIbxQsJFGklxRvnKYqzbHhNyrVWajCzQd8uN_w5vAGc1OOi8oCSCaIKboTBGdb-iK6bfNTxMIy9ZP9465o0_A");
            //token 解析正常
            //header.put("X-Sugar-Token", "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl9tZW1iZXJfa2V5IjoiY2M3OGNhMWEtNmJjMS00N2MzLTgwMjgtZjhhMTkyNDExMjU0In0.tHBHWIDNvzawfsf3-StgmbVilloS4OnkG_N772S_LqXKq5ZiRUk22cvQBmqtX2SpmuIh6MHrzikS9NucM0UrDw");
            //curl -X GET "http://kimlienapi.zhilongsoft.com/app/authMember/info" -H  "Request-Origion:SwaggerBootstrapUi" -H  "accept:*/*" -H  "X-Sugar-Token:eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl9tZW1iZXJfa2V5IjoiY2M3OGNhMWEtNmJjMS00N2MzLTgwMjgtZjhhMTkyNDExMjU0In0.tHBHWIDNvzawfsf3-StgmbVilloS4OnkG_N772S_LqXKq5ZiRUk22cvQBmqtX2SpmuIh6MHrzikS9NucM0UrDw"
            String result = HttpClient.request(HttpMethod.GET, "http://studyapi.zhilongsoft.com/app/authMember/info"
                , null, null, header, false);
            Json json = JsonFactory.getProvider();
            Map<String, Object> map = json.parse(result);
            Integer code = (Integer) map.get("code");
            if (code.equals(200)) {
                Map<String, Object> userMap = (Map<String, Object>) map.get("data");
                Map<String, Object> userProperty = (Map<String, Object>) userMap.get("member");
                Integer userId = (Integer) userProperty.get("id");
            }
            System.out.println(result);
        }
    }
}
