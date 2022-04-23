package com.atguigu.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//设置允许跨域路径
//                .allowedOrigins("*")//设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                .allowedHeaders("*")///设置允许的header属性
                .allowedMethods("GET","POST","DELETE","PUT")//设置允许的请求方式
                .allowCredentials(true)//是否允许cookie
                .maxAge(3600);//跨域允许的时间
    }
}
