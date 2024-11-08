package com.echat.easychat.service.impl;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.UserContactSearchResultDTO;
import com.echat.easychat.entity.UserContact;
import com.echat.easychat.entity.UserInfo;
import com.echat.easychat.enums.UserContactStatus;
import com.echat.easychat.mapper.UserContactMapper;
import com.echat.easychat.mapper.UserInfoMapper;
import com.echat.easychat.service.UserContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 联系人 服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-06
 */
@Slf4j
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact> implements UserContactService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserContactMapper userContactMapper;
    @Override
    public Result search(String contactId) {
        UserContact userContact = userContactMapper.selectById(contactId);
        UserInfo userInfo = userInfoMapper.selectById(contactId);
        // 从联系表中查找当前登录用户和搜索用户的关系

        log.info("查到的用户信息：{}",userInfo.toString());
//        log.info("查到的用户和联系人关系信息：{}",userContact.toString());
        if (userInfo == null) {
            return Result.fail("用户未找到");
        }
        UserContactSearchResultDTO resultDTO = new UserContactSearchResultDTO();
        resultDTO.setContactId(userInfo.getUserId());
        resultDTO.setContactType(userContact.getContactType()); //代表好友
        resultDTO.setNickName(userInfo.getNickName());
        resultDTO.setSex(userInfo.getSex() ? 1 : 0);
        resultDTO.setAreaName(userInfo.getAreaName());
        resultDTO.setStatus(userContact.getStatus());

        UserContactStatus contactStatus = UserContactStatus.getByStatus(userContact.getStatus());
        if (contactStatus != null) {
            resultDTO.setStatusName(contactStatus.getStatusName());
        }

        return Result.ok(resultDTO);
    }
}
