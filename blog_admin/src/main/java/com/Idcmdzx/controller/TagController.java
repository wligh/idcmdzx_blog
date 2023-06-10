package com.Idcmdzx.controller;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.TagDto;
import com.Idcmdzx.domain.dto.TagListDto;
import com.Idcmdzx.domain.vo.PageVo;
import com.Idcmdzx.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签 前端控制器
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addOrUpdateTag(tagDto);
    }

    @PreAuthorize("@ps.hasPermission('content:tag:delete')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable String id){
        return tagService.deleteTag(Long.valueOf(id));
    }

    @GetMapping("/{id}")
    public ResponseResult getByTagId(@PathVariable String id){
        return tagService.getByTagId(Long.valueOf(id));
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.addOrUpdateTag(tagDto);
    }

}
