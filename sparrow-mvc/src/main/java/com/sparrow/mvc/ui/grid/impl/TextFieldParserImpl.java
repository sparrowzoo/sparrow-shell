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

package com.sparrow.mvc.ui.grid.impl;

import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.mvc.ui.grid.FieldParser;
import com.sparrow.mvc.ui.grid.attribute.TextAttribute;
import com.sparrow.utility.StringUtility;

import java.util.List;

public class TextFieldParserImpl implements FieldParser {
    @Override
    public String parse(String[] config, List<String> valueList) {
        TextAttribute textAttribute = new TextAttribute(config);
        if (valueList == null || valueList.size() == 0) {
            return textAttribute.getDefaultValue();
        }
        String result = textAttribute.getTitle(valueList);
        if (textAttribute.getTextLength() > Digit.ALL) {
            if (result.length() > textAttribute.getTextLength()) {
                result = String.format("<span title='%1$s'>%2$s</span>", result, StringUtility.subStringByByte(result, textAttribute.getTextLength(), "..."));
            }
        }
        return result;
    }
}
