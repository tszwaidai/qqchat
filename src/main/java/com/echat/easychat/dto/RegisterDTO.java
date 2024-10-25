package com.echat.easychat.dto;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/25 15:45
 */
@Data
public class RegisterDTO {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 密码
     */
    private String password;
    /**
     * 确认密码
     */
    private String confirmPassword;
    /**
     * 用户输入的验证码
     */
    private String code;
    /**
     * 验证码的key，用于从Redis获取对应验证码
     */
    private String captchaKey;
}
