package com.Idcmdzx.controller;

import com.Idcmdzx.annotation.SystemLog;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.LoginUser;
import com.Idcmdzx.domain.entity.Menu;
import com.Idcmdzx.domain.entity.User;
import com.Idcmdzx.domain.vo.AdminUserInfoVo;
import com.Idcmdzx.domain.vo.MenuVo;
import com.Idcmdzx.domain.vo.RouterVo;
import com.Idcmdzx.domain.vo.UserInfoVo;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.service.IMenuService;
import com.Idcmdzx.service.IRoleService;
import com.Idcmdzx.service.IUserService;
import com.Idcmdzx.service.LoginService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private LoginService loginService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RouterVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RouterVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
