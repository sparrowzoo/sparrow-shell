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
package com.sparrow.user.domain.object.value;

import com.sparrow.constant.Config;
import com.sparrow.constant.SparrowError;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.user.domain.entity.SecurityPrincipalEntity;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.DateTimeUtility;
import java.sql.Timestamp;

public class FindPasswordOriginToken {

    public static FindPasswordOriginToken createToken(SecurityPrincipalEntity securityPrincipal, String currentDate) {
        FindPasswordOriginToken passwordOriginToken = new FindPasswordOriginToken();
        passwordOriginToken.userId = securityPrincipal.getUserId();
        passwordOriginToken.userName = securityPrincipal.getUserName();
        passwordOriginToken.sendDate = currentDate;
        return passwordOriginToken;
    }

    public static FindPasswordOriginToken parse(String originToken) {
        String[] array = originToken.split("\\|");
        FindPasswordOriginToken findPasswordOriginToken = new FindPasswordOriginToken();
        findPasswordOriginToken.userId = Long.parseLong(array[0]);
        findPasswordOriginToken.userName = array[1];
        findPasswordOriginToken.sendDate = array[2];
        return findPasswordOriginToken;
    }

    private Long userId;
    private String userName;
    private String sendDate;

    public String generateOriginToken() {
        // 令牌原码
        return this.userId + "|" + this.userName + "|" + this.sendDate;
    }

    public boolean isValid(String originUserName) throws BusinessException {
        Asserts.isTrue(!this.userName.equals(originUserName), SparrowError.USER_PASSWORD_VALIDATE_TOKEN_ERROR);
        int validateTokenAvailableDay = Integer.valueOf(ConfigUtility
            .getValue(Config.VALIDATE_TOKEN_AVAILABLE_DAY));
        int passDay = DateTimeUtility.getInterval(
            Timestamp.valueOf(sendDate).getTime(),
            System.currentTimeMillis(),
            DateTimeUnit.DAY);
        if (passDay > validateTokenAvailableDay) {
            throw new BusinessException(SparrowError.USER_VALIDATE_TOKEN_TIME_OUT);
        }
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getSendDate() {
        return sendDate;
    }
}
