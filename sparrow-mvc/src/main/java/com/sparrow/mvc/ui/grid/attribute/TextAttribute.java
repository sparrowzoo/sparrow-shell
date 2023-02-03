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

import com.sparrow.mvc.ui.grid.BaseAttribute;

/**
 * 格式text#filedName#format#textLength#defaultValue 例子text#name&sex#姓名+姓别#100#200 {0}+{2}
 */
public class TextAttribute extends BaseAttribute {
    public TextAttribute(String[] config) {
        super(config);
        if (config.length > 3) {
            this.textLength = Integer.valueOf(config[2]);
        }
        if (config.length > 4) {
            this.defaultValue = config[3];
        }
    }
}
