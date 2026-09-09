package com.sparrow.controller;

import com.sparrow.spring.config.SparrowConfig;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
    @Inject
    private SparrowConfig config;

    @RequestMapping("api/config")
    public SparrowConfig config() {
        return config;
    }
}
