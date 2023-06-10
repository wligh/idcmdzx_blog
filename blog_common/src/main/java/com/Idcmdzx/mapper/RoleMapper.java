package com.Idcmdzx.mapper;

import com.Idcmdzx.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-12
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT r.role_key from sys_user_role ur " +
            "LEFT JOIN sys_role r on ur.role_id = r.id " +
            "WHERE " +
            "ur.user_id = #{id} and " +
            "r.status = 0 and " +
            "r.del_flag = 0;")
    List<String> selectRoleKeyByUserId(Long id);

    @Select("select r.id from sys_role r left join sys_user_role ur on ur.role_id = r.id where ur.user_id = #{userId}")
    List<Long> selectRoleIdByUserId(Long userId);
}
