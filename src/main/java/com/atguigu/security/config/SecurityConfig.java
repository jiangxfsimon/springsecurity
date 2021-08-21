package com.atguigu.security.config;

import com.atguigu.security.handler.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired//注入数据源
    DataSource dataSource;
    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;

    @Bean//配置remember me持久化数据库实现对象
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcToken = new JdbcTokenRepositoryImpl();
        jdbcToken.setDataSource(dataSource);
        return jdbcToken;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    /** permitAll表示允许访问
     *  denyAll表示不允许访问
     *  anonymous表示匿名访问(不需要登陆就可以访问)
     *  authenticated表示登陆后可以访问(包括rememberMe)
     *  fullyAuthenticated表示登陆后可以访问(不包括rememberMe)
     *  rememberMe表示可以访问
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()//表单提交
                .loginPage("/login.html")//自定义登陆页面
                .loginProcessingUrl("/login")//必须和表单提交的接口一样，然后执行自定义登陆逻辑(执行UserDetailService)
                .defaultSuccessUrl("/success.html")//登陆成功跳转页面,POST请求到此页面
//                .successForwardUrl("http://www.baidu.com") 自定义成功跳转页面
                .failureForwardUrl("/toError");//登陆失败跳转页面,POST请求到此页面
//                .failureHandler("")//自定义登陆失败跳转

        http.logout().logoutSuccessUrl("/login.html");//退出成功后跳转页面


        //授权
        http.authorizeRequests()// ant匹配规则： ？匹配一个字符 *匹配0个或多个字符   **匹配0个或多个目录
//                .antMatchers("/**/*.jpg").permitAll()放心所有的jpg图片
//                .regexMatchers(HttpMethod.DELETE,"[.]png$").permitAll()// 使用正则匹配
//                .antMatchers(HttpMethod.POST,"/dem0").permitAll()
                .antMatchers("/toLogin","/login.html","/error.html","/toUpdate").permitAll()//对“/login.html”,"/error.html"放行
//                .antMatchers("/update.html").hasAuthority("role")
//                .antMatchers("/update.html").hasIpAddress("127.0.0.1")//基于IP地址访问
//                .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");//自定义access方法
                .anyRequest().authenticated();//所有请求必须认证后才能访问(必须登陆)，必须放在最后


        http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService);
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);


//        http.csrf().disable();//跨站请求伪造
     }
}
