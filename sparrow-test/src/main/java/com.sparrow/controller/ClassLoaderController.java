package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ClassLoaderController {
    @RequestParameters("response")
    public void getParent(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        ClassLoader loader = this.getClass().getClassLoader();

        while (loader != null) {
            out.write(loader + "<br/>");
            loader = loader.getParent();
        }

        out.flush();
        out.close();
    }
}
