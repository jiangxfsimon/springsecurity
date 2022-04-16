package com.atguigu.security;

import com.alibaba.fastjson.JSON;
import com.atguigu.security.entity.Role;
import com.atguigu.security.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Base64Utils;

import java.util.*;
import java.util.stream.Stream;

public class PasswordEncoderTest {
    public static void main(String[] args) throws JsonProcessingException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);//默认16次散列

        String encode = encoder.encode("123");
        String encode1 = encoder.encode("123");
        System.out.println(encode);
        System.out.println(encode1);
//        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder();

        Map<String,Object> map=new HashMap<>();
        map.put("1","a,b,c,d");
        map.compute("2",(k,v)-> Objects.isNull(v) ? null :((String) v).split(","));
        System.out.println(map);
        String pwd="123456";
        String salt="abc";
        byte[] encode2 = Base64Utils.encode((pwd+salt).getBytes());
        System.out.println(new String(encode2));
        byte[] passwd = Base64Utils.decode(encode2);
        System.out.println(new String(passwd));

        String s="ccc";
        System.out.println(Optional.ofNullable(s).orElse("abc"));

        System.out.println(APP_MODE.getMode("a"));
        APP_MODE standalone = APP_MODE.getMode("STANDALONE");

//        Jwts.builder().setId()
        String st="地址123";
        System.out.println(st.replaceAll("[^\\d]*(\\d{1,})", "$1"));


        Date date = new Date(1648187047000l);
        System.out.println(date);


        String format = String.format("%s 在%s进行登录成功", "aaaa", "bbbb");
        System.out.println(format);
        System.out.println(new Date());

        User user = new User();
        user.setPassword("abc");
        user.setUsername("simon");
        user.setId(100);
        Role role = new Role();
        role.setId(525);
        role.setName("ROLE_ADMIN");
        role.setNameZh("nihaoa");
//        Object[] objects=new Object[]{user,role};
//        String s1 = JSON.toJSONString(objects);
//        System.out.println(s1);
        ObjectMapper objectMapper = new ObjectMapper();

//        Optional.ofNullable(user).map(u->JSON.toJSONString(u)));
//        String s1 = objectMapper.writeValueAsString(user);
//        System.out.println(s1);


        System.out.println(Arrays.asList(APP_MODE.values()));
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key","a,b,c,");
//        map1.compute("key",(k,v)-> Optional.ofNullable(v).map(val ->Arrays.asList(((String)val).split(","))).orElse(""));
        map1.computeIfPresent("key",(k,v)->Arrays.asList(((String)v).split(",")));
        System.out.println(map1);

        System.out.println(null==new Object());

        List<String> strList = new ArrayList<>();
        strList.add("a,b,");
        strList.add("a,c,i,p,0");
        strList.add(null);
//        HashSet<Object> collect = strList.stream().map(e -> Optional.ofNullable(e).map(str -> Arrays.asList(s.split(","))).orElse()).collect(HashSet::new, HashSet::add, HashSet::addAll);
//        System.out.println(collect);

    }
}
enum APP_MODE {
    STANDALONE("STANDALONE", "独立部署"),
    MIXALONE("MIXALONE", "混合独立"),
    MIX("MIX", "混合部署");

    public String mode;
    public String desc;

    APP_MODE(String mode, String desc) {
        this.mode = mode;
        this.desc = desc;
    }

    public static APP_MODE getMode(String mode) {
        return Stream.of(APP_MODE.values()).filter(e -> e.mode.equals(mode)).findFirst().orElse(null);
    }
}