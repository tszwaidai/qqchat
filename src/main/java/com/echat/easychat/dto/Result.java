package com.echat.easychat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/24 20:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean success;
    private String errorMsg;
    private Object data;
    private Long total;
    private Integer code;

    public static Result ok(){
        return new Result(true, null, null, null,200);
    }
    public static Result ok(Object data){
        return new Result(true, null, data, null,200);
    }
    public static Result ok(List<?> data, Long total){
        return new Result(true, null, data, total,200);
    }
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null, null,400);
    }

    public static Result fail(String errorMsg, Integer code) {
        return new Result(false,errorMsg,null,null,code);
    }

}
