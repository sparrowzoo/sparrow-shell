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

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.enums.Language;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

public class ErrorMessage {

    private static ErrorMessage errorSupport = new ErrorMessage();

    public static ErrorMessage getInstance() {
        return errorSupport;
    }

    private ErrorMessage() {
    }

    public String getMessage(String key, String defaultMessage) {
        return getMessage(key, defaultMessage, null);
    }

    public String getMessage(String key, String defaultMessage, Language language) {
        if (language == null) {
            language = Language.ZH_CN;
        }
        String message = ConfigUtility.getLanguageValue(key, language.toString());
        if (!StringUtility.isNullOrEmpty(message)) {
            return message;
        }

        if (key.contains(Symbol.DOT)) {
            key = key.split("\\.")[0];
        }
        message = ConfigUtility.getLanguageValue(key, language.toString().toLowerCase());
        if (!StringUtility.isNullOrEmpty(message)) {
            return message;
        }
        return defaultMessage;
    }
}
