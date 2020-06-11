package org.codeme.im.imapi.config;

import org.codeme.im.imapi.auth.RestRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * RequestInterceptorConfig
 *
 * @author walker lee
 * @date 2019-11-07
 */
@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHZBackStageRequestInterceptor()).addPathPatterns("/**").excludePathPatterns("/api/oauth/**");
    }




    @Bean
    RestRequestInterceptor getHZBackStageRequestInterceptor(){
        return new RestRequestInterceptor();
    }
}
