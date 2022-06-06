/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.protocol;

import com.sparrow.protocol.constant.magic.SYMBOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 业务异常 程序中断
 *
 * @author harry
 */
public class BusinessException extends Exception {
    /**
     * 错误码
     */
    private String code;
    /**
     * 用于提示信息国际化的key
     * <p>
     * key=ErrorSupport.name()+suffix ［
     * <p>
     * suffix 对应前端界面  input name
     * <p>
     * 由于 error support 提供的可能是公共错误信息，针对每一个输入可能提示信息不一样
     * <p>
     * 举例:
     * <p>
     * GLOBAL_CONTENT_IS_NULL
     * <p>
     * 提示信息可能为:
     * <p>
     * 1. 用户名不允许为空
     * <p>
     * 2. 密码不能为空...
     * <p>
     * 注:由客户端负责国际化配置
     */
    private String key;
    /**
     * 其他参数
     */
    private List<Object> parameters;

    public BusinessException(ErrorSupport errorSupport, String suffix, List<Object> parameters) {
        super(errorSupport.getMessage());
        this.key = errorSupport.name();
        if (suffix != null) {
            this.key = this.key + "." + suffix.toLowerCase();
        }
        this.code = errorSupport.getCode();
        if (parameters != null && parameters.size() > 0 && !parameters.get(0).equals(SYMBOL.EMPTY)) {
            this.parameters = parameters;
        }
    }

    public BusinessException(ErrorSupport errorSupport, String suffix, Object parameter) {
        this(errorSupport, suffix, Collections.singletonList(parameter));
    }

    public BusinessException(ErrorSupport errorSupport, List<Object> parameters) {
        this(errorSupport, null, parameters);
    }

    public BusinessException(ErrorSupport errorSupport, String suffix) {
        this(errorSupport, suffix, "");
    }

    public BusinessException(ErrorSupport errorSupport) {
        this(errorSupport, null, SYMBOL.EMPTY);
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            for (Object object : parameters) {
                if (sb.length() > 0) {
                    sb.append(SYMBOL.COMMA);
                }
                sb.append(object.toString().trim());
            }
        }
        return String.format("key:%s,code:%s,parameters:%s", key, code,
                sb.toString());
    }
}
