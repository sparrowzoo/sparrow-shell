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
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAuthenticatorService implements Authenticator {
    static Logger logger = LoggerFactory.getLogger(AbstractAuthenticatorService.class);

    protected abstract String getEncryptKey();

    protected abstract String getDecryptKey();

    protected abstract boolean validateDeviceId();

    /**
     * 比如IM系统，客户认识，我们鉴权，sparrow系统无法用户状态
     *
     * @return
     */
    protected abstract boolean validateStatus();


    protected abstract String sign(LoginUser loginUser, String secretKey);

    protected abstract LoginUser verify(String token, String secretKey) throws BusinessException;

    protected abstract void setUserStatus(Long loginUser, LoginUserStatus loginUserStatus);

    /**
     * 存储挂掉以token的过期时间为准,或者状态被删除，更新缓存方案
     * cache aside 用户状态和过期时间分成两个key保存
     * https://sparrowzoo.feishu.cn/docx/doxcnBm4bqwM7gNWiScATMgWHng
     * <p>
     * 用户状态不存在 从数据库中获取用户状态
     * loginUserStatus = this.getUserStatusFromDB(loginUser.getUserId());
     * <p>
     * this.setUserStatus(loginUser, loginUserStatus);
     *
     * @param userId
     * @return
     */
    protected abstract LoginUserStatus getUserStatus(Long userId);

    protected abstract LoginUserStatus getUserStatusFromDB(Long userId);

    protected abstract void renewal(Long userId, LoginUserStatus loginUserStatus);

    @Override
    public LoginUser authenticate(String token, String deviceId) throws BusinessException {
        logger.debug("token {},deviceId {}", token, deviceId);
        Asserts.isTrue(StringUtility.isNullOrEmpty(token), SparrowError.USER_LOGIN_TOKEN_NOT_FOUND);
        LoginUser loginUser = this.verify(token, this.getDecryptKey());
        //设备指纹验证失败 说明token被盗
        if (this.validateDeviceId() && !loginUser.getDeviceId().equals(deviceId) && !Constant.LOCALHOST_IP.equals(deviceId)) {
            logger.error("device can't match sign's device [{}] request device [{}],user-id {} ", loginUser.getDeviceId(), deviceId, loginUser.getUserId());
            throw new BusinessException(SparrowError.USER_DEVICE_NOT_MATCH);
        }

        LoginUserStatus loginUserStatus = this.getUserStatus(loginUser.getUserId());
        //获取当前用户的过期时间
        long expireAt = loginUser.getExpireAt();
        if (this.validateStatus()) {
            Asserts.isTrue(loginUserStatus == null, SparrowError.USER_TOKEN_ABNORMAL);
            Asserts.isTrue(!LoginUserStatus.STATUS_NORMAL.equals(loginUserStatus.getStatus()), SparrowError.USER_TOKEN_ABNORMAL);
            //如果用户已经在服务器存在，则拿更新过的时期时间
            if (loginUserStatus.getExpireAt() != null) {
                //用户状态存在 说明token 存储正常，以用户状态的过期时间为准
                expireAt = loginUserStatus.getExpireAt();
            }
        }
        //过期
        Asserts.isTrue(expireAt < System.currentTimeMillis(), SparrowError.USER_TOKEN_EXPIRED);
        if (this.validateStatus() && loginUserStatus != null) {
            this.renewal(loginUser.getUserId(), loginUserStatus);
        }
        return loginUser;
    }

    private LoginUser getVisitor(String deviceId) {
        String visitorName = ConfigUtility.getLanguageValue(ConfigKeyLanguage.USER_VISITOR_NAME,
                ConfigUtility.getValue(Config.LANGUAGE));
        String visitorNickName = ConfigUtility.getLanguageValue(ConfigKeyLanguage.USER_VISITOR_NICKNAME,
                ConfigUtility.getValue(Config.LANGUAGE));
        String avatar = ConfigUtility.getValue(Config.DEFAULT_AVATAR);
        return LoginUser.create(LoginUser.VISITOR_ID,
                LoginUser.CATEGORY_VISITOR
                , visitorName
                , visitorNickName
                , avatar, deviceId, 0);
    }

    public String sign(LoginUser loginUser, LoginUserStatus loginUserStatus) {
        String token = this.sign(loginUser, this.getEncryptKey());
        this.setUserStatus(loginUser.getUserId(), loginUserStatus);
        return token;
    }
}
