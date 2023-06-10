package com.Idcmdzx.utils;

import com.Idcmdzx.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthenticate().getPrincipal();
    }

    /**
     * 从SecurityContextHolder获取Authenticate
     */
    public static Authentication getAuthenticate() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录id
     */
    public static Long getUserId(){
        return getLoginUser().getUser().getId();
    }

    /**
     * 判断当前用户是否为管理员
     */
    public static Boolean isAdmin(){
        Long userId = getUserId();
        return userId != null && userId.equals(1L);
    }
}
