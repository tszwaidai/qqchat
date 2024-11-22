package com.echat.easychat.service.impl;

import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserContact;
import com.echat.easychat.entity.UserContactApply;
import com.echat.easychat.enums.UserContactTypeEnum;
import com.echat.easychat.enums.UserContactApplyStatusEnum;
import com.echat.easychat.enums.UserContactStatusEnum;
import com.echat.easychat.exception.BusinessException;
import com.echat.easychat.mapper.UserContactApplyMapper;
import com.echat.easychat.mapper.UserContactMapper;
import com.echat.easychat.service.UserContactApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.echat.easychat.utils.UserConstants.SECRET_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@Slf4j
@Service
public class UserContactApplyServiceImpl extends ServiceImpl<UserContactApplyMapper, UserContactApply> implements UserContactApplyService {

    @Autowired
    private UserContactApplyMapper userContactApplyMapper;
    @Autowired
    private UserContactMapper userContactMapper;

    /**
     * 获取好友申请列表
     * @param pageNo
     * @return
     */
    @Override
    public Result loadApply(String token,Integer pageNo) {
        log.info("Token: {}", token);
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 从 token 中获取 userId
        String userId = claims.getSubject(); // 假设 token 的 `sub` 字段是 userId
        log.info("当前的userId是：{}",userId);
        UserContactApply userContactApply = new UserContactApply();
//        userContactApply.setOrderBy("last_apply_time desc");
        userContactApply.setReceiveUserId(userId);
        userContactApply.setPageNo((pageNo - 1) * 10); // 计算偏移量
        // TODO 后续要修改页数大小
        userContactApply.setPageSize(10);

        //连表查询
        List<UserContactApply> applyList = userContactApplyMapper.selectApplyListWithUserInfo(userContactApply);

        return Result.ok(applyList);
    }

    /**
     * 处理好友申请
     * @param userId
     * @param applyId
     * @param status
     * @return
     */
    @Override
    // TODO 要加事务
    public Result dealWithApply(String userId, Integer applyId, Integer status) {
        UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getByStatus(status);
        if (statusEnum == null || UserContactApplyStatusEnum.INIT == statusEnum) {
            throw new BusinessException(600,"无效的状态");
        }
        UserContactApply applyInfo = userContactApplyMapper.selectById(applyId);
        if (applyInfo == null || !userId.equals(applyInfo.getReceiveUserId())) {
            throw new BusinessException(600,"无效的申请ID或申请不属于当前用户");
        }
        //更新状态
        UserContactApply updateInfo = new UserContactApply();
        updateInfo.setStatus(statusEnum.getStatus());
        updateInfo.setLastApplyTime(LocalDateTime.now());

        UserContactApply applyQuery = new UserContactApply();
        applyQuery.setApplyId(applyId);
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());

        // TODO 需要测试
        Integer count = userContactApplyMapper.updateByParam(updateInfo, applyQuery);
        if (count == 0) {
            throw new BusinessException(600,"更新申请状态失败");
        }

        // 同意后加入
        if (UserContactApplyStatusEnum.PASS.getStatus().equals(status)) {
            // TODO 添加联系人
            addContact(applyInfo.getApplyUserId(),applyInfo.getReceiveUserId(),applyInfo.getContactId(),applyInfo.getContactType(),applyInfo.getApplyInfo());
            return null;
        }

        // TODO 需要测试 拉黑联系人
        if (UserContactApplyStatusEnum.BLACKLIST == statusEnum) {
            UserContact userContact = new UserContact();
            userContact.setUserId(applyInfo.getApplyUserId());
            userContact.setContactId(applyInfo.getContactId());
            userContact.setContactType(applyInfo.getContactType()); // 是好友还是群组
            userContact.setCreateTime(LocalDateTime.now());
            userContact.setStatus(UserContactStatusEnum.BLOCKED_FRIEND.getStatus());
            userContact.setLastUpdateTime(LocalDateTime.now());
            userContactMapper.insertOrUpdate(userContact); //TODO 新增或修改
        }
        return null;

    }

    @Override
    public void addContact(String applyUserId, String receiveUSerId, String contactId, Integer contactType, String applyInfo) {
        // TODO 判断群聊人数

        //同意 双方添加好友
        List<UserContact> contactList = new ArrayList<>();
        //申请人添加对方
        UserContact userContact = new UserContact();
        userContact.setUserId(applyUserId);
        userContact.setContactId(contactId);
        userContact.setContactType(contactType);
        userContact.setCreateTime(LocalDateTime.now());
        userContact.setLastUpdateTime(LocalDateTime.now());
        userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        contactList.add(userContact);
        // 如果是申请好友，接受人添加申请人，群组不用添加对方为好友
        if (UserContactTypeEnum.USER.getStatus().equals(contactType)) {
            UserContact userContact1 = new UserContact();
            userContact1.setUserId(applyUserId);
            userContact1.setContactId(contactId);
            userContact1.setContactType(contactType);
            userContact1.setCreateTime(LocalDateTime.now());
            userContact1.setLastUpdateTime(LocalDateTime.now());
            userContact1.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            contactList.add(userContact1);
        }
        // 批量插入
        userContactMapper.insertOrUpdate(contactList);
        //TODO 如果是好友 接受人也添加申请人为好友 添加缓存

        //TODO 创建会话

    }
}
