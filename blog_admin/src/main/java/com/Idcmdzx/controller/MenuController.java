package com.Idcmdzx.controller;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.service.IMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @GetMapping("/list")
    public ResponseResult listByMenu(String status,String menuName){
        return menuService.listByMenu(status,menuName);
    }

}
