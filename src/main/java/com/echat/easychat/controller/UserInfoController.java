package com.echat.easychat.controller;

import com.echat.easychat.dto.LoginDTO;
import com.echat.easychat.dto.RegisterDTO;
import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserInfo;
import com.echat.easychat.service.UserInfoService;
import com.echat.easychat.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Map;


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
@Validated
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
    @SneakyThrows
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userInfoService.login(loginDTO);
    }

    /**
     *获取当前登录用户信息
     * @param requestData
     * @return
     */
    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody Map<String , String> requestData) {
        String token = requestData.get("token");
        return userInfoService.getUserInfo(token);
    }

    /**
     * 修改用户信息并保存
     * @return
     */
    @PostMapping("/saveUserInfo")
    public Result saveUserInfo(@RequestPart("token") String token,
                               @RequestPart("userInfo") String userInfoJson,
                               @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
                               @RequestPart(value = "avatarCover", required = false) MultipartFile avatarCover) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = objectMapper.readValue(userInfoJson, UserInfo.class);
        return userInfoService.saveUserInfo(token,userInfo,avatarFile,avatarCover);
    }



    /**
     *修改密码
     * @param token
     * @param password
     * @return
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(String token, @Pattern(regexp = Constants.REGEX_PASSWORD) String password) {
        return userInfoService.updatePassword(token, password);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        return userInfoService.logout();
    }


}
