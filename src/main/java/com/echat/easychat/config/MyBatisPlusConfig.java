package com.echat.easychat.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/24 21:12
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 可以设置超出最大页数的处理
        paginationInnerInterceptor.setOverflow(true);
        // 可以设置最大单页限制，默认 500，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(500L);
        return paginationInnerInterceptor;
    }

}
