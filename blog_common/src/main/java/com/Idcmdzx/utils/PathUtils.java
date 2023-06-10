package com.Idcmdzx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    public static String generateFilePath(String fileName){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = simpleDateFormat.format(new Date());
        // uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int index = fileName.lastIndexOf(".");
        //文件名后缀
        String substring = fileName.substring(index);
        return new StringBuilder(formatDate).append(uuid).append(substring).toString();
    }

}
