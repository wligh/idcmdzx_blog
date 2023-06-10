package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-07
 */
public interface ICommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
