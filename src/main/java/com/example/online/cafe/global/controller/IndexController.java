package com.example.online.cafe.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/welcome")
public class IndexController {
    @GetMapping()
    public String welcomePage() {
        return "welcome";
    }
}
