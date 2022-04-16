package com.atguigu.security.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private  Producer producer;
    @Autowired
    private DataSource dataSource;
    //生成验证码   base64转图片工具：https://tool.chinaz.com/tools/imgtobase 格式：data:image/jpg;base64,
    @ResponseBody
    @GetMapping("/vc.jpg")
    public String getVerifyCode(HttpSession session) throws IOException {
        //1.⽣成验证码
        String code = producer.createText();
        session.setAttribute("kaptcha", code);//可以更换成redis实现
        BufferedImage bi = producer.createImage(code);//生成图片
        //2.写⼊内存
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi, "jpg", fos);
        //3.⽣成 base64(前后端分离不能直接返回图片的流，而是返回base64加密的string)
        return Base64.encodeBase64String(fos.toByteArray());
    }


    @GetMapping("/login.html")
    public String login(HttpServletRequest request){
        System.out.println("login....");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(dataSource.getClass());
        return "login";
    }

    @GetMapping("/fail")
    public String error(){
        System.out.println("error....");
        return "error";
    }

    @PreAuthorize("hasAnyRole('')")
    @GetMapping("/toUpdate")
    public String update(){
        return "update";
    }

    @PostMapping("/success.html")
    public String success(){
        return "success";
    }


    @GetMapping("/anonymous")
    public String anonymity(){
        return "update";
    }

}
