package com.tanklab.mathless.controller.call;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "函数补全", tags = "函数补全 controller")
@RequestMapping("/api/complete")
public interface CompleteControllerApi {

    @ApiOperation(value = "获取函数补全信息列表", notes = "函数补全", httpMethod = "GET")
    @GetMapping("/list")
    public GraceJSONResult getCompleteList() throws JsonProcessingException;

    @ApiOperation(value = "获取函数补全信息列表版本", notes = "函数补全", httpMethod = "GET")
    @GetMapping("/version")
    public GraceJSONResult getCompleteListVer();
}
