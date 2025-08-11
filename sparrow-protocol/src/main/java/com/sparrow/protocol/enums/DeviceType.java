package com.sparrow.protocol.enums;

import com.sparrow.protocol.EnumIdentityAccessor;

public enum DeviceType implements EnumIdentityAccessor {
    PC(1),
    MOBILE(2),
    IPAD(3);


    DeviceType(Integer id) {
        this.id = id;
    }

    private final Integer id;

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public DeviceType getDeviceById(Integer id) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (deviceType.id.equals(id)) {
                return deviceType;
            }
        }
        return null;
    }
}
