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
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class StyleLink extends TagSupport {
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        StringBuilder writeHTML = new StringBuilder();
        writeHTML.append("<link rel=\"stylesheet\" type=\"text/css\"  href=\"");
        String href = this.getHref();
        if (href.contains("$resource")) {
            href = href.replace("$resource",
                    ConfigUtility.getValue(Config.RESOURCE));
        }
        if (href.contains("$rootPath")) {
            href = href.replace("$rootPath",
                    ConfigUtility.getValue(Config.ROOT_PATH));
        }

        if (href.contains("$website")) {
            href = href.replace("$website",
                    ConfigUtility.getValue(Config.WEBSITE));
        }
        writeHTML.append(href);
        if (href.contains("?")) {
            writeHTML.append("&");
        } else {
            writeHTML.append("?");
        }
        writeHTML.append("v=" + ConfigUtility.getValue(Config.RESOURCE_VERSION, "1.0")
                + "\"");
        try {
            if (!StringUtility.isNullOrEmpty(writeHTML)) {
                this.pageContext.getOut().print(writeHTML + "/>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
