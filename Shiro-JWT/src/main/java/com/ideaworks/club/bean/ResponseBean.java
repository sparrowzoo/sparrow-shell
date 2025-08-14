package com.ideaworks.club.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一返回结果
 * @author 王庆港
 * @version 1.0.0
*/
@Data
@AllArgsConstructor
public class ResponseBean {
    private int code;
    private String msg;
    private Object data;
}
