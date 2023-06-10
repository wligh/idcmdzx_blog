package com.Idcmdzx.service.impl;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.LoginUser;
import com.Idcmdzx.domain.entity.User;
import com.Idcmdzx.domain.vo.UserInfoVo;
import com.Idcmdzx.service.LoginService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.JwtUtil;
import com.Idcmdzx.utils.RedisCache;
import com.Idcmdzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword(), null);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        redisCache.setCacheObject("login:"+id,loginUser);
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(loginUser.getUser(), UserInfoVo.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token",jwt);
        hashMap.put("userInfo",userInfoVo);
        return ResponseResult.okResult(hashMap);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
