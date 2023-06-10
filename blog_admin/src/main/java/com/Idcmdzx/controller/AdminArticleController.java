package com.Idcmdzx.controller;

import com.Idcmdzx.domain.ResponseResult;
import com.Idcmdzx.domain.dto.AddArticleDto;
import com.Idcmdzx.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author Idcmdzx
 * @since 2023-05-05
 */
@RestController
@RequestMapping("/content/article")
public class AdminArticleController {

    @Autowired
    private IArticleService articleService;

    @GetMapping("/list")
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize,String title,String summary){
        return articleService.adminArticleList(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getByIdArticle(@PathVariable String id){
        return articleService.getByArticle(Long.valueOf(id));
    }

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.addOrUpdateArticle(articleDto);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addOrUpdateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable String id){
        return articleService.deleteArticle(Long.valueOf(id));
    }
}
