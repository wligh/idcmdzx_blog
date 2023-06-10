package com.Idcmdzx.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("sys_role")
@ApiModel(value = "RoleVo对象", description = "角色信息Vo表")
public class RoleVo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色权限字符串")
    private String roleKey;

    @ApiModelProperty("显示顺序")
    private Integer roleSort;

    @ApiModelProperty("角色状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("备注")
    private String remark;


}
