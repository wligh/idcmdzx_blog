package com.Idcmdzx.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtil {

    //泛型方法 <T>
    public static <T> T copyBean(Object source,Class<T> target){
        T t = null;
        try {
            t = target.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source,t);
        return t;
    }

    public static <T> List<T> copyBeanList(List<?> sources, Class<T> target){
        return sources.stream()
                .map(source -> copyBean(source, target))
                .collect(Collectors.toList());
    }

}
