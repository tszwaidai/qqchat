package com.echat.easychat.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;
import java.util.Scanner;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: 代码生成器
 * @date 2024/10/24
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 获取用户输入的表名
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入表名，多个表名用英文逗号隔开：");
        String tableName = scanner.nextLine();

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/easychat?&useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "12345678")
                .globalConfig(builder -> {
                    builder.author("tszwaidai")
                            //.enableSwagger() // 启用swagger
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                             // 只在文件不存在时生成文件，避免覆盖
                })
                .packageConfig(builder -> {
                    builder.entity("entity") // 实体类包名
                            .parent("com.echat.easychat") // 父包名
                            .controller("controller") // 控制层包名
                            .mapper("mapper") // mapper层包名
                            .service("service") // service层包名
                            .serviceImpl("service.impl") // service实现类包名
                            // 自定义mapper.xml文件输出目录
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    // 设置要生成的表名
                    builder.addInclude(tableName.split(",")) // 支持多个表名，使用逗号分隔
                            .addTablePrefix("sys_") // 设置表前缀过滤
                            .entityBuilder()
                            .enableLombok() // 启用Lombok
                            .enableChainModel()
                            .naming(NamingStrategy.underline_to_camel) // 下划线转驼峰
                            .columnNaming(NamingStrategy.underline_to_camel) // 表字段映射驼峰
                            .idType(IdType.AUTO) // 主键策略
                            .formatFileName("%s") // 实体类文件名格式
                            .mapperBuilder()
                            .enableMapperAnnotation() // 启用Mapper注解
                            .enableBaseResultMap() // 启用BaseResultMap
                            .enableBaseColumnList() // 启用BaseColumnList
                            .formatMapperFileName("%sMapper") // Mapper文件名格式
                            .formatXmlFileName("%sMapper") // Mapper XML文件名格式
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service接口文件名格式
                            .formatServiceImplFileName("%sServiceImpl") // Service实现类文件名格式
                            .controllerBuilder()
                            .enableRestStyle() // 启用Rest风格的Controller
                            .formatFileName("%sController") // Controller文件名格式
                            .build();
                })
                .execute();
    }
}
