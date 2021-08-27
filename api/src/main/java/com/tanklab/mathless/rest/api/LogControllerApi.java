package com.tanklab.mathless.rest.api;

import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@Api(
        value = "log",
        tags = {"日志 controller"})
public interface LogControllerApi {
    @ApiOperation(value = "运行摘要", notes = "运行摘要", httpMethod = "GET")
    @PostMapping("/run/sum")
    public GraceJSONResult Summary();

    @ApiOperation(value = "运行输出", notes = "运行输出", httpMethod = "GET")
    @GetMapping("/run/output")
    public GraceJSONResult Output();
}
