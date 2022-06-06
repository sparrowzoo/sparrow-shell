package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;
import com.sparrow.protocol.BusinessException;
import com.sparrow.vo.HelloVO;
import com.sparrow.protocol.mvc.ViewWithModel;

/**
 * @author harry
 */
public class HelloController {
    public ViewWithModel hello() throws BusinessException {
        return ViewWithModel.forward("hello", new HelloVO("我来自遥远的sparrow 星球,累死我了..."));
    }

    public ViewWithModel fly() throws BusinessException {
        return ViewWithModel.redirect("fly-here", new HelloVO("我是从fly.jsp飞过来，你是不是没感觉？"));
    }

    public ViewWithModel transit() throws BusinessException {
        return ViewWithModel.transit("transit-here", new HelloVO("我从你这歇一会，最终我要到transit-here"));
    }

    @RequestParameters("threadId,pageIndex")
    public ViewWithModel thread(Long threadId, Integer pageIndex) {
        return ViewWithModel.forward("thread", new HelloVO("服务器的thread" + threadId + "-" + pageIndex));
    }

    public HelloVO json() {
        return new HelloVO("够意思吧，json不用页面");
    }

    public ViewWithModel welcome(){
        return ViewWithModel.forward(new HelloVO("jsp page content from server ..."));
    }
}
