package com.Idcmdzx.service;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.TagDto;
import com.Idcmdzx.domain.dto.TagListDto;
import com.Idcmdzx.domain.entity.Tag;
import com.Idcmdzx.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-11
 */
public interface ITagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addOrUpdateTag(TagDto tagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getByTagId(Long id);

    ResponseResult listAllTag();
}
