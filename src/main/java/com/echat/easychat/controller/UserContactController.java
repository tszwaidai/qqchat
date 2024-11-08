package com.echat.easychat.controller;

import com.echat.easychat.dto.Result;
import com.echat.easychat.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
