package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Menu;
import com.Idcmdzx.domain.vo.MenuVo;
import com.Idcmdzx.mapper.MenuMapper;
import com.Idcmdzx.service.IMenuService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有权限
        if(SecurityUtils.isAdmin()){
            QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
            menuQueryWrapper.in("menu_type", SystemConstants.MENU,SystemConstants.BUTTON);
            menuQueryWrapper.eq("status",SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(menuQueryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        List<MenuVo> menuVos = BeanCopyUtil.copyBeanList(menus, MenuVo.class);

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = builderMenuTree(menuVos,0L);
        return menuTree;
    }

    @Override
    public ResponseResult listByMenu(String status, String menuName) {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.like(StringUtils.hasText(menuName),"menu_name",menuName)
                .eq(StringUtils.hasText(status),"status",status)
                .orderByAsc("order_num")
                .orderByDesc("parent_id");
        List<Menu> list = list(menuQueryWrapper);
        return ResponseResult.okResult(list);
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menuVos, Long parentId) {
        List<MenuVo> menuTree = menuVos.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menuVos)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menuVo
     * @param menuVos
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> childrenList = menuVos.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildren(m,menuVos)))
                .collect(Collectors.toList());
        return childrenList;
    }

}
