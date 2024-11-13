package com.echat.easychat.controller;

import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserContact;
import com.echat.easychat.service.UserContactApplyService;
import com.echat.easychat.utils.UserContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@Slf4j
@RestController
@RequestMapping("/userContactApply")
public class UserContactApplyController {
    @Autowired
    private UserContactApplyService userContactApplyService;

    /**
     * 获取好友申请列表
     * @param pageNo
     * @return
     */
    @PostMapping("/loadApply")
    public Result loadApply(String token, Integer pageNo) {
        return userContactApplyService.loadApply(token,pageNo);
    }

    /**
     * 处理好友申请
     * @param userId
     * @param applyId
     * @param status
     * @return
     */
    @PostMapping("/dealWithApply")
    public Result dealWithApply(String userId,Integer applyId, Integer status) {
        userId = UserContext.getUserId();
        log.info("当前token的用户ID：{}",userId);
        return userContactApplyService.dealWithApply(userId, applyId, status);
    }
}
