package com.atguigu.security.config;

import com.alibaba.druid.support.http.StatViewFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

//    @Bean
//    public ServletRegistrationBean statViewServlet() {
//        StatViewServlet statViewServlet = new StatViewServlet();
//        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(statViewServlet,"/druid/**");
//        Map<String, String> map = new HashMap<>();
//        map.put("loginUsername", "admin");
//        map.put("loginPassword", "123456");
//        map.put("allow", ""); // 所有人都可以访问
//        bean.setInitParameters(map);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean statViewFilter(){
//        StatViewFilter statViewFilter = new StatViewFilter();
//        FilterRegistrationBean<StatViewFilter> bean = new FilterRegistrationBean<>(statViewFilter,statViewServlet());
//        Map<String, String> map = new HashMap<>();
//        map.put("exclusions", "*.js,*.css,/druid/*");
//        bean.setInitParameters(map);
//        bean.setUrlPatterns(Arrays.asList("/*"));
//        return bean;
//    }
}
