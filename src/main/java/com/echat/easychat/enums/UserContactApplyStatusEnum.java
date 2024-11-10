package com.echat.easychat.enums;

public enum UserContactApplyStatusEnum {
    INIT(0, "待处理"),
    PASS(1, "已同意"),
    REJECT(2, "已拒绝"),
    BLACKLIST(3, "已拉黑");
    private Integer status;
    private String desc;

    UserContactApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    // 获取状态值
    public Integer getStatus() {
        return status;
    }

    // 获取描述
    public String getDesc() {
        return desc;
    }
    // 根据状态值获取枚举实例
    public static UserContactApplyStatusEnum fromStatus(Integer status) {
        for (UserContactApplyStatusEnum applyStatus : UserContactApplyStatusEnum.values()) {
            if (applyStatus.getStatus().equals(status)) {
                return applyStatus;
            }
        }
        throw new IllegalArgumentException("未知的状态值: " + status);
    }

}
