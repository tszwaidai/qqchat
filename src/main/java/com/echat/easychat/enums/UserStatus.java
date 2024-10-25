package com.echat.easychat.enums;

public enum UserStatus {
    ENABLED(true),   // 启用状态
    DISABLED(false); // 禁用状态

    private final boolean value;

    UserStatus(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    // 根据布尔值获取对应的枚举
    public static UserStatus fromBoolean(boolean status) {
        return status ? ENABLED : DISABLED;
    }
}
