package com.tanklab.mathless.controller.contribute;

import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Api(value = "Gitlab操作", tags = "Gitlab 操作 controller")
@RequestMapping("/api/gitlab")
public interface GitlabControllerApi {

    @ApiOperation(value = "新建仓库", notes = "新建仓库", httpMethod = "POST")
    @PostMapping("/create")
    public GraceJSONResult create(@RequestParam String functionName) throws IOException;

    @ApiOperation(value = "删除仓库", notes = "删除仓库", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String functionName) throws IOException;

}
