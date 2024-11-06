package com.echat.easychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("group_info")
public class GroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群ID

     */
    @TableId(value = "group_id", type = IdType.AUTO)
    private String groupId;

    /**
     * 群组名
     */
    private String groupName;

    /**
     * 群主ID
     */
    private String groupOwnerId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 群公告
     */
    private String groupNotice;

    /**
     * 0:直接加入 1:管理员同意后加入
     */
    private Boolean joinType;

    /**
     * 状态 1:正常 0:解散
     */
    private Boolean status;
}
