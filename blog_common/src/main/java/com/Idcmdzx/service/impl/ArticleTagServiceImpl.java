package com.Idcmdzx.service.impl;

import com.Idcmdzx.domain.entity.ArticleTag;
import com.Idcmdzx.mapper.ArticleTagMapper;
import com.Idcmdzx.service.IArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-13
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

}
