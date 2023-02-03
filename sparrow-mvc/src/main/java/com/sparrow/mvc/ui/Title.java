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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class Title extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        String writeHTML = "<title>";
        String title = (String) this.pageContext.getRequest().getAttribute("title");
        if (title == null) {
            returnValue = TagSupport.EVAL_BODY_INCLUDE;
        } else {
            writeHTML += title;
        }
        try {
            this.pageContext.getOut().print(writeHTML);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            this.pageContext.getOut().print("</title>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }
}
