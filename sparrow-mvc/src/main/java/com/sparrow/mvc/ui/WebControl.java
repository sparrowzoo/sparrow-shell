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

import com.sparrow.cg.MethodAccessor;
import com.sparrow.cg.PropertyNamer;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.StringUtility;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebControl extends TagSupport {
    protected static Logger logger = LoggerFactory.getLogger(WebControl.class);
    private String id;
    private String name;
    private String pojo;
    protected String cssClass = Symbol.EMPTY;
    private String cssText = Symbol.EMPTY;
    private String events = Symbol.EMPTY;
    private String title = Symbol.EMPTY;
    private String tagName;
    private String visible = Boolean.TRUE.toString();
    private boolean isNameOfEnum;

    public String getPojo() {
        return pojo;
    }

    public void setPojo(String pojo) {
        this.pojo = pojo;
    }

    public void setNameOfEnum(String enums) {
        String enumsArray[] = enums.split(":");
        if (enumsArray.length >= 3) {
            this.isNameOfEnum = Boolean.valueOf(enumsArray[2]);
        } else {
            this.isNameOfEnum = false;
        }
    }

    public boolean isInput() {
        return true;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCtrlName() {
        return this.name == null ? Symbol.EMPTY : this.name;
    }

    public String getName() {
        return !StringUtility.isNullOrEmpty(this.name) ? String.format(" name=\"%1$s\" ", this.name) : Symbol.EMPTY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssClass() {
        String format = " class=\"%1$s\" ";
        Object css = this.pageContext.getRequest().getAttribute(
            this.getId() + ".cssClass");
        if (css != null) {
            return String.format(format, css);
        }
        if (this.cssClass != null) {
            return String.format(format, this.cssClass);
        }

        return Symbol.EMPTY;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssText() {
        String format = " style=\"%1$s\" ";
        Object requestCssText = this.pageContext.getRequest().getAttribute(
            this.getId() + ".cssText");
        if (requestCssText != null) {
            return String.format(format,
                requestCssText.toString());

        }
        if (!StringUtility.isNullOrEmpty(this.cssText)) {
            return String.format(format, this.cssText);
        }
        return Symbol.EMPTY;
    }

    public void setCssText(String cssText) {
        this.cssText = cssText;
    }

    public String getEvents() {
        Object requestEvents = this.pageContext.getRequest().getAttribute(
            this.getId() + ".events");
        if (requestEvents != null) {
            return requestEvents.toString();
        }
        if (!StringUtility.isNullOrEmpty(this.events)) {
            return this.events.trim().replaceAll("\\s", Symbol.EMPTY);
        }
        return Symbol.EMPTY;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getTitle() {
        Object requestTitle = this.pageContext.getRequest().getAttribute(
            this.getId() + ".title");
        if (requestTitle != null) {
            return String.format(" title=\"%1$s\" ", requestTitle.toString());
        }
        if (!StringUtility.isNullOrEmpty(this.title)) {
            return String.format(" title=\"%1$s\" ", this.title);
        }
        return Symbol.EMPTY;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagName() {
        return this.tagName == null ? "input" : tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getVisible() {
        String result = this.visible;
        Object requestVisible = this.pageContext.getRequest().getAttribute(
            this.getId() + ".visible");

        if (requestVisible != null) {
            result = requestVisible.toString();
        }
        return result;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getRequestValue() {
        String name = this.getCtrlName();
        Object requestValue = this.pageContext.getRequest().getAttribute(name);
        if (requestValue != null) {
            return requestValue.toString();
        }
        requestValue = HttpContext.getContext().get(name);
        if (requestValue != null) {
            return requestValue.toString();
        }
        if (this.pojo != null) {
            requestValue = this.pageContext.getRequest().getAttribute(
                this.pojo);

            if (requestValue == null) {
                requestValue = HttpContext.getContext().get(this.pojo);
            }

            if (requestValue != null) {
                try {
                    MethodAccessor methodAccessor = ApplicationContext.getContainer()
                        .getProxyBean(
                            requestValue.getClass());
                    String getMethodName = PropertyNamer.getter(this.getCtrlName());
                    requestValue = methodAccessor.get(requestValue,
                        getMethodName);
                    if (requestValue != null) {
                        if (requestValue instanceof Enum) {
                            Enum en = (Enum) requestValue;
                            return this.isNameOfEnum ? en.name() : en.ordinal() + "";
                        }
                        return requestValue.toString();
                    }
                } catch (Exception e) {
                    logger.error(this.getTagName(), e);
                    return null;
                }
            }
        }
        // 如果model.name形式还没有，则获取请求参数，原样返回
        requestValue = this.pageContext.getRequest().getParameter(name);
        if (requestValue != null) {
            return requestValue.toString();
        }
        return null;
    }

    public void drawTable(StringBuilder writeHTML) {
        writeHTML.append("<table class='pure-table'");
        writeHTML.append(String.format(" id=\"%1$s\"", this.getId()));
        writeHTML.append(this.getCssClass());
        writeHTML.append(this.getCssText());
        writeHTML.append(this.getTitle());
        writeHTML.append(this.getEvents());
        writeHTML.append(">");
    }
}
