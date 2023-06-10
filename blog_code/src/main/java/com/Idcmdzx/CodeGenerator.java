package com.Idcmdzx;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

/*
  代码生成器
 */

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/sg_blog",
                        "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Idcmdzx") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\data\\my_blog"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.Idcmdzx") // 设置父包名
                            //                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\data\\my_blog_xml")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user_role")// 设置需要生成的表名
                            .addTablePrefix("sg_","sys_") //去除表前缀
                            .entityBuilder()
                            .enableLombok()
                            .controllerBuilder()
                            .enableRestStyle();
                })
                //                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
