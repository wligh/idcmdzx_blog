package com.Idcmdzx.controller;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Comment;
import com.Idcmdzx.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "获取评论列表",notes = "获取一页评论列表，子评论根据评论创建时间升序排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId",value = "文章Id"),
            @ApiImplicitParam(name = "pageNum",value = "当前页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页评论数")
    })
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
