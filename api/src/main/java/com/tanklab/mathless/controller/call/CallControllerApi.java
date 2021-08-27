package com.tanklab.mathless.controller.call;

import com.tanklab.mathless.pojo.bo.ConsoleBO;
import com.tanklab.mathless.pojo.bo.LogBO;
import com.tanklab.mathless.pojo.bo.OutputBO;
import com.tanklab.mathless.pojo.bo.RunBO;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/api/call")
@Api(
        value = "函数调用",
        tags = {"函数调用 controller"})
public interface CallControllerApi {

    @ApiOperation(value = "依赖获取", notes = "依赖获取", httpMethod = "GET")
    @GetMapping("/packages")
    public GraceJSONResult getDependencies();

    @ApiOperation(value = "第二版运行", notes = "第二版运行", httpMethod = "POST")
    @PostMapping("/run/v2")
    public GraceJSONResult runInPod(@RequestBody @Valid RunBO runBO) throws IOException, ApiException, InterruptedException;

    @ApiOperation(value = "输出", notes = "输出", httpMethod = "POST")
    @PostMapping("/logs/v2")
    public GraceJSONResult output(@RequestBody @Valid OutputBO outputBO) throws IOException;

    @ApiOperation(value = "运行终端", notes = "运行终端", httpMethod = "POST")
    @PostMapping("/exec/console")
    public GraceJSONResult execConsole(@RequestBody @Valid ConsoleBO consoleBO);

    @ApiOperation(value = "日志", notes = "日志", httpMethod = "POST")
    @PostMapping("/logs")
    public GraceJSONResult logs(@RequestBody @Valid LogBO logBO);

}
