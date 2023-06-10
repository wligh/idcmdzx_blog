package com.Idcmdzx.service.impl;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.TagDto;
import com.Idcmdzx.domain.dto.TagListDto;
import com.Idcmdzx.domain.entity.Tag;
import com.Idcmdzx.domain.vo.PageVo;
import com.Idcmdzx.domain.vo.TagVo;
import com.Idcmdzx.enums.AppHttpCodeEnum;
import com.Idcmdzx.exception.SystemException;
import com.Idcmdzx.mapper.TagMapper;
import com.Idcmdzx.service.ITagService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-11
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.like(StringUtils.hasText(tagListDto.getName()),"name",tagListDto.getName())
                .like(StringUtils.hasText(tagListDto.getRemark()),"remark",tagListDto.getRemark());
        Page<Tag> tagPage = new Page<>(pageNum,pageSize);
        page(tagPage,tagQueryWrapper);
        List<TagVo> tagVos = BeanCopyUtil.copyBeanList(tagPage.getRecords(), TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVos,tagPage.getTotal()));
    }

    @Override
    public ResponseResult addOrUpdateTag(TagDto tagDto) {
        if(!StringUtils.hasText(tagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NULL);
        }
        if(!StringUtils.hasText(tagDto.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NULL);
        }
        Tag tag = BeanCopyUtil.copyBean(tagDto, Tag.class);
        saveOrUpdate(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getByTagId(Long id) {
        Tag byIdTag = getById(id);
        TagVo tagVo = BeanCopyUtil.copyBean(byIdTag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVos = BeanCopyUtil.copyBeanList(tagList, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }


}
