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
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.mvc.ui.grid.BaseAttribute;
import com.sparrow.utility.ConfigUtility;

/**
 * 格式image#fielName#format#width#height#defaultIco 例子image#filedName#图片显示文本 title#60px#40px#http://r.**.net/defualt.ico
 */
public class ImageAttribute extends BaseAttribute {
    private String width = "60px";
    private String height = "auto";

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public ImageAttribute(String[] config) {
        super(config);
        if (config.length > Digit.THREE) {
            this.width = config[Digit.THREE];
        }
        if (config.length > Digit.FOUR) {
            this.height = config[Digit.FOUR];
        }
        if (config.length > Digit.FIVE) {
            this.defaultValue = config[Digit.FIVE];
            if (!this.defaultValue.startsWith(Constant.HTTP_PROTOCOL)) {
                this.defaultValue = ConfigUtility.getValue(Config.RESOURCE) + this.defaultValue;
            }
        }
    }
}
