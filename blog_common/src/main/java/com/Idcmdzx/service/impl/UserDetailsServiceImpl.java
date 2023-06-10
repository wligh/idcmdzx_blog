package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.mapper.MenuMapper;
import com.Idcmdzx.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.Idcmdzx.domain.entity.LoginUser;
import com.Idcmdzx.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        User userByName = userMapper.selectOne(userQueryWrapper.eq("user_name", username));
        System.out.println(userByName);
        if(Objects.isNull(userByName)){
            throw new RuntimeException("用户名或密码错误!");
        }
        // 如果是后台用户，将权限信息封装进LoginUser
        if(userByName.getType().equals(SystemConstants.ADMIN)){
            List<String> permsByUserId = menuMapper.selectPermsByUserId(userByName.getId());
            return new LoginUser(userByName,permsByUserId);
        }
        return new LoginUser(userByName);
    }
}
