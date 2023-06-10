package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-12
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listAllRole();

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}
