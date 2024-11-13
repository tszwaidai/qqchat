package com.echat.easychat.mapper;

import com.echat.easychat.entity.UserContactApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@Mapper
public interface UserContactApplyMapper extends BaseMapper<UserContactApply> {

    @Select("SELECT a.apply_id, a.apply_user_id, a.receive_user_id, a.contact_type, " +
            "a.contact_id, a.last_apply_time, a.status, a.apply_info, " +
            "u.nick_name AS contact_name " +
            "FROM user_contact_apply a " +
            "LEFT JOIN user_info u ON a.apply_user_id = u.user_id " +
            "WHERE a.receive_user_id = #{receiveUserId} " +
            "LIMIT #{pageNo}, #{pageSize}")
    List<UserContactApply> selectApplyListWithUserInfo(UserContactApply userContactApply);

    // TODO 待验证
    @Update("<script>" +
            "UPDATE user_contact_apply " +
            "SET status = #{updateInfo.status}, " +
            "last_apply_time = #{updateInfo.lastApplyTime} " +
            "WHERE apply_id = #{applyQuery.applyId} " +
            "<if test='applyQuery.status != null'>" +
            "AND status = #{applyQuery.status} " +
            "</if>" +
            "</script>")
    Integer updateByParam(@Param("updateInfo") UserContactApply updateInfo, @Param("applyQuery") UserContactApply applyQuery);
}
