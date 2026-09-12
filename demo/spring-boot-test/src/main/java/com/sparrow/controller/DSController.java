package com.sparrow.controller;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.google.common.collect.Lists;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

@RestController
public class DSController {
    @Inject
    private DataSource ds;
    /**
     * 没有对外暴露
     */
    private DruidPasswordCallback callback;

    @RequestMapping("/api/ds")
    public List<String> ds() {
        return Lists.newArrayList(ds.getClass().getName(), ds.toString());
    }
}
