package com.atguigu.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.stream.Stream;

//@EnableOpenApi
@SpringBootApplication
@MapperScan("com.atguigu.security.mapper")
public class SecurityApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SecurityApplication.class, args);
//        Stream.of(run.getBeanDefinitionNames()).forEach(System.out::println);
    }

}
