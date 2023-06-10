package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Category;
import com.Idcmdzx.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
