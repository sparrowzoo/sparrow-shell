package com.ideaworks.club.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBean {
    private int code;
    private String msg;
    private Object data;
}
