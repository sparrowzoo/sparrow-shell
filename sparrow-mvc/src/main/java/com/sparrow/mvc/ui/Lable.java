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

public class Lable extends AbstractJWebBodyControl {

    private String forCtrl;
    /**
     *
     */
    private static final long serialVersionUID = 7885097366405702047L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getForCtrl() {
        return forCtrl;
    }

    public void setForCtrl(String forCtrl) {
        this.forCtrl = forCtrl;
    }

    @Override
    public String setTagNameAndGetTagAttributes() {
        super.setTagName("Label");
        if (!StringUtility.isNullOrEmpty(this.getForCtrl())) {
            return String.format(" for='%1$s' ", this.getForCtrl());
        }
        return Symbol.EMPTY;
    }
}
