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

package com.sparrow.mvc;

import com.sparrow.protocol.DTO;
import com.sparrow.protocol.constant.Constant;

public class ViewWithModel {
    private DTO dto;
    /**
     * 目标url 不包括 template 及.jsp ....
     */
    private String url;
    /**
     * 中转url 不包括 template 及.jsp ....
     */
    private String transitUrl;
    private PageSwitchMode switchMode;
    private String[] urlArgs;

    private ViewWithModel() {
    }

    private ViewWithModel(String transitUrl, String targetUrl, PageSwitchMode switchMode, DTO dto) {
        this.transitUrl = transitUrl;
        this.dto = dto;
        this.url = targetUrl;
        this.switchMode = switchMode;
    }

    public static ViewWithModel forward() {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.FORWARD, null);
    }

    public static ViewWithModel forward(DTO dto) {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.FORWARD, dto);
    }

    public static ViewWithModel forward(String targetUrl) {
        return new ViewWithModel(null, targetUrl, PageSwitchMode.FORWARD, null);
    }

    public static ViewWithModel forward(String targetUrl, DTO dto) {
        return new ViewWithModel(null, targetUrl, PageSwitchMode.FORWARD, dto);
    }

    public static ViewWithModel transit() {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.TRANSIT, null);
    }

    public static ViewWithModel transit(DTO dto) {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.TRANSIT, dto);
    }

    public static ViewWithModel transit(String url) {
        return new ViewWithModel(null, url, PageSwitchMode.TRANSIT, null);
    }

    public static ViewWithModel transit(String targetUrl, DTO dto) {
        return new ViewWithModel(null, targetUrl, PageSwitchMode.TRANSIT, dto);
    }

    public static ViewWithModel transit(String transitUrl, String targetUrl, DTO dto) {
        return new ViewWithModel(transitUrl, targetUrl, PageSwitchMode.TRANSIT, dto);
    }

    public static ViewWithModel redirect(DTO dto) {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.REDIRECT, dto);
    }

    public static ViewWithModel redirect() {
        return new ViewWithModel(null, Constant.SUCCESS, PageSwitchMode.REDIRECT, null);
    }

    public static ViewWithModel redirect(String url) {
        return new ViewWithModel(null, url, PageSwitchMode.REDIRECT, null);
    }

    public static ViewWithModel redirect(String url, DTO dto) {
        return new ViewWithModel(null, url, PageSwitchMode.REDIRECT, dto);
    }

    public DTO getDTO() {
        return dto;
    }

    public String getUrl() {
        return url;
    }

    public PageSwitchMode getSwitchMode() {
        return switchMode;
    }

    public String getTransitUrl() {
        return transitUrl;
    }

    public void setTransitUrl(String transitUrl) {
        this.transitUrl = transitUrl;
    }

    public String[] getUrlArgs() {
        return urlArgs;
    }

    public ViewWithModel setUrlArgs(String... urlArgs) {
        this.urlArgs = urlArgs;
        return this;
    }
}
