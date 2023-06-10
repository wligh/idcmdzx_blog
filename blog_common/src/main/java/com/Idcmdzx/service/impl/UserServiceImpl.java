package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.UserChangeDto;
import com.Idcmdzx.domain.dto.UserDto;
import com.Idcmdzx.domain.entity.LoginUser;
import com.Idcmdzx.domain.entity.User;
import com.Idcmdzx.domain.entity.UserRole;
import com.Idcmdzx.domain.vo.PageVo;
import com.Idcmdzx.domain.vo.UserInfoVo;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.mapper.UserMapper;
import com.Idcmdzx.service.IUserRoleService;
import com.Idcmdzx.service.IUserService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.JwtUtil;
import com.Idcmdzx.utils.RedisCache;
import com.Idcmdzx.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private IUserRoleService userRoleService;

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
        redisCache.setCacheObject("blogLogin:"+id,loginUser);
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(loginUser.getUser(), UserInfoVo.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token",jwt);
        hashMap.put("userInfo",userInfoVo);
        return ResponseResult.okResult(hashMap);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        redisCache.deleteObject("blogLogin:" + id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User userById = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtil.copyBean(userById, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(checkNickNameUnique(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like(StringUtils.hasText(userName),"user_name",userName)
                .like(StringUtils.hasText(phonenumber),"phonenumber",phonenumber)
                .eq(StringUtils.hasText(status),"status",status);
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage,userQueryWrapper);
        List<UserInfoVo> userInfoVos = BeanCopyUtil.copyBeanList(userPage.getRecords(), UserInfoVo.class);
        return ResponseResult.okResult(new PageVo(userInfoVos,userPage.getTotal()));
    }

    @Override
    public ResponseResult saveUser(UserDto userDto) {
        if(!StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(!StringUtils.hasText(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }

        if(checkUserNameUnique(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(checkPhoneUnique(userDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(checkEmailUnique(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = BeanCopyUtil.copyBean(userDto, User.class);
        save(user);

        User ByUserPhoneNumber = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, userDto.getPhonenumber()));

        if(userDto.getRoleIds()!=null&&userDto.getRoleIds().size()>0){
            insertUserRole(userDto,ByUserPhoneNumber.getId());
        }
        return ResponseResult.okResult();
    }

    private void insertUserRole(UserDto userDto,Long id) {
        List<UserRole> userRoleList = userDto.getRoleIds().stream()
                .map(userRoleId -> new UserRole(id, userRoleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {

        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,userDto.getId());
        userRoleService.remove(userRoleUpdateWrapper);


        // 新增用户与角色管理
        insertUserRole(userDto, userDto.getId());

        User user = BeanCopyUtil.copyBean(userDto, User.class);
        // 更新用户信息
        updateById(user);
    }

    @Override
    @Transactional
    public ResponseResult changeStatus(UserChangeDto userChangeDto) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id",userChangeDto.getUserId());
        userUpdateWrapper.set("status",userChangeDto.getStatus());
        update(null,userUpdateWrapper);
        return ResponseResult.okResult();
    }


    private boolean checkNickNameUnique(String nickName){
        return count(Wrappers.<User>lambdaQuery().eq(User::getNickName,nickName)) > 0;
    }

    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName)) > 0;
    }

    public boolean checkPhoneUnique(String phoneNumber) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,phoneNumber)) > 0;
    }

    public boolean checkEmailUnique(String email) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,email)) > 0;
    }

}
