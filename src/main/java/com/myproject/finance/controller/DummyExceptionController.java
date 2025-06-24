package com.myproject.finance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyExceptionController {

    @GetMapping("/test-runtime")
    public String throwRuntime() {
        throw new RuntimeException("Test runtime exception");
    }

    @GetMapping("/test-notfound")
    public String throwNotFound() {
        throw new IllegalArgumentException("Test not found");
    }
}