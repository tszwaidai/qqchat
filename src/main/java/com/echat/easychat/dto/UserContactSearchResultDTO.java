package com.echat.easychat.dto;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/4 19:40
 */
@Data
public class UserContactSearchResultDTO {
    /**
     * 联系人ID或者群组ID
     */
    private String contactId;
    /**
     * 联系人类型 0:好友 1:群组
     */
    private Integer contactType;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 状态 0:非好友 1:好友 2:已删除好友 3:被好友删除 4:已拉黑好友 5:被好友拉黑
     */
    private Integer status;

    /**
     *状态名
     */
    private String statusName;
    /**
     * 性别 0:女 1:男
     */
    private Integer sex;
    /**
     * 地区
     */
    private String areaName;



}
