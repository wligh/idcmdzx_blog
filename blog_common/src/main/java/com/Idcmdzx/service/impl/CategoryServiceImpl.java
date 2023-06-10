package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Article;
import com.Idcmdzx.domain.entity.Category;
import com.Idcmdzx.domain.vo.CategoryVo;
import com.Idcmdzx.domain.vo.PageVo;
import com.Idcmdzx.mapper.ArticleMapper;
import com.Idcmdzx.mapper.CategoryMapper;
import com.Idcmdzx.service.ICategoryService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("status", SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> categoryList = articleMapper.selectList(articleQueryWrapper);
        Set<Long> longSet = categoryList.stream()
                .map(category -> category.getCategoryId())
                .collect(Collectors.toSet());
        //获取文章的分类id，并且去重
        List<Category> categories = listByIds(longSet);
        List<Category> list = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVoList = BeanCopyUtil.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    public ResponseResult listAllCategory() {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("status", SystemConstants.STATUS_NORMAL);
        List<Category> categoryList = list(categoryQueryWrapper);
        List<CategoryVo> categoryVoList = BeanCopyUtil.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    @Transactional
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(category.getName()),Category::getName, category.getName());
        queryWrapper.eq(Objects.nonNull(category.getStatus()),Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Category> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
