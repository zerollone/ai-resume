package com.ai.resume.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ws
 * @date 2026/4/16 20:55
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String Test() {
        return "success";
    }
}
