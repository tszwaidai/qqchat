package com.echat.easychat.service;

import com.echat.easychat.dto.LoginDTO;
import com.echat.easychat.dto.RegisterDTO;
import com.echat.easychat.dto.Result;
import com.echat.easychat.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-10-24
 */
public interface UserInfoService extends IService<UserInfo> {

    Result getUserList(int currentPage, int pageSize);

    Result getCaptcha();

    Result register(RegisterDTO registerDTO);

    Result login(LoginDTO loginDTO);
}
