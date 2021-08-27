package com.tanklab.mathless.controller;

import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        return GraceJSONResult.ok();
    }
}
