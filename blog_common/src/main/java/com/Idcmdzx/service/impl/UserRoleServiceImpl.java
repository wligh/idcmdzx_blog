package com.Idcmdzx.service.impl;

import com.Idcmdzx.domain.entity.UserRole;
import com.Idcmdzx.mapper.UserRoleMapper;
import com.Idcmdzx.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
