package com.atguigu.security.config;

import com.atguigu.security.handler.MyAccessDeniedHandler;
import com.atguigu.security.handler.MyAuthenticationFailureHandler;
import com.atguigu.security.handler.MyAuthenticationSuccessHandler;
import com.atguigu.security.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.sql.DataSource;

//@Configuration
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

    /**
     * 如果重写configure(AuthenticationManagerBuilder auth)方法则父类中该方法不会生效(则disableLocalConfigureAuthenticationBldr=false)
     * 而在父类authenticationManager()方法中有下面方法：
     *      false:则会使用类变量localConfigureAuthenticationBldr来构建parent级别的AuthenticationManager
     *      true:则成员变量AuthenticationConfiguration自动配置类就会生效(使用全局配置的AuthenticationManager)
     *      if (disableLocalConfigureAuthenticationBldr) {
     * 				authenticationManager = authenticationConfiguration.getAuthenticationManager();
     * 			}else {
     * 				authenticationManager = localConfigureAuthenticationBldr.build();
     *            }
     */
    @Override//自定义的AuthenticationManager，在spring容器中将不会被暴露出来，可以使用authenticationManagerBean()进行暴露
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);//默认指定的是ProviderManager中parent(AuthenticationManager)的DaoAuthenticationProvider中的userDetailsService
//                .passwordEncoder(encoder());
    }
    @Bean
    @Override//作用：用来将自定义的AuthenticationManager(上面)在spring容器中进行暴露为bean，可以在任何位置注入
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public PasswordEncoder encoder(){//{noop}123 表示明文密码
//        return new BCryptPasswordEncoder();
//    }



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
        http.formLogin()//表单提交认证，使用了UsernamePasswordAuthenticationFilter.attemptAuthentication()实现认证
                .loginPage("/login.html")//自定义登陆页面
                .loginProcessingUrl("/doLogin")//必须和表单提交的接口一样，然后执行自定义登陆逻辑(执行UserDetailService)

                //以下两个认证成功跳转只适用于非前后端分离系统，前后端分离提供了successHandler()
//                .defaultSuccessUrl("/success.html")//登陆成功跳转页面(redirect),如果之前访问了其他页面会优先跳转其他页面，如果没有再跳转success.html。源码使用了SavedRequestAwareAuthenticationSuccessHandler类处理
                .successForwardUrl("/success.html")// 自定义成功跳转页面 forward(无论之前是否访问其他页面都会跳转到此页面 )，源码使用了ForwardAuthenticationSuccessHandler类
                //前后端分离需要返回JSON数据
//                .successHandler(new MyAuthenticationSuccessHandler())

//                .failureForwardUrl("/fail");//登陆失败跳转页面(使用forward方式跳转)，使用ForwardAuthenticationFailureHandler类(登录失败异常会保存在request域中[方法参考onAuthenticationFailure]，key为SPRING_SECURITY_LAST_EXCEPTION)
//                .failureUrl()//登录失败跳转(使用redirect方式)，使用SimpleUrlAuthenticationFailureHandler类(登录失败异常保存在session中[方法参考onAuthenticationFailure]，key为SPRING_SECURITY_LAST_EXCEPTION)
                .failureHandler(new MyAuthenticationFailureHandler());//前后端分离的登录失败跳转

        http.logout().logoutUrl("/logout")
//                .logoutSuccessUrl("/login.html");//退出成功后跳转页面 GET方式
                //自定义多个退出登录逻辑
//                .logoutRequestMatcher(new OrRequestMatcher(
//                        new AntPathRequestMatcher("/logout1","GET"),
//                        new AntPathRequestMatcher("/logout2","GET")
//                )).invalidateHttpSession(true).clearAuthentication(true)//退出登录后清理session与Authentication(默认就会清理)
                //前后端分离注销登录逻辑
                .logoutSuccessHandler(new MyLogoutSuccessHandler());


        //授权
        http.authorizeRequests()// ant匹配规则： ？匹配一个字符 *匹配0个或多个字符   **匹配0个或多个目录
//                .antMatchers("/**/*.jpg").permitAll()放心所有的jpg图片
//                .regexMatchers(HttpMethod.DELETE,"[.]png$").permitAll()// 使用正则匹配
//                .antMatchers(HttpMethod.POST,"/dem0").permitAll()
                .antMatchers("/toLogin","/logout","/doLogin","/fail","/login.html","/error.html","/toUpdate").permitAll()//对“/login.html”,"/error.html"放行
//                .antMatchers("/update.html").hasAuthority("role")
//                .antMatchers("/update.html").hasIpAddress("127.0.0.1")//基于IP地址访问
//                .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");//自定义access方法
                .anyRequest().authenticated();//所有请求必须认证后才能访问(必须登陆)，必须放在最后


//        http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService);
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);


        http.csrf().disable();//跨站请求伪造
     }
}
