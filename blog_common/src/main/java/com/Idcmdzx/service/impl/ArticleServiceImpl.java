package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.AddArticleDto;
import com.Idcmdzx.domain.entity.Article;
import com.Idcmdzx.domain.entity.ArticleTag;
import com.Idcmdzx.domain.entity.Category;
import com.Idcmdzx.domain.vo.*;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.mapper.ArticleMapper;
import com.Idcmdzx.mapper.ArticleTagMapper;
import com.Idcmdzx.mapper.CategoryMapper;
import com.Idcmdzx.service.IArticleService;
import com.Idcmdzx.service.IArticleTagService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-05
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private IArticleTagService articleTagService;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc("view_count");
        Page<Article> articlePage = new Page<>(1,10);
        List<Article> articleList = page(articlePage, queryWrapper).getRecords();
        List<HotArticleVo> hotArticleVos = BeanCopyUtil.copyBeanList(articleList, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper
                .eq(Objects.nonNull(categoryId) && categoryId > 0,"category_id",categoryId)
                .eq("status",SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc("is_top");

        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        Page<Article> pageList = page(articlePage, articleQueryWrapper);
        List<Article> articleList = pageList.getRecords().stream().map(article -> {
            Category byId = categoryMapper.selectById(article.getCategoryId());
            article.setCategoryName(byId.getName());
            return article;
        }).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtil.copyBeanList(articleList, ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,pageList.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article articleById = getById(id);
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        Category category = categoryMapper.selectById(articleById.getCategoryId());
        articleById.setViewCount(Long.valueOf(viewCount));
        articleById.setCategoryName(category.getName());
        ArticleVo articleVo = BeanCopyUtil.copyBean(articleById, ArticleVo.class);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addOrUpdateArticle(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtil.copyBean(articleDto, Article.class);
        saveOrUpdate(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 更新 博客和标签的关联
        //先删除再插入
        delete(articleDto.getId());

        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.like(StringUtils.hasText(title),"title",title)
                .like(StringUtils.hasText(summary),"summary",summary);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage,articleQueryWrapper);
        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtil.copyBeanList(articlePage.getRecords(), AdminArticleListVo.class);
        return ResponseResult.okResult(new PageVo(adminArticleListVos,articlePage.getTotal()));
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        if(ObjectUtils.isEmpty(id)){
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_NULL);
        }
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getByArticle(Long id) {
        if(ObjectUtils.isEmpty(id)){
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_NULL);
        }
        Article article = getById(id);
        QueryWrapper<ArticleTag> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("article_id",id);
        List<ArticleTag> articleTagList = articleTagService.list(articleQueryWrapper);
        AdminArticleVo adminArticleVo = BeanCopyUtil.copyBean(article, AdminArticleVo.class);
        List<Long> tagList = articleTagList.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        adminArticleVo.setTags(tagList);
        return ResponseResult.okResult(adminArticleVo);
    }


    public void delete(Long id){
        QueryWrapper<ArticleTag> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("article_id",id);
        articleTagService.remove(articleQueryWrapper);
    }
}
