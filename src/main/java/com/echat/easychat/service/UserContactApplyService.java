package com.echat.easychat.service;

import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserContactApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
public interface UserContactApplyService extends IService<UserContactApply> {

    Result loadApply(String token,Integer pageNo);

    Result dealWithApply(String userId, Integer applyId, Integer status);

    void addContact(String applyUserId, String receiveUSerId, String contactId, Integer contactType, String applyInfo);
}
