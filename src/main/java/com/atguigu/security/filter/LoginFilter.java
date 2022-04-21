package com.atguigu.security.filter;

import com.atguigu.security.entity.User;
import com.atguigu.security.exception.KaptchaException;
import com.atguigu.security.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义前后端分离系统
 */
@Setter
@Accessors(chain = true)
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    public static final String AUTHENTICATION = "Authentication";
    private static final String FORM_KAPTCHA_KEY = "kaptcha";
    private String kaptchaParameter = FORM_KAPTCHA_KEY;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1.是否是POST
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.是否是JSON数据
        //3.获取json数据中的username和password
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String kaptcha = userInfo.get(getKaptchaParameter());//获取验证码
//                String kaptchaSession = (String) request.getSession().getAttribute(getKaptchaParameter());
//                String kaptchaSession = (String) redisTemplate.opsForValue().get(getKaptchaParameter());
                //验证登录验证码
//                if (!StringUtils.isEmpty(kaptcha) && !StringUtils.isEmpty(kaptchaSession) &&
//                        kaptcha.equalsIgnoreCase(kaptchaSession)) {
                if (true) {//暂时放行
                    String username = userInfo.get(getUsernameParameter());
                    String pwd = userInfo.get(getPasswordParameter());

                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, pwd);
                    setDetails(request, authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
                } else {
                    throw new KaptchaException("验证码错误");
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //如果不是JSON数据则调用父类原始的表单提交的方式验证
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登录成功");
        response.setStatus(HttpStatus.OK.value());
        map.put("authentication", authResult);
        User user = (User) authResult.getPrincipal();
        String token = JwtUtils.createUserJwt(user);
        response.addHeader(AUTHENTICATION, token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(map));

        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登录成功失败");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        map.put("exception", failed.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(map));

        response.getWriter().flush();
        response.getWriter().close();
    }

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }
}
