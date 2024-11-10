package com.echat.easychat.dto;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: 登录用户信息
 * @date 2024/11/8 17:24
 */
@Data
public class TokenUserInfoDTO {
    private String token;
    private String userId;
    private String nickName;

}
