package com.Idcmdzx.mapper;

import com.Idcmdzx.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-05
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT id,view_count from sg_article;")
    List<Map<String,Object>> selectViewCount();

}
