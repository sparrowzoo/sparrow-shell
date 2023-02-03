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

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class Frame extends TagSupport {
    private String id;
    private String name;
    private String src;
    private int frameborder = Digit.ZERO;
    private String scrolling = "no";
    private String cssText = Symbol.EMPTY;
    private String cssClass = Symbol.EMPTY;

    public String getCssText() {
        if (!StringUtility.isNullOrEmpty(this.cssText)) {
            this.cssText = String.format(" style=\"%1$s\" ", this.cssText);
        }
        return cssText;
    }

    public void setCssText(String cssText) {
        this.cssText = cssText;
    }

    public String getFrameborder() {
        return String.format(" frameborder=\"%1$s\" ", this.frameborder);
    }

    public void setFrameborder(int frameborder) {
        this.frameborder = frameborder;
    }

    public String getScrolling() {
        return String.format(" scrolling=\"%1$s\" ", this.scrolling);
    }

    public void setScrolling(String scrolling) {
        this.scrolling = scrolling;
    }

    public String getSrc() {
        if (this.src != null) {
            return String.format(" src=\"%1$s\" ", this.src.replace(
                "$rootPath", ConfigUtility
                    .getValue(Config.ROOT_PATH)));
        }
        return Symbol.EMPTY;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        StringBuilder writeHTML = new StringBuilder("<iframe");
        writeHTML.append(this.getId());
        writeHTML.append(this.getName());
        writeHTML.append(this.getCssClass());
        writeHTML.append(this.getCssText());
        writeHTML.append(this.getFrameborder());
        writeHTML.append(this.getScrolling());
        writeHTML.append(this.getSrc());
        writeHTML.append(">");
        try {
            this.pageContext.getOut().print(writeHTML + "</iframe>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public String getCssClass() {
        if (!StringUtility.isNullOrEmpty(this.cssClass)) {
            this.cssClass = String.format(" class=\"%1$s\"", this.cssClass);
        }
        return this.cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getId() {
        if (!StringUtility.isNullOrEmpty(this.id)) {
            this.id = String.format(" id=\"%1$s\"", this.id);
        }
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (!StringUtility.isNullOrEmpty(this.name)) {
            return String.format(" name=\"%1$s\"", this.name);
        }
        return Symbol.EMPTY;
    }

    public void setName(String name) {
        this.name = name;
    }
}
