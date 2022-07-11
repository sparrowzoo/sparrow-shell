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

import com.sparrow.protocol.constant.magic.LETTER;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.StringUtility;

public class HyperLink extends AbstractJWebBodyControl {

    private String href;
    private String target;

    @Override
    public String setTagNameAndGetTagAttributes() {
        super.setTagName(String.valueOf(LETTER.A_LOWER));
        return this.getHref() +
            this.getTarget();
    }

    public String getHref() {
        Object requestHref = this.pageContext.getRequest().getAttribute(
            this.getId() + ".href");
        if (requestHref != null) {
            return String.format(" href=\"%1$s\"", requestHref);
        }
        if (this.href != null && !Symbol.EMPTY.equals(this.href.trim())) {
            return String.format(" href=\"%1$s\"", this.href);
        }
        return Symbol.EMPTY;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        Object requestTarget = this.pageContext.getRequest().getAttribute(
            this.getId() + ".target");
        if (requestTarget != null) {
            return String.format(" href=\"%1$s\"", requestTarget);
        }
        if (!StringUtility.isNullOrEmpty(this.target)) {
            return String.format(" target=\"%1$s\" ", this.href);
        }
        return Symbol.EMPTY;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
