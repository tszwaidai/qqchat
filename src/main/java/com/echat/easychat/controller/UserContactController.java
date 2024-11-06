package com.echat.easychat.controller;

import com.echat.easychat.dto.Result;
import com.echat.easychat.dto.UserContactSearchResultDTO;
import com.echat.easychat.service.UserContactService;
import com.echat.easychat.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 联系人 前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/userContact")
public class UserContactController {


    @Autowired
    private UserContactService userContactService;

    /**
     * 搜索联系人
     * @param
     * @return
     */
    // TODO 加上群ID
    @PostMapping("/search")
    public Result search(@RequestBody Map<String, Object> params ){
        String contactId = (String) params.get("contactId");
        System.out.println("Received contactId: " + contactId);
        return userContactService.search(contactId);
    }




}
