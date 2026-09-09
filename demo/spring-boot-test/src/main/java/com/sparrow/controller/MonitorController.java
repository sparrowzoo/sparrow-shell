package com.sparrow.controller;

import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.spring.filter.monitor.MonitorResult;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {
    @Inject
    private Monitor monitor;

    @RequestMapping("api/monitor")
    public MonitorResult monitor() {
        return monitor.result();
    }
}
