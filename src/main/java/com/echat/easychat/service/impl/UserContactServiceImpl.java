package com.echat.easychat.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.UserContactDTO;
import com.echat.easychat.dto.UserContactSearchResultDTO;
import com.echat.easychat.entity.UserContact;
import com.echat.easychat.entity.UserContactApply;
import com.echat.easychat.entity.UserInfo;
import com.echat.easychat.enums.JoinTypeEnum;
import com.echat.easychat.enums.UserContactTypeEnum;
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
import com.echat.easychat.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        resultDTO.setContactType(UserContactTypeEnum.USER.getStatus()); //代表用户
        resultDTO.setNickName(userInfo.getNickName());
        resultDTO.setSex(userInfo.getSex());
//        resultDTO.setAreaName(userInfo.getAreaName());
        // 替换 areaName 中的逗号为一个空格
        resultDTO.setAreaName(userInfo.getAreaName());
        //如果从联系表中查找当前登录用户和搜索用户没有关系
        if (userContact == null) {
            resultDTO.setStatus(UserContactStatusEnum.NON_FRIEND.getStatus());
        }

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

    /**
     * 获取联系人列表
     * @param contactType
     * @return
     */
    @Override
    public Result loadContact(String token,String contactType) {
        UserContactTypeEnum contactTypeEnum = UserContactTypeEnum.getByName(contactType);
        log.info("当前联系人类型: {}",contactTypeEnum);
        if (contactTypeEnum == null) {
            throw new BusinessException(600,"无效的联系人类型");
        }
        // 获取当前用户ID
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 从 token 中获取 userId
        String userId = claims.getSubject(); // 假设 token 的 `sub` 字段是 userId

        log.info("获取到的当前用户ID：{}",userId);
        // 查询数据库获取联系人列表
        List<UserContact> userContacts = userContactMapper.selectByUserIdAndContactType(userId, contactTypeEnum.getStatus());
        List<UserContactDTO> contactDTOList = new ArrayList<>();
        for (UserContact userContact : userContacts) {
            UserContactDTO contactDTO = new UserContactDTO();
            contactDTO.setContactId(userContact.getContactId());
            contactDTO.setContactType(userContact.getContactType());
            contactDTO.setStatus(userContact.getStatus());
            contactDTO.setCreateTime(userContact.getCreateTime());
            contactDTO.setLastUpdateTime(userContact.getLastUpdateTime());

            UserInfo userInfo = userInfoMapper.selectById(userContact.getContactId());
            if (userInfo != null) {
                contactDTO.setNickName(userInfo.getNickName());
                //contactDTO.setSex();
            }
            contactDTOList.add(contactDTO);
        }
        return Result.ok(contactDTOList);

    }

    /**
     *获取用户信息 非好友也可以知道
     * @param token
     * @param contactId
     * @return
     */
    @Override
    public Result getContactInfo(String token, String contactId) {
        // 获取当前用户ID
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 从 token 中获取 userId
        String userId = claims.getSubject(); //  token 的 `sub` 字段是 userId

        return null;
    }



    /**
     *获取用户详细信息 只有是好友才能知道
     * @param token
     * @param contactId
     * @return
     */
    @Override
    public Result getContactUserInfo(String token, String contactId) {
        // 获取当前用户ID
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 从 token 中获取 userId
        String userId = claims.getSubject(); //  token 的 `sub` 字段是 userId
        UserContact userContact = userContactMapper.selectOne(new QueryWrapper<UserContact>()
                .eq("user_id",userId)
                .eq("contact_id",contactId)
        );
        log.info("查询到当前的登录用户的联系人信息:{}", JSON.toJSONString(userContact));
        if (userContact == null ) {
            return Result.fail("联系人不存在或没有权限查看");
        }
        UserInfoVO userInfoVO = userInfoMapper.selectByContactId(contactId);
        userInfoVO.setUserId(userInfoVO.getUserId());
        userInfoVO.setSex(userInfoVO.getSex());
        userInfoVO.setJoinType(userInfoVO.getJoinType());
        userInfoVO.setLevel(userInfoVO.getLevel());
        userInfoVO.setNickName(userInfoVO.getNickName());
        // 替换 areaName 中的逗号为一个空格
        if (userInfoVO.getAreaName() != null) {
            String areaNameWithSpaces = userInfoVO.getAreaName().replace(",", " ");
            userInfoVO.setAreaName(areaNameWithSpaces);
        }
        userInfoVO.setAreaCode(userInfoVO.getAreaCode());
        userInfoVO.setPersonalSignature(userInfoVO.getPersonalSignature());

        return Result.ok(userInfoVO);
    }


}
