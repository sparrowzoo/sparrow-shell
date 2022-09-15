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

package com.sparrow.mvc.ui.grid.attribute;

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.mvc.ui.grid.BaseAttribute;
import com.sparrow.utility.ConfigUtility;

/**
 * 格式button#fieldName&fieldName2#format#javascriptClickEvent({0},{1})#cssClass#"" 例子button#filedName&idField#{0}查看详情#Sparrow.alert({0},{1})#button#defautValue
 */
public class ButtonAttribute extends BaseAttribute {
    private String click;

    public ButtonAttribute(String[] config) {
        super(config);
        this.defaultValue = "";
        this.css = "button";
        if (config.length > Digit.THREE) {
            this.click = config[Digit.THREE];
            if (this.click.contains("frontend_root_path")) {
                this.click = this.click.replace("frontend_root_path", ConfigUtility.getValue(Config.FRONTEND_ROOT_PATH));
            }
        }
        if (config.length > Digit.FOUR) {
            this.css = config[Digit.FOUR];
        }
        if (config.length > Digit.FIVE) {
            this.defaultValue = config[Digit.FIVE];
        }
    }

    public String getClick() {
        return click;
    }
}
