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
import com.sparrow.cryptogram.Base64;
import com.sparrow.cryptogram.Hmac;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static com.sparrow.cryptogram.Base64.PREFERRED_ENCODING;

public abstract class AbstractAuthenticatorService implements Authenticator {
    static Logger logger = LoggerFactory.getLogger(AbstractAuthenticatorService.class);

    protected abstract String getSecret(Long userId);

    @Override
    public LoginToken authenticate(String permission, String deviceId) {
        logger.debug("permission {},deviceId {}", permission, deviceId);
        LoginToken login = new LoginToken();
        login.setUserId(User.VISITOR_ID);
        login.setUserName(ConfigUtility.getLanguageValue(
            ConfigKeyLanguage.USER_VISITOR,
            ConfigUtility.getValue(Config.LANGUAGE)));
        login.setAvatar(ConfigUtility.getValue(Config.DEFAULT_AVATAR));
        if (StringUtility.isNullOrEmpty(permission)) {
            logger.info("permission is null");
            return login;
        }

        try {
            if (!permission.contains(".")) {
                logger.debug("permission {} don't contains [.]", permission);
                return login;
            }
            String[] tokens = permission.split("\\.");
            String userInfo = tokens[0];
            String signature = tokens[1];

            userInfo = new String(Base64.decode(userInfo), PREFERRED_ENCODING);
            String[] userInfoArray = userInfo.split("&");
            String dev = userInfoArray[6].substring("deviceId=".length());
            //设备不一致
            if (!dev.equals(deviceId) && !Constant.LOCALHOST_IP.equals(deviceId)) {
                logger.debug("device can't match sign's device [{}] request device [{}] ", dev, deviceId);
                return login;
            }

            String expireAtStr = userInfoArray[3].substring("expireAt=".length());
            long expireAt = 0L;
            //过期
            if (!StringUtility.isNullOrEmpty(expireAtStr) && !"null".equalsIgnoreCase(expireAtStr)) {
                expireAt = Long.parseLong(expireAtStr);
                if (System.currentTimeMillis() > expireAt) {
                    logger.warn("sign is expire at {}", expireAt);
                    return login;
                }
            }

            Long userId = Long.parseLong(userInfoArray[0].substring("id=".length()));
            String newSignature = Hmac.getInstance().getSHA1Base64(userInfo,
                this.getSecret(userId));

            //签名不一致
            if (!signature.equals(newSignature)) {
                logger.warn("sign is not match {} vs new:{}", signature, newSignature);
                return login;
            }
            //id=%1$s&name=%2$s&login=%3$s&expireAt=%4$s&cent=%5$s&avatar=%6$s&deviceId=%7$s&activate=%8$s

            login.setUserId(userId);
            login.setUserName(userInfoArray[1].substring("name="
                .length()));
            login.setNickName(userInfoArray[2].substring("login=".length()));

            login.setCent(Long.valueOf(userInfoArray[4].substring("cent=".length())));
            login.setAvatar(userInfoArray[5].substring("avatar="
                .length()));
            login.setDeviceId(dev);
            login.setExpireAt(expireAt);
            String activate = userInfoArray[7].substring("activate="
                .length());
            if (!StringUtility.isNullOrEmpty(activate)) {
                login.setActivate(Boolean.valueOf(activate));
            }
            String days = userInfoArray[8].substring("days="
                .length());
            if (!StringUtility.isNullOrEmpty(days)) {
                login.setDays(Integer.valueOf(days));
            }
            return login;
        } catch (Exception ignore) {
            logger.error("parser error ", ignore);
            return login;
        }
    }

    public String sign(LoginToken login, String secret) {
        String userInfo = String.format(
            "id=%1$s&name=%2$s&login=%3$s&expireAt=%4$s&cent=%5$s&avatar=%6$s&deviceId=%7$s&activate=%8$s&days=%9$s",
            login.getUserId(),
            login.getUserName(),
            login.getNickName(),
            login.getExpireAt(),
            login.getCent(),
            login.getAvatar(),
            login.getDeviceId(),
            login.getActivate(),
            login.getDays());
        String signature = Hmac.getInstance().getSHA1Base64(userInfo,
            secret);
        try {
            return Base64.encodeBytes(userInfo.getBytes(PREFERRED_ENCODING)) + "." + signature;
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }
}
