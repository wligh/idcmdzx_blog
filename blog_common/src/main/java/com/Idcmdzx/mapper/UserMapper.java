package com.Idcmdzx.mapper;

import com.Idcmdzx.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
