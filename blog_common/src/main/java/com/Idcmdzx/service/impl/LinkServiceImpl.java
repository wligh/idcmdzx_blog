package com.Idcmdzx.service.impl;

import com.Idcmdzx.Constants.SystemConstants;
import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.entity.Link;
import com.Idcmdzx.domain.vo.LinkVo;
import com.Idcmdzx.domain.vo.PageVo;
import com.Idcmdzx.mapper.LinkMapper;
import com.Idcmdzx.service.ILinkService;
import com.Idcmdzx.utils.BeanCopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-06
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Override
    public ResponseResult getAllLink() {
        QueryWrapper<Link> linkQueryWrapper = new QueryWrapper<>();
        linkQueryWrapper.eq("status", SystemConstants.STATUS_NORMAL);
        List<Link> linkList = list(linkQueryWrapper);
        List<LinkVo> linkVoList = BeanCopyUtil.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVoList);
    }

    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(link.getName()),Link::getName, link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()),Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
