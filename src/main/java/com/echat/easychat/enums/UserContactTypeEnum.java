package com.echat.easychat.enums;

public enum UserContactTypeEnum {
    USER(0,"用户"),
    GROUP(1,"群组");
    private Integer status;
    private String desc;
    public Integer getStatus() {
        return status;
    }
    UserContactTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public static UserContactTypeEnum getByStatus(Integer status) {
        for (UserContactTypeEnum types : UserContactTypeEnum.values()) {
            if (types.getStatus().equals(status)) {
                return types;
            }
        }
        throw new IllegalArgumentException("未知的状态值: " + status);
    }
    public static UserContactTypeEnum getByName(String desc) {
        for (UserContactTypeEnum type : UserContactTypeEnum.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的描述值: " + desc);
    }

}
