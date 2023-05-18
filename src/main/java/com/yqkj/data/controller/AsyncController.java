package com.yqkj.data.controller;

import com.yqkj.data.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AsyncController {
    @Autowired
    private CustomService customService;

    @GetMapping("/open/something")
    public String something() {
        int count = 100;
        for (int i = 0; i < count; i++) {
            customService.doSomething("index = " + i);
        }
        return "success";
    }
}
