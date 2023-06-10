package com.Idcmdzx.controller;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.service.IRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }

}
