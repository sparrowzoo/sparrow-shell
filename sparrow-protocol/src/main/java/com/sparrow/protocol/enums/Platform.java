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
package com.sparrow.protocol.enums;

public enum Platform {

    Unkonwn(-1, "Unkonwn"),
    IOS(1, "IOS"),
    Android(2, "Android"),
    PC(0, "PC"),
    WECHAT(3, "WECHAT"),
    ;

    private int platform;

    private String desc;

    Platform(int platform, String desc) {
        this.platform = platform;
        this.desc = desc;
    }

    public int getPlatform() {
        return platform;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "Platform{" +
            "platform=" + platform +
            ", desc='" + desc + '\'' +
            '}';
    }

    public static Platform getByPlatform(int platform) {
        if (platform == IOS.platform) {
            return IOS;
        }

        if (platform == Android.platform) {
            return Android;
        }

        if (platform == PC.platform) {
            return PC;
        }

        if (platform == WECHAT.platform) {
            return WECHAT;
        }

        return Unkonwn;
    }
}
