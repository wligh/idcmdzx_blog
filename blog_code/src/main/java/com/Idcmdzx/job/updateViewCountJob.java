package com.Idcmdzx.job;

import com.Idcmdzx.domain.entity.Article;
import com.Idcmdzx.service.IArticleService;
import com.Idcmdzx.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class updateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IArticleService articleService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void updateViewCount(){
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        Set<Map.Entry<String, Integer>> entries = cacheMap.entrySet();
        List<Article> articleList = entries.stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        for (Article article : articleList) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article :: getId, article.getId());
            updateWrapper.set(Article :: getViewCount, article.getViewCount());
            articleService.update(updateWrapper);
        }
//        articleService.updateBatchById(articleList);
    }

}
