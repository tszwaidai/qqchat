package com.echat.easychat.enums;

/**
 * 联系人状态类型枚举
 */
public enum UserContactStatus {
    NON_FRIEND(0, "非好友"),
    FRIEND(1, "好友"),
    DELETED_FRIEND(2, "已删除好友"),
    FRIEND_DELETED(3, "被好友删除"),
    BLOCKED_FRIEND(4, "已拉黑好友"),
    FRIEND_BLOCKED(5, "被好友拉黑");

    private final int status;
    private final String statusName;

    // 构造方法
    UserContactStatus(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }
    // 根据 status 获取对应的枚举
    public static UserContactStatus getByStatus(int status) {
        for (UserContactStatus statusEnum : UserContactStatus.values()) {
            if (statusEnum.getStatus() == status) {
                return statusEnum;
            }
        }
        return null;  // 如果没有匹配项，返回 null 或者抛出异常
    }
    
}
