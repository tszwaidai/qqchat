package com.echat.easychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 联系人
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_contact")
public class UserContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 联系人ID或者群组ID
     */
    @TableId(value = "contact_id", type = IdType.AUTO)
    private String contactId;

    /**
     * 联系人类型 0:好友 1:群组
     */
    private Integer contactType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 状态 0:非好友 1:好友 2:已删除好友 3:被好友删除 4:已拉黑好友 5:被好友拉黑
     */
    private Integer status;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;
}
