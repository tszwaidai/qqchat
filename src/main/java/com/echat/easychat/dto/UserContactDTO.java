package com.echat.easychat.dto;

import lombok.Data;
import lombok.experimental.StandardException;

import java.time.LocalDateTime;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/13 19:44
 */
@Data
public class UserContactDTO {
    private String userId;
    private String contactId;
    private Integer contactType;
    private LocalDateTime createTime;
    private Integer status;
    private Integer sex;
    private LocalDateTime lastUpdateTime;
    private String nickName;
}
