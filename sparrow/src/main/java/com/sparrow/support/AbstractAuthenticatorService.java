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

package com.sparrow.support;

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.constant.User;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAuthenticatorService implements Authenticator {
    static Logger logger = LoggerFactory.getLogger(AbstractAuthenticatorService.class);

    protected abstract String getEncryptKey();

    protected abstract String getDecryptKey();

    protected abstract String sign(LoginUser loginUser, String secretKey);

    protected abstract LoginUser verify(String token, String secretKey) throws BusinessException;

    protected abstract void setUserStatus(LoginUser loginUser, LoginUserStatus loginUserStatus);

    protected abstract LoginUserStatus getUserStatus(Long userId);

    protected abstract LoginUserStatus getUserStatusFromDB(Long userId);

    protected abstract void renewal(LoginUser loginUser, LoginUserStatus loginUserStatus);

    @Override
    public LoginUser authenticate(String token, String deviceId) throws BusinessException {
        logger.debug("token {},deviceId {}", token, deviceId);
        LoginUser visitor = this.getVisitor(deviceId);
        if (StringUtility.isNullOrEmpty(token)) {
            logger.error("permission is null");
            return visitor;
        }
        LoginUser loginUser = this.verify(token, this.getDecryptKey());
        //设备指纹验证失败 说明token被盗
        if (!loginUser.getDeviceId().equals(deviceId) && !Constant.LOCALHOST_IP.equals(deviceId)) {
            logger.error("device can't match sign's device [{}] request device [{}] ", loginUser.getDeviceId(), deviceId);
            return visitor;
        }

        LoginUserStatus loginUserStatus = this.getUserStatus(loginUser.getUserId());
        //存储挂掉以token的过期时间为准
        long expireAt;
        //用户状态不存在 说明token 存储挂掉了,兜底方案
        if (loginUserStatus == null) {
            //存储挂掉以token的过期时间为准
            expireAt = loginUser.getExpireAt();
            //用户状态不存在 从数据库中获取用户状态
            loginUserStatus = this.getUserStatusFromDB(loginUser.getUserId());
            loginUserStatus.setExpireAt(expireAt);
            this.setUserStatus(loginUser, loginUserStatus);
        } else {
            //用户状态存在 说明token 存储正常，以用户状态的过期时间为准
            expireAt = loginUserStatus.getExpireAt();
        }

        //过期
        if (expireAt < System.currentTimeMillis()) {
            logger.error("token is expired");
            return visitor;
        }

        if (!LoginUserStatus.STATUS_NORMAL.equals(loginUserStatus.getStatus())) {
            logger.error("user is disable");
            return visitor;
        }
        this.renewal(loginUser, loginUserStatus);
        return loginUser;
    }

    private LoginUser getVisitor(String deviceId) {
        String visitorName = ConfigUtility.getLanguageValue(ConfigKeyLanguage.USER_VISITOR_NAME,
            ConfigUtility.getValue(Config.LANGUAGE));
        String visitorNickName = ConfigUtility.getLanguageValue(ConfigKeyLanguage.USER_VISITOR_NICKNAME,
            ConfigUtility.getValue(Config.LANGUAGE));
        String avatar = ConfigUtility.getValue(Config.DEFAULT_AVATAR);
        return LoginUser.create(User.VISITOR_ID
            , visitorName
            , visitorNickName
            , avatar, deviceId, 0);
    }

    public String sign(LoginUser loginUser, LoginUserStatus loginUserStatus) {
        String token = this.sign(loginUser, this.getEncryptKey());
        this.setUserStatus(loginUser, loginUserStatus);
        return token;
    }
}
