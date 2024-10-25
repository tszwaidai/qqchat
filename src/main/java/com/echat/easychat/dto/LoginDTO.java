package com.echat.easychat.dto;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/25 21:57
 */
@Data
public class LoginDTO {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
}
