package com.atguigu.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/toLogin")
    public String login(){
        System.out.println("login....");
        return "login";
    }

    @PostMapping("/toError")
    public String error(){
        System.out.println("error....");
        return "redirect:error.html";
    }

    @GetMapping("/toUpdate")
    public String update(){
        return "update";
    }
}
