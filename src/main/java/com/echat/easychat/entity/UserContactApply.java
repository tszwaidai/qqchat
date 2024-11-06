package com.echat.easychat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("user_contact_apply")
public class UserContactApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;

    /**
     * 申请人ID
     */
    private String applyUserId;

    /**
     * 接收人ID
     */
    private String receiveUserId;

    /**
     * 联系人类型 0:好友 1:群组
     */
    private Boolean contactType;

    /**
     * 联系人群组ID
     */
    private String contactId;

    /**
     * 最后申请时间
     */
    private Long lastApplyTime;

    /**
     * 状态0:待处理 1:已同意 2:已拒绝 3:已拉黑
     */
    private Boolean status;

    /**
     * 申请信息
     */
    private String applyInfo;
}
