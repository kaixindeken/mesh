package com.tanklab.mathless.controller.user;

import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/api")
@Api(
        value = "hello",
        tags = {"测试 controller"})
public interface HelloControllerApi {

    @ApiOperation(value = "hello 测试", notes = "hello 测试", httpMethod = "POST")
    @PostMapping("/hello")
    public GraceJSONResult Hello();

    @ApiOperation(value = " redis 测试", notes = "redis 测试", httpMethod = "GET")
    @GetMapping("/redis")
    public GraceJSONResult Redis();

    @ApiOperation(value = " k8s 测试", notes = "k8s 测试", httpMethod = "GET")
    @GetMapping("/k8s")
    public GraceJSONResult Kubernetes() throws IOException, ApiException;
}
