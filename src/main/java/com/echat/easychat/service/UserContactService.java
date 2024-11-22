package com.echat.easychat.service;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.TokenUserInfoDTO;
import com.echat.easychat.entity.UserContact;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 联系人 服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-06
 */
public interface UserContactService extends IService<UserContact> {

    Result search(String contactId);


    Result applyAdd(String token, String contactId, String applyInfo);

    Result loadContact(String token,String contactType);

    Result getContactUserInfo(String token, String contactId);

    Result getContactInfo(String token, String contactId);
}
