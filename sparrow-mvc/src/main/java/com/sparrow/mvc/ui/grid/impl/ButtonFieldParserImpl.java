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

import com.sparrow.mvc.ui.grid.FieldParser;
import com.sparrow.mvc.ui.grid.attribute.ButtonAttribute;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.util.List;

public class ButtonFieldParserImpl implements FieldParser {
    @Override
    public String parse(String[] config, List<String> valueList) {
        ButtonAttribute buttonAttribute = new ButtonAttribute(config);
        String title = buttonAttribute.getTitle(valueList);
        String click = buttonAttribute.getClick();
        for (int i = 0; i < valueList.size(); i++) {
            click = click.replace("{" + i + "}", valueList.get(i));
        }
        if (!StringUtility.isNullOrEmpty(valueList.get(0))) {
            return String.format("<input type=\"button\" onclick=\"%1$s\" title=\"%2$s\" value=\"%2$s\" class=\"%3$s\"/>",
                click, ConfigUtility.getLanguageValue(title), buttonAttribute.getCss());
        } else {
            return "";
        }
    }
}
