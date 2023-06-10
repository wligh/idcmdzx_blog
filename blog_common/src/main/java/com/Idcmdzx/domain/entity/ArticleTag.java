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
 * 文章标签关联表
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article_tag")
@ApiModel(value = "ArticleTag对象", description = "文章标签关联表")
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @ApiModelProperty("标签id")
    private Long tagId;


}
