package com.sparrow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "World!";
    }

    @GetMapping("/world")
    public String world() {
        return "World!";
    }
}
