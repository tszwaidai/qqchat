package com.echat.easychat.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/25 22:19
 */
public class UserConstants {
    // 使用符合 HS256 要求的密钥生成方法
    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 过期日期为7天
}
