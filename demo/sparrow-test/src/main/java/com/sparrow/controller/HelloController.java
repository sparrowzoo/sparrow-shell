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
package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;
import com.sparrow.mvc.ViewWithModel;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.vo.HelloVO;
import com.sparrow.vo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HelloController {


    private ServletContainer servletContainer;


    public void setServletContainer(ServletContainer servletContainer) {
        this.servletContainer = servletContainer;
    }

    public ViewWithModel hello() throws BusinessException {
        return ViewWithModel.forward("hello", new HelloVO("我来自遥远的sparrow 星球,累死我了..."));
    }

    @RequestParameters("key")
    public HelloVO env(String key) {
        return new HelloVO(System.getenv(key));
    }

    @RequestParameters("key")
    public HelloVO env2(String key) throws BusinessException {
        return new HelloVO(key);
    }

    public ViewWithModel exception() throws BusinessException {
        throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
    }

    public ViewWithModel fly() throws BusinessException {
        return ViewWithModel.redirect("fly-here", new HelloVO("我是从fly.jsp飞过来，你是不是没感觉？"));
    }

    public ViewWithModel transit() throws BusinessException {
        return ViewWithModel.transit(new HelloVO("我从你这歇一会，最终我要到transit-here"));
    }

    @RequestParameters("threadId,pageIndex")
    public ViewWithModel thread(Long threadId, Integer pageIndex) {
        return ViewWithModel.forward("thread", new HelloVO("服务器的threadId=" + threadId + ",pageIndex=" + pageIndex));
    }

    public ViewWithModel thymeleaf(HttpServletRequest request) {
        com.sparrow.vo.User user = new User();
        user.setUserId("我是服务器的用户ID 小志");
        request.setAttribute("user", user);
        return ViewWithModel.forward("/thymeleaf-test");
    }

    public HelloVO json() {
        return new HelloVO("够意思吧，json不用页面");
    }

    public PagerResult<HelloVO> pager() {
        PagerResult<HelloVO> pagerResult = new PagerResult<>();
//        pagerResult.setAddition(new JsonVO("json"));
        pagerResult.setRecordTotal(1000L);
        pagerResult.setPageSize(100);
        pagerResult.setPageNo(1);
        List<HelloVO> hellos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            hellos.add(new HelloVO(i + ""));
        }
        pagerResult.setList(hellos);
        return pagerResult;
    }

    public ViewWithModel welcome() {
        return ViewWithModel.forward(new HelloVO("jsp page content from server ..."));
    }

    public ViewWithModel login(HttpServletRequest request) {
        ServletUtility servletUtility = ServletUtility.getInstance();
        return null;
    }

    public ViewWithModel authorizing() {
        return ViewWithModel.forward(new HelloVO("你成功了"));
    }
}
