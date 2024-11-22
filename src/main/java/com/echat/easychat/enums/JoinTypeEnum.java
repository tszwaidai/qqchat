package com.echat.easychat.enums;

public enum JoinTypeEnum {
    JOIN(0,"直接加入"),
    AGREE(1,"同意后加入");
    private Integer status;
    private String desc;
    public Integer getStatus() {
        return status;
    }
    JoinTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public static JoinTypeEnum getByStatus(Integer status) {
        for (JoinTypeEnum contactTypes : JoinTypeEnum.values()) {
            if (contactTypes.getStatus().equals(status)) {
                return contactTypes;
            }
        }
        throw new IllegalArgumentException("未知的状态值: " + status);
    }

}
