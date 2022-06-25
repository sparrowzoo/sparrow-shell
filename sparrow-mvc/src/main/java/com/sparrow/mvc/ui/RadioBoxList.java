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

import com.sparrow.utility.EnumUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class RadioBoxList extends WebControl {
    private static Logger logger = LoggerFactory.getLogger(RadioBoxList.class);
    private String col = "3";
    private boolean isShowTable = true;
    private String selectedValue;
    private String defaultSelectValue;
    private String valueText;
    private String indent = "0";
    /**
     * com.sparrow.enums.Status:10:true enums:maxCount:isShowName
     */
    private String enums;

    public boolean isShowTable() {
        return isShowTable;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void setShowTable(boolean isShowTable) {
        this.isShowTable = isShowTable;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        StringBuilder writeHTML = new StringBuilder();
        // value text
        Map<String, String> valueTextList = (Map<String, String>) this.pageContext
            .getRequest().getAttribute(this.getId() + ".valueText");
        // 如果后台有设置 id.valueText
        // 如果后台没有设置
        if (valueTextList == null) {
            valueTextList = new LinkedHashMap<String, String>();
            String[] options;
            String[] option;
            if (!StringUtility.isNullOrEmpty(this.valueText)) {
                options = this.valueText.split(",");
                for (String keyValue : options) {
                    option = keyValue.split(":");
                    valueTextList.put(option[0], option[1]);
                }
            }

            if (valueTextList.size() == 0
                && !StringUtility.isNullOrEmpty(this.getEnums())) {
                valueTextList = EnumUtility.getMap(this.getEnums());
            }
        }
        if (!Boolean.FALSE.toString().equalsIgnoreCase(this.getVisible()) && valueTextList != null
            && valueTextList.size() > 0) {
            if (this.isShowTable()) {
                this.drawTable(writeHTML);
            }
            String strValue;
            String strChecked = "";
            Set<String> value = valueTextList.keySet();
            Iterator<String> it = value.iterator();
            int row = (int) Math.ceil(valueTextList.size()
                / Double.valueOf(col));
            for (int i = 0; i < row; i++) {
                if (this.isShowTable()) {
                    writeHTML.append(StringUtility.getIndent(Integer.valueOf(this
                        .getIndent() + 1)) + "<tr>");
                }
                for (int j = 0; j < Integer.valueOf(col); j++) {
                    if (it.hasNext()) {
                        strValue = it.next();
                        if (i == 0 && j == 0) {
                            this.defaultSelectValue = strValue;
                        }
                        if (strValue.equals(this.getSelectedValue())) {
                            strChecked = "checked=true";
                        } else {
                            strChecked = "";
                        }
                        if (this.isShowTable()) {
                            writeHTML.append(StringUtility.getIndent(Integer
                                .valueOf(this.getIndent() + 2)) + "<td>");
                        }
                        writeHTML
                            .append(StringUtility.getIndent(Integer
                                .valueOf(this.getIndent()) + 3)
                                + String.format(
                                "<input id=\"%7$s%1$s%2$s\"  type=\"radio\" %3$s value=\"%4$s\"  %6$s /><label for=\"%7$s%1$s%2$s\" >%5$s</label>",
                                // 行
                                i,
                                // 列
                                j,
                                // name
                                this.getName(),
                                // value
                                strValue,
                                // test
                                valueTextList.get(strValue),
                                // 是否被选中
                                strChecked,
                                //同一个界面中可能会有多个list
                                this.getId()
                            ));
                        if (this.isShowTable) {
                            writeHTML.append("</td>");
                        }
                    } else {
                        if (this.isShowTable()) {
                            writeHTML.append("<td></td>");
                        }
                    }
                }
                if (this.isShowTable()) {
                    writeHTML.append(StringUtility.getIndent(Integer.valueOf(this
                        .getIndent() + 1)) + "</tr>");
                }
            }
            if (this.isShowTable()) {
                writeHTML.append("</table>");
            }
        }
        try {
            if (valueTextList != null && valueTextList.size() != 0) {
                this.pageContext.getOut().println(writeHTML.toString());
            }
        } catch (IOException e) {
            logger.error("out error", e);
        }
        return returnValue;

    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getEnums() {
        return enums;
    }

    public void setEnums(String enums) {
        this.enums = enums;
        this.setNameOfEnum(enums);
    }

    public String getSelectedValue() {
        String selectValue = (String) this.pageContext.getRequest()
            .getAttribute(this.getId() + ".selectedValue");
        if (selectValue == null) {
            selectValue = super.getRequestValue();
        }
        if (selectValue == null) {
            selectValue = this.selectedValue;
        }
        if (StringUtility.isNullOrEmpty(selectValue)) {
            selectValue = this.defaultSelectValue;
        }
        return selectValue;
    }

}
