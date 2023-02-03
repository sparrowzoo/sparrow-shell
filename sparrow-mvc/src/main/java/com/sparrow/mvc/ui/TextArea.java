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

@SuppressWarnings("serial")
public class TextArea extends AbstractJWebBodyControl {

    private String rows;
    private String cols;
    private String readonly;
    private String disabled;
    private String tabIndex;

    public String getTabIndex() {
        Object tabIndex = this.pageContext.getRequest().getAttribute(
            this.getId() + ".tabIndex");
        if (tabIndex == null) {
            tabIndex = this.tabIndex;
        }
        return !StringUtility.isNullOrEmpty(tabIndex) ? String.format(" tabindex=\"%1$s\" ", tabIndex) : Symbol.EMPTY;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    @Override
    public boolean isInput() {
        return true;
    }

    @Override
    public String setTagNameAndGetTagAttributes() {
        super.setTagName("textarea");
        StringBuilder attributes = new StringBuilder();
        if (!StringUtility.isNullOrEmpty(this.getCols())) {
            attributes.append(String.format(" cols='%1$s' ", this.getCols()));
        }
        if (!StringUtility.isNullOrEmpty(this.getRows())) {
            attributes.append(String.format(" rows='%1$s' ", this.getRows()));
        }
        if (!StringUtility.isNullOrEmpty(this.getReadonly())) {
            attributes.append(String.format(" readonly='%1$s' ",
                this.getReadonly()));
        }
        if (!StringUtility.isNullOrEmpty(this.getDisabled())) {
            attributes.append(String.format(" disabled='%1$s' ",
                this.getDisabled()));
        }
        if (!StringUtility.isNullOrEmpty(this.getTabIndex())) {
            attributes.append(this.getTabIndex());
        }
        return attributes.toString();
    }

}
