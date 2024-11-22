package com.echat.easychat.mapper;

import com.echat.easychat.entity.UserContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 联系人 Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-06
 */
@Mapper
public interface UserContactMapper extends BaseMapper<UserContact> {
    @Select("SELECT user_id, contact_id, contact_type, create_time, status, last_update_time " +
            "FROM user_contact " +
            "WHERE user_id = #{userId} AND contact_type = #{contactType}")
    List<UserContact> selectByUserIdAndContactType(@Param("userId") String userId, @Param("contactType") Integer contactType);
}
