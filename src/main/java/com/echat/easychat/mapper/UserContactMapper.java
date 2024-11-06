package com.echat.easychat.mapper;

import com.echat.easychat.entity.UserContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 联系人 Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@Mapper
public interface UserContactMapper extends BaseMapper<UserContact> {
    UserContact selectByContactId(String contactId);
}
