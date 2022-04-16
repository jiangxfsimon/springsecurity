package com.atguigu.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.security.entity.User;
import com.atguigu.security.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader(LoginFilter.AUTHENTICATION);
        Map<String, Object> payLoad = null;
        //存在token
        if (!StringUtils.isEmpty(token) && !CollectionUtils.isEmpty(payLoad = JwtUtils.getPayLoad(token))
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {//解析出来为null说明token有问题

            User user = JSON.parseObject(JSON.toJSONString(payLoad), User.class);
            List<SimpleGrantedAuthority> authorities = ((JSONArray) payLoad.get("authorities")).toJavaList(SimpleGrantedAuthority.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //没有token则放行到下一个filter->LoginFilter进行认证
        chain.doFilter(request, response);
    }

}