package com.Idcmdzx.controller;

import com.Idcmdzx.annotation.SystemLog;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.User;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return userService.logout();
    }

    @PostMapping("/user/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/user/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/user/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

}
