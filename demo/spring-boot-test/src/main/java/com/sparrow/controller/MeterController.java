package com.sparrow.controller;

import com.alibaba.fastjson.JSON;
import com.sparrow.query.PostQuery;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeterController {
    @RequestMapping("/test/post")
    public String post(@RequestBody PostQuery postQuery) {
        return "meter post!" + JSON.toJSONString(postQuery);
    }

    @GetMapping("/test/get")
    public String get(@RequestParam("threadId") Long theadId) {
        return "meter get!" + theadId;
    }
}
