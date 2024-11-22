package com.echat.easychat.vo;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/18 00:21
 */
@Data
public class UserInfoVO {
    private String userId;
    private String nickName;
    private Integer sex;
    private Integer joinType;
    private String personalSignature;
    private String areaName;
    private String areaCode;
    private String level;
}
