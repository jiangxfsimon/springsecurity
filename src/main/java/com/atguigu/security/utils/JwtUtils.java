package com.atguigu.security.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.*;
import com.atguigu.security.entity.Role;
import com.atguigu.security.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class JwtUtils {
    //    @Value("${jwt.expiration.period}")
    private static Long period = 1000 * 60 * 60 * 24L;//1天
    //key的大小必须大于或等于256bit,需要32位英文字符，一个英文字符为：8bit,一个中文字符为12bit
    private static String key = "jwp^oen&*fOI~Niasll;/lksaj[foiefk";
    //设置加密算法
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    //获取header中的数据
    private static final Integer GET_HEADER_DATA = 0;
    //获取payload中的数据
    private static final Integer GET_PAYLOAD_DATA = 1;


    /**
     * 获取转换后的私钥对象
     *
     * @return
     */
    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    /**
     * 生成JWT
     *
     * @param exp     指定过期时间，不能小于当前时间
     * @param payLoad 携带的数据
     * @return
     */
    public static String createJwt(Long exp, Map<String, Object> payLoad) {
        long current = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(payLoad)//设置携带参数
                .setIssuedAt(new Date(current))//创建时间
                .setExpiration(new Date(current + exp))//过期时间
                .signWith(getSecretKey(), signatureAlgorithm)//设置加密算法和私钥
                .compact();
    }


    public static String createUserJwt(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId());
        map.put("authorities",user.getAuthorities());
        return createJwt(period, map);
    }

    /**
     * 解析JWS，返回一个布尔结果
     *
     * @param jwsString
     * @return
     */
    private static Boolean parseJwt(String jwsString) {
        boolean result = false;
        try {
            Jwts.parserBuilder()
//                    .setAllowedClockSkewSeconds(60 * 60 * 24)
                    .setSigningKey(getSecretKey())//设置私钥
                    .build()
                    .parseClaimsJws(jwsString);//要解析的jws
            result = true;
        } catch (JwtException e) {
            log.error("错误Class: {}, 错误消息: {}", e.getClass(), e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    private static String getJson(String jwsString, Integer code) {
        //判断解析结果如果失败返回空，如果有全局异常处理，此处可抛自定义异常进行处理
        if (!parseJwt(jwsString)) return null;
        //将jws中的数据编码串截取出来使用Base64解析成字节数组
        byte[] decodePayLoad = Base64Utils.decodeFromString(jwsString.split("\\.")[code]);
        return new String(decodePayLoad);
    }

    public static Map<String, Object> getData(String jwsString, Integer code) {
        //此处使用的阿里的fastJson,可使用其他的工具将字节json字节转map
        String json = getJson(jwsString, code);
        return JSON.parseObject(json, Map.class);
    }

    /**
     * 获取header中的数据
     *
     * @param jwsString
     * @return
     */
    public static Map<String, Object> getHeader(String jwsString) {
        return getData(jwsString, GET_HEADER_DATA);
    }

    /**
     * 获取PayLoad中携带的数据
     *
     * @param jwsString
     * @return
     */
    public static Map<String, Object> getPayLoad(String jwsString) {
        return getData(jwsString, GET_PAYLOAD_DATA);
    }

    /**
     * 获取除去exp和iat的数据，exp：过期时间，iat：JWT生成的时间
     *
     * @param jwsString
     * @return
     */
    public static Map<String, Object> getPayLoadExcludeExpAndIat(String jwsString) {
        Map<String, Object> map = getData(jwsString, GET_PAYLOAD_DATA);
        map.remove("exp");
        map.remove("iat");
        return map;
    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) throws JsonProcessingException {
        Map<String, Object> payLoad1 = JwtUtils.getPayLoad("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MywiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfdXNlciJ9LHsiYXV0aG9yaXR5IjoiUk9MRV9hZG1pbiJ9XSwidXNlcm5hbWUiOiJibHIiLCJpYXQiOjE2NDk2MDEwODMsImV4cCI6MTY0OTY4NzQ4M30.flP9GNEoKV6jfAxkKfItlCBV-kMC5w6H2BpzbBC-TGE1");



        User user = new User();
        user.setId(100);
        user.setUsername("Simon");
        user.setPassword("{noop}123");

        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        Role role = new Role();
        role.setName("ABC");
        Role role1 = new Role();
        role1.setName("BCE");
        user.setRoles(Arrays.asList(role,role1 ));
////
        String userJwt = JwtUtils.createUserJwt(user);
        System.out.println(userJwt);
        Map<String, Object> payLoad = JwtUtils.getPayLoad(userJwt);
        System.out.println(payLoad);
//        SimpleGrantedAuthority authorities = JSONArray.toJavaObject((JSON) payLoad.get("authorities"), SimpleGrantedAuthority.class);

        List<SimpleGrantedAuthority> authorities1 = ((JSONArray) payLoad.get("authorities")).toJavaList(SimpleGrantedAuthority.class);
        System.out.println(authorities1);

        //序列化
        String s = JSON.toJSONString(user, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                System.out.println(object);
                System.out.println(name);
                System.out.println(value);
                return false;
            }
        });
        String s1 = JSON.toJSONString(user, new AfterFilter() {
            @Override
            public void writeAfter(Object object) {
                System.out.println(object);
            }
        });
        System.out.println(s1);
        String s2 = JSON.toJSONString(user, (ContextValueFilter) (context, object, name, value) -> {
            System.out.println(context);
            System.out.println(object);
            System.out.println(name);
            System.out.println(value);
            return null;
        });
        System.out.println(s2);
        JSON.toJSONString(user, new SimplePropertyPreFilter() {
        });
    }
}