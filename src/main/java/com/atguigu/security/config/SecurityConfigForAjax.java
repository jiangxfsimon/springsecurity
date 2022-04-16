package com.atguigu.security.config;

import com.atguigu.security.filter.LoginFilter;
import com.atguigu.security.service.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 用于前后端分离系统（不能使用formLogin()-->UsernamePasswordAuthenticationFilter）
 */
//@Configuration
//public class SecurityConfigForAjax extends WebSecurityConfigurerAdapter {
//    @Autowired
//    UserDetailService userDetailService;
//
//    //根据父类DefaultPasswordEncoderAuthenticationManagerBuilder中代码，SS默认会创建支持所有加密方式的Encoder
//    //指定使用某一种加密方式的Encoder
////    @Bean
////    public PasswordEncoder passwordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
//
//    @Bean//这种方式密码会带有{bcrypt} 上面的方式不会带有{bcrypt}
//    public DelegatingPasswordEncoder delegatingPasswordEncoder(){
//        String encodingId = "bcrypt";
//        Map<String, PasswordEncoder> encoders = new HashMap<>();
//        encoders.put(encodingId, new BCryptPasswordEncoder());
//        return new DelegatingPasswordEncoder(encodingId,encoders);//升级密码最好使用DelegatingPasswordEncoder
//    }
//
//    @Bean
//    public LoginFilter loginFilter() throws Exception {
//        LoginFilter loginFilter = new LoginFilter();//loginFilter设置AuthenticationManager
//
//        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证的url
//        loginFilter.setAuthenticationManager(authenticationManagerBean());
////        loginFilter.setFilterProcessesUrl("/success.html");
//        loginFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
//            Map<String,Object> map=new HashMap<>();
//            map.put("msg","登录成功");
//            response.setStatus(HttpStatus.OK.value());
//            map.put("authentication",authentication);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().println(new ObjectMapper().writeValueAsString(map));
//        }));
//
//        loginFilter.setAuthenticationFailureHandler((request,response,authenticationException)->{
//            Map<String,Object> map=new HashMap<>();
//            map.put("msg","登录成功失败");
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            map.put("exception",authenticationException.getMessage());
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().println(new ObjectMapper().writeValueAsString(map));
//        });
//        loginFilter.setRememberMeServices(tokenBasedRememberMeServices());//设置remember-me的service
//        return loginFilter;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService);//.passwordEncoder(passwordEncoder());
//    }
//    @Bean
//    @Override//作用：用来将自定义的AuthenticationManager(上面)在spring容器中进行暴露为bean，可以在任何位置注入
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("*.js","*.gif","*.jpg","*.png","*.css","*.ico","/druid/*"
//                ,"/webjars/**"
//                ,"/swagger-ui/**","/swagger-resources/**","/v3/**");
//
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/druid/**","/login.html","/vc.jpg").permitAll() //表单提交认证方式
//                .antMatchers("/success.html").hasAnyRole("user")
////                .antMatchers().permitAll()//放行验证码
//                .anyRequest().authenticated().and().formLogin()//需要替换forLogin中UsernamePasswordAuthenticationFilter实现
//
//                //注销登录处理
//                .and().logout().logoutSuccessHandler(((request, response, authentication) -> {
//                    Map<String,Object> map=new HashMap<>();
//                    map.put("msg","注销成功");
//                    response.setStatus(HttpStatus.OK.value());
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.getWriter().println(new ObjectMapper().writeValueAsString(map));
//                }))
//                //此时访问受保护的资源会默认跳转到SS中默认的登录界面(使用异常可以处理)
//                .and().exceptionHandling()
//                .authenticationEntryPoint(((request, response, authException) -> {
//                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                    response.getWriter().println(authException.getMessage());
//                }))//处理
//                .accessDeniedHandler(((request, response, accessDeniedException) -> {
//                    Map<String,Object> map=new HashMap<>();
//                    map.put("msg",accessDeniedException.getMessage());
//                    response.setStatus(HttpStatus.OK.value());
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.getWriter().println(new ObjectMapper().writeValueAsString(map));
//                }))
////                .and()
////                .rememberMe()
////                    .tokenValiditySeconds(60*60)
////                    .tokenRepository(new InMemoryTokenRepositoryImpl())
////                    .userDetailsService(userDetailService).alwaysRemember(true)
////                .rememberMeServices(new TokenBasedRememberMeServices("remember-me",userDetailService))
//
//                .and().csrf().disable();//跨站请求伪造主要针对PATCH,POST,PUT,DELETE请求进行拦截。如果不使用session可以关闭CSRF
////               .and().csrf().ignoringAntMatchers("/druid/**");
//
//        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
//
//    }
//
//    @Bean
//    public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
//        TokenBasedRememberMeServices tokenBase = new TokenBasedRememberMeServices("KEY", userDetailService);
//        tokenBase.setAlwaysRemember(true);//前后端分离系统会从request.getParameter("remember-me")不会生效，所以需要强制设置为true
//
//        return tokenBase;
//    }
//}
