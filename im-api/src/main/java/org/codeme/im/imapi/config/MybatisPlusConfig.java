package org.codeme.im.imapi.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlusConfig
 *
 * @author walker lee
 * @date 2019-10-11
 */
@EnableTransactionManagement
@Configuration
@MapperScan("org.codeme.im.imapi.mapper")
public class MybatisPlusConfig {


    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 开启 count 的 join 优化,只针对 left join !!!
//        return new PaginationInterceptor().setCountSqlParser(new JsqlParserCountOptimize(true));
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
