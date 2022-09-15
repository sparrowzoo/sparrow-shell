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

import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.EnumUtility;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 调用说明
 * <p/>
 * 前台设置
 * <p/>
 * <J:JCheckBoxList name="chlDemo" showTable="false" valueText="0:女,1:男,2:保密" col="2" id="chlDemo"></J:JCheckBoxList>
 * <p/>
 * 后台设置
 * <p/>
 * String[] selectedList =new String[]; Map<String, String> allItems=null; super.getRequest().setAttribute("chlRoleList.valueText",
 * allItems); super .getRequest().setAttribute("chlRoleList.selectedValue",selectedList );
 */
public class CheckBoxList extends WebControl {
    private static final long serialVersionUID = -2203780367514791117L;
    private boolean isShowTable = true;
    private int col = Digit.THREE;
    private Object[] selectedValue;
    /**
     * com.sparrow.enums.Status:10:true enums:maxCount:isShowName
     */
    private String enums;

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    private int indent = Digit.ZERO;

    private String valueText;

    public boolean isShowTable() {
        return isShowTable;
    }

    public void setShowTable(boolean isShowTable) {
        this.isShowTable = isShowTable;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int doStartTag() throws JspException {
        int returnValue = TagSupport.SKIP_BODY;
        StringBuilder writeHTML = new StringBuilder();
        // value test
        Map<String, String> valueTextList = null;
        // 如果后台有设置 id.valueText
        valueTextList = (Map<String, String>) this.pageContext.getRequest()
            .getAttribute(this.getId() + ".valueText");
        // 如果后台没有设置
        if (valueTextList == null) {
            valueTextList = new LinkedHashMap<String, String>();
            String[] options;
            String[] option;
            if (!StringUtility.isNullOrEmpty(this.valueText)) {
                options = this.valueText.split(Symbol.DOT);
                for (String keyValue : options) {
                    option = keyValue.split(Symbol.COLON);
                    valueTextList.put(option[Digit.ZERO], option[Digit.ONE]);
                }
            }

            if (valueTextList.size() == Digit.ZERO
                && !StringUtility.isNullOrEmpty(this.getEnums())) {
                valueTextList = EnumUtility.getMap(this.getEnums());
            }
        }
        if (!Boolean.FALSE.toString().equalsIgnoreCase(this.getVisible()) && valueTextList != null
            && valueTextList.size() != Digit.ZERO) {
            if (this.isShowTable) {
                drawTable(writeHTML);
            }
            Iterator it = valueTextList.keySet().iterator();
            int row = (int) Math.ceil(valueTextList.size()
                / col);
            for (int i = 0; i < row; i++) {
                if (this.isShowTable) {
                    writeHTML.append(StringUtility.getIndent(this
                        .getIndent() + 1) + "<tr>");
                }
                drawColumn(writeHTML, valueTextList, it, i);
                if (this.isShowTable) {
                    writeHTML.append(StringUtility.getIndent(this
                        .getIndent() + 1) + "</tr>");
                }
            }
            if (this.isShowTable) {
                writeHTML.append("</table>");
            }
        }
        try {
            if (valueTextList != null && valueTextList.size() != 0) {
                this.pageContext.getOut().println(writeHTML.toString());
            }
        } catch (IOException e) {

        }
        return returnValue;
    }

    private void drawColumn(StringBuilder writeHTML, Map<String, String> valueTextList, Iterator it, int i) {
        Object key;
        String strChecked;
        for (int j = 0; j < col; j++) {
            if (!it.hasNext()) {
                if (this.isShowTable) {
                    writeHTML.append("<td></td>");
                }
                continue;
            }

            key = it.next();
            this.setSelectedValue();
            strChecked = checked(key);

            if (this.isShowTable) {
                writeHTML.append(StringUtility.getIndent(this.getIndent() + Digit.TOW) + "<td>");
            }
            writeHTML
                .append(StringUtility.getIndent(this.getIndent() + Digit.THREE)
                    + String.format(
                    "<input id=\"%7$s%1$s%2$s\"  type=\"checkbox\" %3$s value=\"%4$s\"  %6$s /><label for=\"%7$s%1$s%2$s\" >%5$s</label>",
                    i,
                    j,
                    this.getName(),
                    key,
                    valueTextList.get(key),
                    strChecked,
                    this.getId()));
            if (this.isShowTable) {
                writeHTML.append("</td>");
            }
        }
    }

    private String checked(Object key) {
        if (this.selectedValue == null) {
            return Symbol.EMPTY;
        }
        if (this.selectedValue.length == 0) {
            return Symbol.EMPTY;
        }

        //已选值为all
        if (String.valueOf(Digit.ALL).equals(this.selectedValue[0].toString().trim())) {
            return "checked=true";
        }
        //当前选项在已选列表中
        if (StringUtility.existInArray(this.selectedValue,
            key.toString())) {
            return "checked=true";
        }
        return Symbol.EMPTY;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public String getEnums() {
        return enums;
    }

    public void setEnums(String enums) {
        this.enums = enums;
        this.setNameOfEnum(enums);
    }

    public void setSelectedValue() {
        Object selectedValue = this.pageContext.getRequest().getAttribute(
            this.getId() + ".selectedValue");
        if (selectedValue == null) {
            this.selectedValue = this.pageContext.getRequest()
                .getParameterValues(this.getCtrlName());
            return;
        }
        if (selectedValue.getClass() == Number.class
            && Integer.valueOf(selectedValue.toString()) == Digit.ALL) {
            this.selectedValue = new String[1];
            this.selectedValue[0] = String.valueOf(Digit.ALL);
            return;
        }
        this.selectedValue = (Object[]) selectedValue;
    }
}
