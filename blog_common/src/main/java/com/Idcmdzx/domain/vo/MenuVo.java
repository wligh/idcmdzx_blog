package com.Idcmdzx.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 链式编程，使set方法有返回
@Accessors(chain = true)
public class MenuVo {

    @ApiModelProperty("菜单ID")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("是否为外链（0是 1否）")
    private Integer isFrame;

    @ApiModelProperty("菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @ApiModelProperty("菜单状态（0显示 1隐藏）")
    private String visible;

    @ApiModelProperty("菜单状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("权限标识")
    private String perms;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String remark;

    private List<MenuVo> children;
}
