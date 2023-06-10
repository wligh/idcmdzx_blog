package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Comment;
import com.Idcmdzx.domain.vo.CommentVo;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.mapper.CommentMapper;
import com.Idcmdzx.mapper.UserMapper;
import com.Idcmdzx.service.ICommentService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.Idcmdzx.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),"article_id", articleId)
                .eq("root_id", -1)
                .eq("type",commentType);

        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        Page<Comment> page = page(commentPage, commentQueryWrapper);
        List<CommentVo> commentVos = toCommentVoList(page.getRecords());
        List<CommentVo> collect = commentVos.stream().peek(commentVo -> {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }).collect(Collectors.toList());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("rows",collect);
        hashMap.put("total",page.getTotal());
        return ResponseResult.okResult(hashMap);
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        comment.setCreateBy(SecurityUtils.getUserId());
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("root_id", id)
                .orderByAsc("create_time");
        List<Comment> commentList = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(commentList);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList) {
        List<CommentVo> commentVoList = BeanCopyUtil.copyBeanList(commentList, CommentVo.class);
        List<CommentVo> commentVos = commentVoList.stream().peek(commentVo -> {
            Long toCommentUserId = commentVo.getToCommentUserId();
            //  toCommentUserId为-1 则此评论为根评论 没有被评论用户
            if (toCommentUserId != -1) {
                commentVo.setToCommentUserName(userMapper.selectById(toCommentUserId).getUserName());
            }
            commentVo.setUsername(userMapper.selectById(commentVo.getCreateBy()).getNickName());
        }).collect(Collectors.toList());
        return commentVos;
    }
}
