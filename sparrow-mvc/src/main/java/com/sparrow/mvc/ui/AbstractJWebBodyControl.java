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

import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.HtmlUtility;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public abstract class AbstractJWebBodyControl extends WebControl {
    /**
     *
     */
    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    /**
     * 设置 tag name和tag 属性
     *
     * @return
     */
    public abstract String setTagNameAndGetTagAttributes();

    @Override
    public int doStartTag() throws JspException {

        if (this.getVisible().equalsIgnoreCase(Boolean.FALSE.toString())) {
            return TagSupport.SKIP_BODY;
        }

        int returnValue = TagSupport.SKIP_BODY;
        StringBuilder writeHTML = new StringBuilder();

        String htmlId = this.getId() + ".innerHTML";
        Object innerHTML = this.pageContext.getRequest().getAttribute(
            htmlId);
        if (innerHTML == null) {
            innerHTML = HttpContext.getContext().get(htmlId);
        }
        if (innerHTML == null) {
            innerHTML = this.pageContext.getRequest().getAttribute(
                this.getId() + ".value");
        }
        if (innerHTML == null) {
            innerHTML = super.getRequestValue();
        }

        String tagAttributes = this.setTagNameAndGetTagAttributes();
        writeHTML.append(String.format("<%1$s id=\"%2$s\"", this
            .getTagName(), this.getId()));
        writeHTML.append(tagAttributes);
        writeHTML.append(this.getName());
        writeHTML.append(this.getCssClass());
        writeHTML.append(this.getCssText());
        writeHTML.append(this.getTitle());
        writeHTML.append(this.getEvents());
        writeHTML.append(">");

        if (innerHTML == null) {
            returnValue = TagSupport.EVAL_BODY_INCLUDE;
        } else {
            writeHTML.append(innerHTML);
        }
        try {
            String content = writeHTML.toString().trim();
            if ("textarea".equalsIgnoreCase(this.getTagName())) {
                content = HtmlUtility.decode(content);
            }
            this.pageContext.getOut().print(content);
            this.pageContext.getOut().print("</" + this.getTagName() + ">");
        } catch (IOException e) {
            logger.error("body control error {}", e);
        }
        return returnValue;
    }

    @Override
    public boolean isInput() {
        return false;
    }
}
