package com.echat.easychat.enums;

public enum TypeEnum {
    USER(0,"用户"),
    GROUP(1,"群组");
    private Integer status;
    private String desc;
    public Integer getStatus() {
        return status;
    }
    TypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public static TypeEnum fromStatus(Integer status) {
        for (TypeEnum types : TypeEnum.values()) {
            if (types.getStatus().equals(status)) {
                return types;
            }
        }
        throw new IllegalArgumentException("未知的状态值: " + status);
    }

}
