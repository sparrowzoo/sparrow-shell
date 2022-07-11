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

package com.sparrow.validate;

public class Validate {
    private String id;

    private String control;

    private Boolean nullable;

    private Boolean digital;

    private String compareId;
    
    private String regex;

    private String errorNullable;
    private String errorDigital;
    private String errorCompare;
    private String errorRegex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean getDigital() {
        return digital;
    }

    public void setDigital(Boolean digital) {
        this.digital = digital;
    }

    public String getCompareId() {
        return compareId;
    }

    public void setCompareId(String compareId) {
        this.compareId = compareId;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getErrorNullable() {
        return errorNullable;
    }

    public void setErrorNullable(String errorNullable) {
        this.errorNullable = errorNullable;
    }

    public String getErrorDigital() {
        return errorDigital;
    }

    public void setErrorDigital(String errorDigital) {
        this.errorDigital = errorDigital;
    }

    public String getErrorCompare() {
        return errorCompare;
    }

    public void setErrorCompare(String errorCompare) {
        this.errorCompare = errorCompare;
    }

    public String getErrorRegex() {
        return errorRegex;
    }

    public void setErrorRegex(String errorRegex) {
        this.errorRegex = errorRegex;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }
}
