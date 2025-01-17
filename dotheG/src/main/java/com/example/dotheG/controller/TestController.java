package com.example.dotheG.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/my")
    @ResponseBody
    public String my() {
        return "Hello World";
    }
}
