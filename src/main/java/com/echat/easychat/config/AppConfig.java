package com.echat.easychat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/11/21 10:51
 */
@Configuration
@Data
public class AppConfig {

    // 配置文件中定义的项目文件夹路径
    @Value("${app.project-folder}")
    private String projectFolder;


}
