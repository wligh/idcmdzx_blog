package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.AddArticleDto;
import com.Idcmdzx.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-05
 */
public interface IArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addOrUpdateArticle(AddArticleDto article);

    ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult deleteArticle(Long id);

    ResponseResult getByArticle(Long id);
}
