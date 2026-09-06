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

package com.sparrow.authenticator;

/**
 * shiro 中的定义将认证授权统一管理
 * 这里将认证接口单独抽离出来管理，与授权解耦
 * AuthenticationInfo 用于认证信息
 * AuthorizationInfo 用于授权信息 sparrow 不使用，不作处理
 *
 * 该接口的核心职责即将用户的登录 token 转换为认证信息
 *
 * 为了提高性能，每次token 解析可以考虑本地缓存,具体实现可以暂时在这里实现
 */
public interface Realm {

    boolean support(AuthenticationToken token);

    /**
     * Apache shiro 源代码参考
     *
     *- 根据用户的token 获取对应的认证信息
     * protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException
     * <code>AuthenticationInfo</code> 表示与认证/登录过程相关的Subject(即用户)存储的账户信息
     * <p>
     * 理解该接口与{@link AuthenticationToken AuthenticationToken}接口的区别非常重要
     *
     * <code>AuthenticationInfo</code>实现类表示已经验证过且存储在系统中的账户数据，
     * 而<code>AuthenticationToken</code>表示提交用于登录尝试的数据(这些数据可能与已验证存储的账户<code>AuthenticationInfo</code>匹配，也可能不匹配)
     */
    AuthenticationInfo getAuthenticationInfo(AuthenticationToken token);
}
