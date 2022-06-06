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

package com.sparrow.protocol.mvc;

import com.sparrow.protocol.VO;
import com.sparrow.protocol.constant.CONSTANT;

/**
 * @author harry
 */
public class ViewWithModel {
    private VO vo;
    private String url;
    private String transitUrl;
    private PageSwitchMode switchMode;
    private String []urlArgs;

    private ViewWithModel() {
    }

    private ViewWithModel(String url, PageSwitchMode switchMode) {
        this(url, switchMode, null);
    }

    private ViewWithModel(String url, PageSwitchMode switchMode, VO vo) {
        this(null, url, switchMode, vo);
    }

    private ViewWithModel(String transitUrl, String url, PageSwitchMode switchMode, VO vo) {
        this.transitUrl = transitUrl;
        this.vo = vo;
        this.url = url;
        this.switchMode = switchMode;
    }

    public static ViewWithModel forward() {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.FORWARD, null);
    }

    public static ViewWithModel forward(VO vo) {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.FORWARD, vo);
    }

    public static ViewWithModel forward(String url) {
        return new ViewWithModel(url, PageSwitchMode.FORWARD, null);
    }

    public static ViewWithModel forward(String url, VO vo) {
        return new ViewWithModel(url, PageSwitchMode.FORWARD, vo);
    }

    public static ViewWithModel transit() {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.TRANSIT, null);
    }

    public static ViewWithModel transit(VO vo) {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.TRANSIT, vo);
    }

    public static ViewWithModel transit(String transitUrl) {
        return transit(transitUrl, null);
    }

    public static ViewWithModel transit(String transitUrl, VO vo) {
        return new ViewWithModel(transitUrl, CONSTANT.SUCCESS, PageSwitchMode.TRANSIT, vo);
    }

    public static ViewWithModel redirect(VO vo) {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.REDIRECT, vo);
    }

    public static ViewWithModel redirect() {
        return new ViewWithModel(CONSTANT.SUCCESS, PageSwitchMode.REDIRECT, null);
    }


    public static ViewWithModel redirect(String url) {
        return new ViewWithModel(url, PageSwitchMode.REDIRECT, null);
    }



    public static ViewWithModel redirect(String url, VO vo) {
        return new ViewWithModel(url, PageSwitchMode.REDIRECT, vo);
    }

    public VO getVo() {
        return vo;
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
