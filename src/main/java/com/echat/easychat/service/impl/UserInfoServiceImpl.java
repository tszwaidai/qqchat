package com.echat.easychat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echat.easychat.dto.LoginDTO;
import com.echat.easychat.dto.RegisterDTO;
import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserInfo;
import com.echat.easychat.enums.UserStatus;
import com.echat.easychat.mapper.UserInfoMapper;
import com.echat.easychat.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echat.easychat.utils.RandomUserId;
import com.pig4cloud.captcha.SpecCaptcha;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.echat.easychat.utils.UserConstants.EXPIRATION_TIME;
import static com.echat.easychat.utils.UserConstants.SECRET_KEY;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-10-24
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 获取验证码
     * @return
     */
    @Override
    public Result getCaptcha() {
        // 生成验证码
        SpecCaptcha captcha = new SpecCaptcha(130, 48);
        captcha.setLen(4); //设置四位字母
        String captchaText = captcha.text();
        String key = UUID.randomUUID().toString();

        // 存入redis设置30分钟过期
        Boolean isStored = stringRedisTemplate.opsForValue().setIfAbsent(key, captchaText, 30, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(isStored)) {
            log.info("验证码已成功存储，key：{}，内容：{}", key, captchaText);
        } else {
            log.warn("验证码存储失败，key：{}，内容：{}", key, captchaText);
        }
        // 返回验证码图片Base64编码和key
        return Result.ok(Map.of("captchaImg",captcha.toBase64(), "captchaKey", key));
    }

    /**
     * 注册
     * @param registerDTO
     * @return
     */
    @Override
    public Result register(RegisterDTO registerDTO) {
        // 验证输入
        if (!StringUtils.hasText(registerDTO.getEmail())||
        !StringUtils.hasText(registerDTO.getNickName())||
        !StringUtils.hasText(registerDTO.getPassword())||
        !StringUtils.hasText(registerDTO.getConfirmPassword())||
        !StringUtils.hasText(registerDTO.getCode())) {
            return Result.fail("所有字段均为必填项！");
        }

        // 校验邮箱格式
        if (!registerDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return Result.fail("邮箱格式不正确");
        }
        // 校验密码
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return Result.fail("密码和确认密码不一致");
        }

        // 验证码校验
        String captchaKey = registerDTO.getCaptchaKey();
        log.info("captchaKey获取到的是{}",captchaKey);
        String storedCaptcha = stringRedisTemplate.opsForValue().get(captchaKey);
        log.info("storedCaptcha是：{}",storedCaptcha);
        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(registerDTO.getCode())) {
            return Result.fail("验证码错误或已过期");
        }

        // 验证成功后删除验证码
        stringRedisTemplate.delete(captchaKey);
        // 检查用户是否已存在
        if (userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                .eq("email", registerDTO.getEmail())) > 0) {
            return Result.fail("该邮箱已被注册");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(RandomUserId.generateRandomUserId()); // 随机形成12位ID
        userInfo.setEmail(registerDTO.getEmail());
        userInfo.setNickName(registerDTO.getNickName());
        userInfo.setPassword(registerDTO.getPassword());
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setLevel("1"); // 账号初始等级为1
        userInfo.setStatus(UserStatus.ENABLED.getValue());
        userInfo.setLastOffTime(LocalDateTime.now());


        userInfoMapper.insert(userInfo);
        // TODO 创建机器人好友


        return Result.ok("注册成功");

    }

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @Override
    public Result login(LoginDTO loginDTO) {
        // 校验邮箱和密码
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            return Result.fail("邮箱和密码均为必填项",400);
        }

        // 根据邮箱查询用户
        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("email", email));
        if (userInfo == null || !userInfo.getPassword().equals(password)) {
            return Result.fail("邮箱或密码错误",401);
        }

        // 生成JWT token
        String token = Jwts.builder()
                .setSubject(userInfo.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();

        // 将 token 存入 Redis，设置过期时间与token一致
        String tokenKey = "token:" + userInfo.getUserId();
        stringRedisTemplate.opsForValue().set(tokenKey, token, EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        // 返回token给前端
        HashMap<String, Object> data = new HashMap<>();
        data.put("userInfo", userInfo);
        data.put("token", token);
        return Result.ok(data);
    }
}
