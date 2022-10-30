/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"), you may not use this file except in compliance with
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

package com.sparrow.protocol.constant;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

/**
 * first byte:
 * <p>
 * 0 表示系统模块
 * <p>
 * 1 模块错误
 * <p>
 * 2-3 bytes:
 * <p>
 * 00:全局模块(公共使用)
 * <p>
 * 01:用户模块
 * <p>
 * 02:EXCEL
 * <p>
 * 03:BLOG
 * <p>
 * 04:SHOP
 * <p>
 * 05:UPLOAD
 * <p>
 * 06:ACTIVITY
 * <p>
 * 4-5 bytes
 * <p>
 * 错误编码
 * <p>
 * <p>
 * 对于开发者和接口的调用者都隐藏着一个信息（当前操作的接口名称）
 */
public enum SparrowError implements ErrorSupport {
    SYSTEM_SERVER_ERROR(true, GlobalModule.GLOBAL, "01", "System error"),
    SYSTEM_SERVICE_UNAVAILABLE(true, GlobalModule.GLOBAL, "02", "Service unavailable"),
    SYSTEM_REMOTE_SERVICE_UNAVAILABLE(true, GlobalModule.GLOBAL, "03", "Remote Service unavailable"),
    SYSTEM_PERMISSION_DENIED(true, GlobalModule.GLOBAL, "04", "Permission denied"),
    SYSTEM_ILLEGAL_REQUEST(true, GlobalModule.GLOBAL, "05", "Illegal request"),
    GLOBAL_DB_ADD_ERROR(true, GlobalModule.GLOBAL, "06", "add error"),
    GLOBAL_DB_DELETE_ERROR(true, GlobalModule.GLOBAL, "07", "delete error"),
    GLOBAL_DB_UPDATE_ERROR(true, GlobalModule.GLOBAL, "08", "update error"),
    GLOBAL_DB_LOAD_ERROR(true, GlobalModule.GLOBAL, "09", "load error"),
    GLOBAL_REQUEST_ID_NOT_EXIST(true, GlobalModule.GLOBAL, "11", "Request id not exist"),
    GLOBAL_VALIDATE_CODE_ERROR(true, GlobalModule.GLOBAL, "12", "ValidateCode error"),
    GLOBAL_CONTENT_IS_NULL(true, GlobalModule.GLOBAL, "13", "Content is null"),
    GLOBAL_CONTAIN_ILLEGAL_WEBSITE(true, GlobalModule.GLOBAL, "14", "Contain illegal website"),
    GLOBAL_CONTAIN_ADVERTISING(true, GlobalModule.GLOBAL, "15", "Contain advertising"),
    GLOBAL_CONTENT_IS_ILLEGAL(true, GlobalModule.GLOBAL, "16", "Content is illegal"),
    GLOBAL_CONTENT_DUPLICATE(true, GlobalModule.GLOBAL, "17", "Content duplicate"),
    GLOBAL_UNSUPPORTED_IMAGE_TYPE(true, GlobalModule.GLOBAL, "18",
        "Unsupported image type only support JPG, GIF, PNG"),
    GLOBAL_IMAGE_SIZE_TOO_LARGE(true, GlobalModule.GLOBAL, "19", "Image size too large"),
    GLOBAL_ACCOUNT_ILLEGAL(true, GlobalModule.GLOBAL, "20",
        "Account or ip or app is illegal, can not continue"),
    GLOBAL_OUT_OF_TIMES_LIMIT(true, GlobalModule.GLOBAL, "21", "Out of times limit"),
    GLOBAL_ADMIN_CAN_NOT_OPERATION(true, GlobalModule.GLOBAL, "22", "Admin can't operation"),
    GLOBAL_PARAMETER_NULL(true, GlobalModule.GLOBAL, "23", "Parameter is null"),
    GLOBAL_REQUEST_REPEAT(true, GlobalModule.GLOBAL, "24", "Request repeat"),
    GLOBAL_EMAIL_SEND_FAIL(true, GlobalModule.GLOBAL, "25", "email send fail"),
    GLOBAL_OPERATION_VALIDATE_STATUS_INVALID(true, GlobalModule.GLOBAL, "26", "operation validate status is invalid"),
    GLOBAL_OPERATION_VALIDATE_ROLE_INVALID(true, GlobalModule.GLOBAL, "27", "operation validate role is invalid"),
    GLOBAL_PARAMETER_IS_ILLEGAL(true, GlobalModule.GLOBAL, "28", "parameter is illegal"),
    GLOBAL_SMS_SEND_ERROR(true, GlobalModule.GLOBAL, "29", "short message service error"),
    USER_NOT_LOGIN(true, GlobalModule.GLOBAL, "34", "user not login"),
    IMAGE_EXTENSION_NOT_FOUND(true, GlobalModule.GLOBAL, "35", "image extension not found");

    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    SparrowError(boolean system, ModuleSupport module, String code, String message) {
        this.system = system;
        this.message = message;
        this.module = module;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
