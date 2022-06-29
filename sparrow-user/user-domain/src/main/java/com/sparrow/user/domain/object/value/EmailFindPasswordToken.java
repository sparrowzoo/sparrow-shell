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
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.constant.SparrowError;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.user.support.suffix.UserFieldSuffix;
import com.sparrow.utility.ConfigUtility;

public class EmailFindPasswordToken {
    private String base64Email;
    private String encryptedToken;

    public static EmailFindPasswordToken parse(String finalToken) throws BusinessException {
        String[] array = finalToken.split("\\.");
        Asserts.isTrue(array.length < 2, SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL, UserFieldSuffix.USER_PASSWORD_RESET_VALIDATE_CODE);
        return EmailFindPasswordToken.create(array[0], array[1]);
    }

    public static EmailFindPasswordToken create(String base64Email, String token) {
        EmailFindPasswordToken emailFindPasswordToken = new EmailFindPasswordToken();
        emailFindPasswordToken.base64Email = base64Email;
        emailFindPasswordToken.encryptedToken = token;
        return emailFindPasswordToken;
    }

    public String generateToken() {
        return this.base64Email + "." + this.encryptedToken;
    }

    public String getBase64Email() {
        return base64Email;
    }

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public String generateEmailContent(String currentDate) {
        String language = ConfigUtility.getValue(Config.LANGUAGE);
        return ConfigUtility
            .getLanguageValue(ConfigKeyLanguage.PASSWORD_EMAIL_CONTENT,
                language)
            .replace("$rootPath", ConfigUtility.getValue(Config.ROOT_PATH))
            .replace("$validateToken", this.generateToken())
            .replace("$date", currentDate);
    }
}
