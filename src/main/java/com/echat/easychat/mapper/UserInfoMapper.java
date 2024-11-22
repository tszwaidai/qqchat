package com.echat.easychat.mapper;

import com.echat.easychat.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.echat.easychat.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-10-24
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT user_id, nick_name, sex,join_type, personal_signature, area_name, area_code,level FROM user_info WHERE user_id = #{contactId}")
    UserInfoVO selectByContactId(String contactId);
}
