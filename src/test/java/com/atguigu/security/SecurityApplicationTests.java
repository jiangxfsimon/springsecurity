package com.atguigu.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootTest
class SecurityApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder en = new BCryptPasswordEncoder();
//        String encode = en.encode("123");
//        String encode1 = en.encode("123");
//        System.out.println(encode);
//        System.out.println(encode1);
        String compact = Jwts.builder().setSubject("Simon").setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256, "Simon").setId("888").compact();
        System.out.println(compact);

    }

}
