package com.echat.easychat.controller;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.TokenUserInfoDTO;
import com.echat.easychat.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 联系人 前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-06
 */
@RestController
@RequestMapping("/userContact")
public class UserContactController {

    @Autowired
    private UserContactService userContactService;

    /**
     * 搜索联系人
     * @param params
     * @return
     */
    // TODO 加上群ID
    @PostMapping("/search")
    public Result search(@RequestBody Map<String, Object> params ){
        String contactId = (String) params.get("contactId");
        System.out.println("Received contactId: " + contactId);
        return userContactService.search(contactId);
    }

    /**
     * 添加联系人
     * @param
     * @param
     * @return
     */
    @PostMapping("/applyAdd")
    public Result applyAdd(@RequestBody Map<String, String> requestData) {
        String token = requestData.get("token");
        String contactId = requestData.get("contactId");
        String applyInfo = requestData.get("applyInfo");
        return userContactService.applyAdd(token,contactId, applyInfo);
    }

}
