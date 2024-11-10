package com.echat.easychat.utils;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/9 17:47
 */
public class UserContext {
    private static ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static ThreadLocal<String> currentNickName = new ThreadLocal<>();
    public static void setUserId(String userId) {
        currentUser.set(userId);
    }

    // 设置 nickName
    public static void setNickName(String nickName) {
        currentNickName.set(nickName);
    }

    // 获取 nickName
    public static String getNickName() {
        return currentNickName.get();
    }

    public static String getUserId() {
        return currentUser.get();
    }


    public static void clear() {
        currentUser.remove();
        currentNickName.remove();
    }
}
