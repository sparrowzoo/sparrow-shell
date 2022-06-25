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

package com.sparrow.mvc.ui;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.EnumUtility;
import com.sparrow.utility.StringUtility;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * value:name,value2:name2
 */
public class DropDownList extends WebControl {
    private static final long serialVersionUID = 8678128692032102460L;
    Logger logger = LoggerFactory.getLogger(DropDownList.class);
    /**
     * 只读属性
     */
    private String readonly;
    /**
     * 是否可用
     */
    private String disabled;
    /**
     * 多选
     */

    private String multiple;
    /**
     * key value
     */
    private String keyValues;
    /**
     * 选中的key
     */
    private String selectKey;
    /**
     * 枚举
     */
    private String enums;

    public String getSelectKey() {
        return selectKey;
    }

    public void setSelectKey(String selectKey) {
        this.selectKey = selectKey;
    }

    @SuppressWarnings("unchecked")
    public String getItemList() {
        // 后台可以设置 OPTION
        Map<String, String> listItem = (Map<String, String>) super.pageContext
            .getRequest().getAttribute(super.getId() + ".options");
        // 如果为空则获取前台的设置值0:女,1:男,2:保密
        if (listItem == null) {
            listItem = new LinkedHashMap<String, String>();
            String[] options;
            String[] option;
            if (!StringUtility.isNullOrEmpty(this.getKeyValues())) {
                options = this.getKeyValues().split(",");
                for (String o : options) {
                    option = o.split(":");
                    listItem.put(option[0], option[1]);
                }
            }

            if (listItem.size() == 0
                && !StringUtility.isNullOrEmpty(this.getEnums())) {
                listItem = EnumUtility.getMap(this.getEnums());
            }
        }
        Set<String> keySet = listItem.keySet();
        Iterator<String> it = keySet.iterator();
        String listKey;
        Object selectValue = super.pageContext.getRequest().getAttribute(
            this.getCtrlName());
        if (StringUtility.isNullOrEmpty(selectValue)) {
            selectValue = super.pageContext.getRequest().getParameter(
                this.getCtrlName());
        }
        if (StringUtility.isNullOrEmpty(selectValue)) {
            selectValue = this.getSelectKey();
        }
        StringBuilder options = new StringBuilder();
        while (it.hasNext()) {
            listKey = it.next();
            if (listKey.equals(selectValue)) {
                options.append(String
                    .format("\n<option selected=\"selected\" value=\"%1$s\">%2$s</option>",
                        listKey, listItem.get(listKey)));
            } else {
                options.append(String.format(
                    "\n<option value=\"%1$s\">%2$s</option>", listKey,
                    listItem.get(listKey)));
            }
        }
        return options.toString();
    }

    public String getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(String keyValues) {
        this.keyValues = keyValues;
    }

    public String getReadonly() {
        Object requestReadonly = this.pageContext.getRequest().getAttribute(
            this.getId() + ".readonly");
        if (requestReadonly != null) {
            if (!requestReadonly.toString().toLowerCase().trim().equalsIgnoreCase(Boolean.FALSE.toString())) {
                return " readonly= \"readonly\" ";
            }
            return Symbol.EMPTY;
        }
        if (this.readonly != null && this.readonly.trim().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return " readonly= \"readonly\" ";
        }
        return Symbol.EMPTY;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getDisabled() {
        Object requestDisabled = this.pageContext.getRequest().getAttribute(
            this.getId() + ".disabled");
        if (requestDisabled != null) {
            if (!requestDisabled.toString().toLowerCase().trim().equalsIgnoreCase(Boolean.FALSE.toString())) {
                return " disabled= \"disabled\" ";
            }
            return Symbol.EMPTY;
        }
        if (this.disabled != null && this.disabled.trim().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return " disabled= \"disabled\" ";
        }
        return Symbol.EMPTY;

    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getMultiple() {
        if (this.multiple != null) {
            return "multiple=\"" + multiple + "\"";
        }
        return Symbol.EMPTY;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    @Override
    public int doEndTag() throws JspException {
        if (this.getVisible().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return super.doEndTag();
        }
        try {
            this.pageContext.getOut().print("\n</select>");
        } catch (IOException e) {
            logger.error("dropdown list out error", e);
        }
        return super.doEndTag();
    }

    public String getEnums() {
        return enums;
    }

    public void setEnums(String enums) {
        this.enums = enums;
        this.setNameOfEnum(enums);
    }

    @SuppressWarnings("static-access")
    @Override
    public int doStartTag() throws JspException {
        if (this.getVisible().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return SKIP_BODY;
        }
        StringBuilder writeHTML = new StringBuilder();
        writeHTML.append(String.format("<%1$s id=\"%2$s\"", "select",
            this.getId()));
        writeHTML.append(this.getName());
        writeHTML.append(this.getCssClass());
        writeHTML.append(this.getCssText());
        writeHTML.append(this.getTitle());
        writeHTML.append(this.getReadonly());
        writeHTML.append(this.getDisabled());
        writeHTML.append(this.getMultiple());
        writeHTML.append(">");
        writeHTML.append(this.getItemList());
        try {
            this.pageContext.getOut().print(writeHTML.toString());
        } catch (IOException e) {
            logger.error("dropdown list error", e);
        }
        return SKIP_BODY;
    }
}
