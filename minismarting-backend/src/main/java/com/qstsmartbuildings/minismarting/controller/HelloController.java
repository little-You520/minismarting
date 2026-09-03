package com.qstsmartbuildings.minismarting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "我的第一个Spring Boot接口跑通了！准备做智慧工地项目！";
    }
}
