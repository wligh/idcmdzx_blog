package com.Idcmdzx.controller;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.UserChangeDto;
import com.Idcmdzx.domain.dto.UserDto;
import com.Idcmdzx.domain.entity.Role;
import com.Idcmdzx.domain.entity.User;
import com.Idcmdzx.domain.vo.UserInfoAndRoleIdsVo;
import com.Idcmdzx.service.IRoleService;
import com.Idcmdzx.service.IUserService;
import com.Idcmdzx.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/user")
public class AdminUserController {

    @Resource
    private IUserService userService;

    @Resource
    private IRoleService roleService;

    @GetMapping("/list")
    public ResponseResult listByUser(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.listByUser(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult saveUser(@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable Long userId) {
        //所有角色列表
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserChangeDto userChangeDto){
        return userService.changeStatus(userChangeDto);
    }
}
