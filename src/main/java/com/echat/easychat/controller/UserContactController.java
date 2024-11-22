package com.echat.easychat.controller;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.TokenUserInfoDTO;
import com.echat.easychat.enums.UserContactTypeEnum;
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

    /**
     * 获取联系人列表 用户或群组
     * @param
     * @return
     */
    @PostMapping("/loadContact")
    public Result loadContact(@RequestBody Map<String, String> requestData ) {
        String token = requestData.get("token");
        String contactType = requestData.get("contactType");
        return userContactService.loadContact(token,contactType);
    }

    //TODO 获取联系人信息用于点击头像显示信息
    @PostMapping("/getContactInfo")
    public Result getContactInfo(@RequestBody Map<String, String> requestData) {
        String token = requestData.get("token");
        String contactId = requestData.get("contactId");
        return userContactService.getContactInfo(token,contactId);
    }

    /**
     *用户详细信息
     * @param requestData
     * @return
     */
    @PostMapping("/getContactUserInfo")
    public Result getContactUserInfo (@RequestBody Map<String, String> requestData ) {
        String token = requestData.get("token");
        String contactId = requestData.get("contactId");
        return userContactService.getContactUserInfo(token, contactId);
    }

    // TODO 删除拉黑联系人 delContact addContactTOBlackList


}
