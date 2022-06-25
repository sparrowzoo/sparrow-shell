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

import com.sparrow.cg.MethodAccessor;
import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.core.StrategyFactory;
import com.sparrow.protocol.POJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CellAttribute {
    private static Logger logger = LoggerFactory.getLogger(CellAttribute.class);
    private String[] fieldAttributeArray;
    private String width = "auto";
    private String align = "left";

    /**
     * sparrow.js中的$用Sparrow 字符代表 [0]带有属性的字段信息 [1]宽度:px [2]:align对齐方式
     *
     * @param config
     */
    public CellAttribute(String config) {
        String[] array = config.split("\\$");
        this.fieldAttributeArray = array[DIGIT.ZERO].split("\\+");
        if (array.length > DIGIT.ONE) {
            this.width = array[DIGIT.ONE];
        }
        if (array.length > DIGIT.TOW) {
            this.align = array[DIGIT.TOW];
        }
    }

    public String getWidth() {
        return width;
    }

    public String getAlign() {
        return align;
    }

    public String toString(POJO entity, String indent, MethodAccessor methodAccessor) {
        StringBuilder htmlLabel = new StringBuilder();
        for (String fieldAttribute : this.fieldAttributeArray) {
            List<String> valuesList = new ArrayList<String>();
            String[] array = fieldAttribute.split("#");
            try {
                FieldParser parser = StrategyFactory.getInstance().get(FieldParser.class, array[0].trim().toLowerCase());
                String[] fieldArray = array[DIGIT.ONE].split(Symbol.AND);
                for (String field : fieldArray) {
                    Object value = methodAccessor.get(entity, field);
                    if (value != null) {
                        valuesList.add(value.toString());
                    } else {
                        valuesList.add(Symbol.EMPTY);
                    }
                }
                htmlLabel.append(parser.parse(array, valuesList));
            } catch (Exception ignore) {
                logger.error("parse error", ignore);
            }
        }
        return String.format("%4$s<td width=\"%2$s\" align=\"%3$s\">%1$s</td>\r\n",
            htmlLabel.toString(), this.getWidth(), this.getAlign(), indent);
    }

    /**
     * 解释 field的配置
     *
     * @param fieldConfig
     * @return
     */
    public static List<CellAttribute> parse(String fieldConfig) {
        List<CellAttribute> cellAttributeList = new ArrayList<CellAttribute>();
        String[] fieldArray = fieldConfig.split("\\|");
        for (String config : fieldArray) {
            cellAttributeList.add(new CellAttribute(config));
        }
        return cellAttributeList;
    }
}
