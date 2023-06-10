package com.Idcmdzx.mapper;

import com.Idcmdzx.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-12
 */
public interface MenuMapper extends BaseMapper<Menu> {
    @Select("SELECT m.perms from sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m on m.id = rm.menu_id " +
            "WHERE " +
            "ur.user_id = #{id} and " +
            "m.menu_type IN ('C','F') and " +
            "m.status = 0 and " +
            "m.del_flag = 0;")
    List<String> selectPermsByUserId(Long id);

    @Select("SELECT " +
            "DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, IFNULL(m.perms,'') AS perms, m.is_frame, m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_menu m " +
            "WHERE " +
            "m.menu_type IN ('C','M') AND " +
            "m.status = 0 AND " +
            "m.del_flag = 0 " +
            "ORDER BY " +
            "m.parent_id,m.order_num")
    List<Menu> selectAllRouterMenu();

    @Select(("SELECT " +
            "DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, IFNULL(m.perms,'') AS perms, m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id " +
            "WHERE " +
            "ur.user_id = #{userId} AND " +
            "m.`menu_type` IN ('C','M') AND " +
            "m.`status` = 0 AND " +
            "m.`del_flag` = 0 " +
            "ORDER BY " +
            "m.parent_id,m.order_num"))
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
