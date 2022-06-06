package com.sparrow.protocol.enums;

/**
 * @author zh_harry@163.com
 */
public enum PLATFORM {

    Unkonwn(-1, "Unkonwn"),
    IOS(1, "IOS"),
    Android(2, "Android"),
    PC(0, "PC"),
    WECHAT(3, "WECHAT"),
    ;

    private int platform;

    private String desc;

    PLATFORM(int platform, String desc) {
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

    public static PLATFORM getByPlatform(int platform) {
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
