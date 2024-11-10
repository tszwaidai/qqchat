package com.echat.easychat.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/9 14:46
 */

public class BusinessException extends RuntimeException{
    private int code;
    private String message;

    // 构造方法
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;  // 默认错误代码
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
