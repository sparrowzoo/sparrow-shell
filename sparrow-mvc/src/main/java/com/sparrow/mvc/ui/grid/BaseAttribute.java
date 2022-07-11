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

package com.sparrow.mvc.ui.grid;

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.EnumUtility;
import com.sparrow.utility.StringUtility;
import java.sql.Timestamp;
import java.util.List;

/**
 * text#filedName#format
 */
public class BaseAttribute {
    protected String[] fieldName;
    protected String css;
    protected int textLength = DIGIT.ALL;
    protected String defaultValue = Symbol.HORIZON_LINE;
    protected String format;

    public BaseAttribute(String[] config) {
        this.fieldName = config[DIGIT.ONE].split(Symbol.AND);
        if (config.length > DIGIT.TOW) {
            this.format = config[DIGIT.TOW];
        }
    }

    public String[] getFieldName() {
        return fieldName;
    }

    public String getCss() {
        return css;
    }

    public int getTextLength() {
        return textLength;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getTitle(List<String> valueList) {
        String text = this.format;
        if (!StringUtility.isNullOrEmpty(text)) {
            if (text.startsWith("date:")) {
                text = text.substring(DIGIT.FIVE);
                text = DateTimeUtility.getFormatTime(Timestamp.valueOf(valueList.get(DIGIT.ZERO)), text);
            } else if (text.startsWith("enum:")) {
                text = text.substring(DIGIT.FIVE);
                text = EnumUtility.getMap(text).get(valueList.get(DIGIT.ZERO));
            } else if (valueList.size() > DIGIT.ZERO) {
                for (int i = DIGIT.ZERO; i < valueList.size(); i++) {
                    text = text.replace("{" + i + "}", valueList.get(i));
                }
            }
        } else {
            text = valueList.get(DIGIT.ZERO);
        }
        if (StringUtility.isNullOrEmpty(text)) {
            text = this.defaultValue;
        }
        return text;
    }

    public String subString(String text) {
        if (this.getTextLength() > DIGIT.ALL) {
            if (text.length() > this.getTextLength()) {
                text = StringUtility.subStringByByte(text, this.getTextLength(), "...");
            }
        }
        return text;
    }
}
