package com.echat.easychat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.TokenUserInfoDTO;
import com.echat.easychat.dto.UserContactSearchResultDTO;
import com.echat.easychat.entity.UserContact;
import com.echat.easychat.entity.UserContactApply;
import com.echat.easychat.entity.UserInfo;
import com.echat.easychat.enums.JoinTypeEnum;
import com.echat.easychat.enums.TypeEnum;
import com.echat.easychat.enums.UserContactApplyStatusEnum;
import com.echat.easychat.enums.UserContactStatusEnum;
import com.echat.easychat.exception.BusinessException;
import com.echat.easychat.mapper.UserContactApplyMapper;
import com.echat.easychat.mapper.UserContactMapper;
import com.echat.easychat.mapper.UserInfoMapper;
import com.echat.easychat.service.UserContactApplyService;
import com.echat.easychat.service.UserContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echat.easychat.utils.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.echat.easychat.utils.UserConstants.SECRET_KEY;

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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserContactApplyMapper userContactApplyMapper;
    @Resource
    private UserContactApplyService userContactApplyService;

    /**
     * 搜索联系人
     * @param contactId
     * @return
     */
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
        resultDTO.setContactType(TypeEnum.USER.getStatus()); //代表用户
        resultDTO.setNickName(userInfo.getNickName());
        resultDTO.setSex(userInfo.getSex() ? 1 : 0);
        resultDTO.setAreaName(userInfo.getAreaName());
        //如果从联系表中查找当前登录用户和搜索用户没有关系
        if (userContact == null) {
            resultDTO.setStatus(UserContactStatusEnum.NON_FRIEND.getStatus());
        }
//        resultDTO.setStatus(UserContactStatusEnum.FRIEND.getStatus());

//        UserContactStatusEnum contactStatus = UserContactStatusEnum.getByStatus(userContact.getStatus());
//        if (contactStatus != null) {
//            resultDTO.setStatusName(contactStatus.getStatusName());
//        }

        return Result.ok(resultDTO);
    }

    /**
     * 添加联系人
     *
     * @param contactId
     * @param applyInfo
     * @return
     */
    @ExceptionHandler
    @Override
    public Result applyAdd(String token,String contactId, String applyInfo) {
//        String applyUserId = UserContext.getUserId();
        log.info("Token: {}", token);
        log.info("contactId: {}", contactId);
        log.info("applyInfo: {}",applyInfo);
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 从 token 中获取 userId
        String applyUserId = claims.getSubject(); // 假设 token 的 `sub` 字段是 userId

        if (applyUserId == null) {
            return Result.fail("用户信息未找到，请重新登录", 404);
        }
        log.info("从登录信息获取到的申请人ID:{}",applyUserId);
        // 默认申请信息
        if (applyInfo == null || applyInfo.isEmpty()) {
            applyInfo = "我是 " + UserContext.getNickName();
        }
        log.info("发送的申请信息是：{}",applyInfo);

        String receiveUserId = contactId;
        // 查询对方好友是否已经添加，如果已经拉黑无法添加
        UserContact userContact = userContactMapper.selectOne(new QueryWrapper<UserContact>()
                .eq("user_id",applyUserId)
                .eq("contact_id",contactId)
        );
        if (userContact != null && userContact.getStatus().equals(UserContactStatusEnum.FRIEND_BLOCKED.getStatus())) {
            throw new BusinessException(403,"对方已经将你拉黑，无法添加");
        }

        UserInfo user = userInfoMapper.selectById(contactId);
        Integer joinType = user.getJoinType();
        log.info("查到的用户信息：{}",user);
        log.info("用户的加入方式：{}",joinType);
        // TODO 加入群聊
        // TODO 直接加入好友不用记录申请记录 后续要修改
        if (JoinTypeEnum.JOIN.getStatus().equals(joinType)) {
            userContactApplyService.addContact(applyUserId,receiveUserId,contactId,0,applyInfo);
            return Result.ok();
        }

        // TODO 这是需要经过对方同意的 需要测试
        UserContactApply dbApply = userContactApplyMapper.selectOne(new QueryWrapper<UserContactApply>()
                .eq("apply_user_id",applyUserId)
                .eq("receive_user_id",receiveUserId)
                .eq("contact_id",contactId)
        );
        if (dbApply == null) {
            UserContactApply contactApply = new UserContactApply();
            contactApply.setApplyUserId(applyUserId);
            // TODO 后续要修改 加上群组
            contactApply.setContactType(0);
            contactApply.setReceiveUserId(receiveUserId);
            contactApply.setLastApplyTime(LocalDateTime.now());
            contactApply.setContactId(contactId);
            contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            contactApply.setApplyInfo(applyInfo);
            userContactApplyMapper.insert(contactApply);
        } else {
            // 更新状态
            UserContactApply contactApply = new UserContactApply();
            contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            contactApply.setLastApplyTime(LocalDateTime.now());
            contactApply.setApplyInfo(applyInfo);
            userContactApplyMapper.updateById(dbApply);
        }

        if (dbApply == null ||dbApply.getStatus().equals(UserContactApplyStatusEnum.INIT.getStatus())) {
            // TODO 发送ws消息
        }

        return Result.ok("申请发送成功");
    }

}
