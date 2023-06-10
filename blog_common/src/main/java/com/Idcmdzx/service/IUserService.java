package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.UserChangeDto;
import com.Idcmdzx.domain.dto.UserDto;
import com.Idcmdzx.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
public interface IUserService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listByUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult saveUser(UserDto userDto);

    void updateUser(UserDto userDto);

    ResponseResult changeStatus(UserChangeDto userChangeDto);
}
