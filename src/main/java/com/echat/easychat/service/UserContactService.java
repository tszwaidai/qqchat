package com.echat.easychat.service;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.UserContactSearchResultDTO;
import com.echat.easychat.entity.UserContact;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 联系人 服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
public interface UserContactService extends IService<UserContact> {


    Result search(String contactId);
}
