package com.Idcmdzx.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_role")
@ApiModel(value = "UserRole对象", description = "用户和角色关联表")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("角色ID")
    private Long roleId;


}
