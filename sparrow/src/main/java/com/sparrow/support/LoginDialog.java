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

package com.sparrow.support;

import com.sparrow.protocol.POJO;

public class LoginDialog implements POJO {
    private Boolean login;
    private Boolean showHead;
    private String url;
    private Boolean inFrame;

    public LoginDialog(Boolean login, Boolean showHead, String url, Boolean inFrame) {
        this.login = login;
        this.showHead = showHead;
        this.url = url;
        this.inFrame = inFrame;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public Boolean getShowHead() {
        return showHead;
    }

    public void setShowHead(Boolean showHead) {
        this.showHead = showHead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getInFrame() {
        return inFrame;
    }

    public void setInFrame(Boolean inFrame) {
        this.inFrame = inFrame;
    }
}
