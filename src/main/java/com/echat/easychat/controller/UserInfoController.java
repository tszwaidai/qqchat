package com.echat.easychat.controller;

import com.echat.easychat.dto.LoginDTO;
import com.echat.easychat.dto.RegisterDTO;
import com.echat.easychat.dto.Result;
import com.echat.easychat.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/captcha")
    public Result getCaptcha() {
        return userInfoService.getCaptcha();
    }

    /**
     * 注册功能
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return userInfoService.register(registerDTO);
    }

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userInfoService.login(loginDTO);
    }



}
