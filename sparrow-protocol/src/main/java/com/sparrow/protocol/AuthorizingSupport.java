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

package com.sparrow.protocol;

/**
 * 谁证授权接口
 */
public interface AuthorizingSupport {
    /**
     * 签名
     *
     * @param login  login token
     * @param secret password
     * @return
     */
    String sign(LoginToken login, String secret);

    /**
     * 认证
     *
     * @param token
     * @return
     */
    LoginToken authenticate(String token, String deviceId);

    /**
     * 授权某资源
     *
     * @param user     当前用户
     * @param resource 请求的资源(标识)
     * @param code     当前资源（当前资源下的编码 比如forum code）
     * @return
     * @throws BusinessException
     */
    boolean isAuthorized(LoginToken user,
        String resource, String code) throws BusinessException;
}
