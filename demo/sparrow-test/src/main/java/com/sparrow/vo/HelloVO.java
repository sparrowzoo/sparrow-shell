package com.sparrow.vo;

import com.sparrow.protocol.DTO;

/**
 * Created by harry on 2018/1/31.
 */
public class HelloVO implements DTO {
    private String hello;

    public HelloVO(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }
}
