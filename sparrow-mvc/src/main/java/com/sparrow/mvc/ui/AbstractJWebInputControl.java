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
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <input type="text" id="" value="" name="" maxlength="3" disabled="disabled" readonly="readonly" visible="false" />
 */
public abstract class AbstractJWebInputControl extends WebControl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 6411644320375966060L;
    private String readonly;
    private String disabled;
    private String maxLength;
    protected String value;
    private String type;
    private String tabIndex;
    private String placeHolder;
    private String autocomplete;

    public String getTabIndex() {
        Integer tabIndex = 0;
        Object requestTabIndex = this.pageContext.getRequest().getAttribute(
            this.getId() + ".tabIndex");
        if (requestTabIndex != null) {
            tabIndex = Integer.valueOf(requestTabIndex.toString());
        } else if (this.tabIndex != null) {
            tabIndex = Integer.valueOf(this.tabIndex);
        }
        return tabIndex > 0 ? String.format(" tabindex=\"%1$s\" ", tabIndex) : Symbol.EMPTY;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * 设置tag name 和get tag 属性
     *
     * @return
     */
    public abstract String setTagNameAndGetTagAttributes();

    @Override
    public int doStartTag() throws JspException {
        if (this.getVisible().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return TagSupport.SKIP_BODY;
        }
        StringBuilder writeHTML = new StringBuilder();
        String tagAttributes = this.setTagNameAndGetTagAttributes();
        writeHTML.append(String.format("<%1$s type=\"%2$s\" id=\"%3$s\"",
            this.getTagName(), this.getType(), this.getId()));
        writeHTML.append(tagAttributes);
        if (!StringUtility.isNullOrEmpty(this.getName())) {
            writeHTML.append(this.getName());
        }
        writeHTML.append(this.getTabIndex());
        writeHTML.append(this.getCssClass());
        writeHTML.append(this.getCssText());
        writeHTML.append(this.getTitle());
        writeHTML.append(this.getEvents());
        writeHTML.append(this.getDisabled());
        writeHTML.append(this.getReadonly());
        writeHTML.append(this.getMaxLength());
        writeHTML.append(this.getValue());
        writeHTML.append(this.getPlaceHolder());
        writeHTML.append(this.getAutocomplete());
        writeHTML.append("/>");
        try {
            this.pageContext.getOut().print(writeHTML.toString());
        } catch (IOException e) {
            logger.error("input control error", e);
        }
        return TagSupport.SKIP_BODY;
    }

    public String getReadonly() {
        String readonly = Symbol.EMPTY;
        Object requestReadonly = this.pageContext.getRequest().getAttribute(
            this.getId() + ".readonly");

        if (requestReadonly != null) {
            readonly = requestReadonly.toString();
        } else if (this.readonly != null) {
            readonly = this.readonly;
        }
        boolean isReadonly = Boolean.TRUE.toString().equalsIgnoreCase(readonly.trim()) || "readonly".equalsIgnoreCase(readonly.trim());
        return isReadonly ? " readonly=\"readonly\" " : Symbol.EMPTY;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getDisabled() {

        String disabled = Symbol.EMPTY;
        Object requestDisabled = this.pageContext.getRequest().getAttribute(
            this.getId() + ".disabled");
        if (requestDisabled != null) {
            disabled = requestDisabled.toString();
        } else if (this.disabled != null) {
            disabled = this.disabled;
        }

        return disabled.trim().equalsIgnoreCase(Boolean.TRUE.toString()) ? " disabled=\"disabled\" " : Symbol.EMPTY;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getMaxLength() {
        String maxLength = Symbol.EMPTY;
        Object requestMaxLength = this.pageContext.getRequest().getAttribute(
            this.getId() + ".maxLength");
        if (requestMaxLength != null) {
            maxLength = requestMaxLength.toString();
        } else if (this.maxLength != null) {
            maxLength = this.maxLength;
        }

        return Symbol.EMPTY.equals(maxLength) ? Symbol.EMPTY : String.format(" maxlength=\"%1$s\" ", maxLength);
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * 获取设置input控件的value 如果控件的类别为checkbox或radiobox则该value不可被手动更改
     *
     * @return
     */
    public String getValue() {
        if ("checkbox".equalsIgnoreCase(this.getType()) || "radiobox".equalsIgnoreCase(this.getType())) {
            return String.format(" value=\"%1$s\"", this.value);
        }
        // 先获取id.value的形式，这种形式优先级最高
        Object requestValue = this.pageContext.getRequest().getAttribute(
            this.getId() + ".value");
        if (requestValue == null) {
            requestValue = super.getRequestValue();
        }

        if (requestValue == null) {
            requestValue = this.value;
        }

        if (requestValue == null) {
            return Symbol.EMPTY;
        }
        requestValue = requestValue.toString().replace("\"", "\\\"");
        return String.format(" value=\"%1$s\"", requestValue);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPlaceHolder() {
        if (StringUtility.isNullOrEmpty(this.placeHolder)) {
            return Symbol.EMPTY;
        }
        return " placeholder=\"" + this.placeHolder + "\" ";
    }

    public String getAutocomplete() {
        if (StringUtility.isNullOrEmpty(this.autocomplete)) {
            return Symbol.EMPTY;
        }
        return " autocomplete=\"off\" ";
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }
}
