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
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class Internationalization extends TagSupport {
    private static final long serialVersionUID = -4455912995732848670L;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        try {
            String language = (String) HttpContext.getContext().get(Constant.REQUEST_LANGUAGE);
            if (StringUtility.isNullOrEmpty(language)) {
                language = ConfigUtility.getValue(Config.LANGUAGE);
            }
            this.pageContext.getOut().print(
                ConfigUtility.getLanguageValue(this.getKey(), language));
        } catch (IOException ignore) {
        }
        return returnValue;
    }
}
