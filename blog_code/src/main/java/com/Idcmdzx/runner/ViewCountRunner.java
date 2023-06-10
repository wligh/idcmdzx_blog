package com.Idcmdzx.runner;

import com.Idcmdzx.mapper.ArticleMapper;
import com.Idcmdzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        List<Map<String, Object>> selectViewCount = articleMapper.selectViewCount();
        Map<String, Integer> collect = selectViewCount.stream()
                .collect(Collectors.toMap(map -> map.get("id").toString(), map -> ((Long) map.get("view_count")).intValue()));
        redisCache.setCacheMap("article:viewCount",collect);
    }
}
