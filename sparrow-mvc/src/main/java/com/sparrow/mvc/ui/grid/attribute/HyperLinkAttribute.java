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
 * 格式hyperLink#name&id#format#url#target#css#textLength#defaultValue
 * //todo 加是否可见表达式
 * 例子hyperLink#filedName&idField#{0}#url?id={1}#_blank#css#100#查看
 */
public class HyperLinkAttribute extends BaseAttribute {
    private String target = "_blank";
    private String url;

    public HyperLinkAttribute(String[] config) {
        super(config);
        if (config.length > Digit.THREE) {
            this.url = config[Digit.THREE];
            if (this.url.contains("frontend_root_path")) {
                this.url = this.url.replace("frontend_root_path", ConfigUtility.getValue(Config.FRONTEND_ROOT_PATH));
            }
        }
        if (config.length > Digit.FOUR) {
            this.target = config[Digit.FOUR];
        }

        if (config.length > Digit.FIVE) {
            this.css = config[Digit.FIVE];
        }
        if (config.length > Digit.SIX) {
            this.textLength = Integer.valueOf(config[Digit.SIX]);
        }
        if (config.length > Digit.SEVEN) {
            this.defaultValue = config[Digit.SEVEN];
        }
    }

    public String getUrl() {
        return url;
    }

    public String getTarget() {
        return this.target;
    }
}
