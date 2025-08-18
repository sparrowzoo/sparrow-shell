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

public enum SparrowError implements ErrorSupport {
    SYSTEM_SERVER_ERROR("01", "System error"),
    SYSTEM_SERVICE_UNAVAILABLE("02", "Service unavailable"),
    SYSTEM_REMOTE_SERVICE_UNAVAILABLE("03", "Remote Service unavailable"),
    SYSTEM_PERMISSION_DENIED("04", "Permission denied"),
    SYSTEM_ILLEGAL_REQUEST("05", "Illegal request"),
    GLOBAL_DB_ADD_ERROR("06", "add error"),
    GLOBAL_DB_DELETE_ERROR("07", "delete error"),
    GLOBAL_DB_UPDATE_ERROR("08", "update error"),
    GLOBAL_DB_LOAD_ERROR("09", "load error"),
    GLOBAL_REQUEST_ID_NOT_EXIST("11", "Request id not exist"),
    GLOBAL_VALIDATE_CODE_ERROR("12", "ValidateCode error"),
    GLOBAL_CONTENT_IS_NULL("13", "Content is null"),
    GLOBAL_CONTAIN_ILLEGAL_WEBSITE("14", "Contain illegal website"),
    GLOBAL_CONTAIN_ADVERTISING("15", "Contain advertising"),
    GLOBAL_CONTENT_IS_ILLEGAL("16", "Content is illegal"),
    GLOBAL_CONTENT_DUPLICATE("17", "Content duplicate"),
    GLOBAL_UNSUPPORTED_IMAGE_TYPE("18",
            "Unsupported image type only support JPG, GIF, PNG"),
    GLOBAL_IMAGE_SIZE_TOO_LARGE("19", "Image size too large"),
    GLOBAL_ACCOUNT_ILLEGAL("20",
            "Account or ip or app is illegal, can not continue"),
    GLOBAL_OUT_OF_TIMES_LIMIT("21", "Out of times limit"),
    GLOBAL_ADMIN_CAN_NOT_OPERATION("22", "Admin can't operation"),
    GLOBAL_PARAMETER_NULL("23", "Parameter is null"),
    GLOBAL_REQUEST_REPEAT("24", "Request repeat"),
    GLOBAL_EMAIL_SEND_FAIL("25", "email send fail"),
    GLOBAL_OPERATION_VALIDATE_STATUS_INVALID("26", "operation validate status is invalid"),
    GLOBAL_OPERATION_VALIDATE_ROLE_INVALID("27", "operation validate role is invalid"),
    GLOBAL_PARAMETER_IS_ILLEGAL("28", "parameter is illegal"),
    GLOBAL_SMS_SEND_ERROR("29", "short message service error"),
    IMAGE_EXTENSION_NOT_FOUND("39", "image extension not found");


    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    SparrowError(String code, String message) {
        this.system = true;
        this.message = message;
        this.module = GlobalModule.GLOBAL;
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
