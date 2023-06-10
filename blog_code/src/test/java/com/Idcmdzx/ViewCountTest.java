package com.Idcmdzx;

import com.Idcmdzx.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class ViewCountTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void viewCountTest(){
        List<Map<String, Object>> viewCountList = articleMapper.selectViewCount();
        Map<Long, Integer> collect = viewCountList.stream()
                .collect(Collectors.toMap(map -> (Long) map.get("id"), map -> ((Long) map.get("view_count")).intValue()));
        Set<Map.Entry<Long, Integer>> entries = collect.entrySet();
        for (Map.Entry<Long, Integer> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

}
