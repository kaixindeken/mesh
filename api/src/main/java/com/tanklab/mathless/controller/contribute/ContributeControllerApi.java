package com.tanklab.mathless.controller.contribute;

import com.tanklab.mathless.pojo.bo.DeployV2BO;
import com.tanklab.mathless.pojo.bo.NewFunctionBO;
import com.tanklab.mathless.pojo.bo.UpdateFunctionBO;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Api(value = "函数贡献", tags = "函数贡献 controller")
@RequestMapping("/api/contribute")
public interface ContributeControllerApi {

    /**
     * 函数汇总
     */
    @ApiOperation(value = "当前用户函数汇总", notes = "当前用户函数汇总", httpMethod = "GET")
    @GetMapping("/functions")
    public GraceJSONResult functions();

    @ApiOperation(value = "函数删除", notes = "函数删除", httpMethod = "POST")
    @PostMapping("/delete/v2")
    public GraceJSONResult deleteFunction(@RequestParam Long id);

    /**
     * 函数基本信息编辑
     */
    //新建
    @ApiOperation(value = "模板信息获取", notes = "模板信息获取", httpMethod = "GET")
    @GetMapping("/models")
    public GraceJSONResult models();

    @ApiOperation(value = "函数新建", notes = "函数新建", httpMethod = "POST")
    @PostMapping("/new/v2")
    public GraceJSONResult createFunction(@RequestBody @Valid NewFunctionBO functionInfoBO) throws IOException;

    //更新
    @ApiOperation(value = "获取函数基本信息", notes = "获取函数基本信息", httpMethod = "GET")
    @GetMapping("/function")
    public GraceJSONResult getFunctionInfo(@RequestParam Long id);

    @ApiOperation(value = "函数信息更新", notes = "函数信息更新", httpMethod = "POST")
    @PostMapping("/update/v2")
    public GraceJSONResult updateFunction(@RequestBody @Valid UpdateFunctionBO functionInfoBO) throws IOException;

    /**
     * 函数部署
     */
    @ApiOperation(value = "部署", notes = "部署", httpMethod = "POST")
    @PostMapping("/deploy/v2")
    public GraceJSONResult deployV2(
            @RequestBody @Valid DeployV2BO deployV2BO) throws IOException, ApiException;

    @ApiOperation(value = "部署状态获取", notes = "部署状态获取", httpMethod = "POST")
    @PostMapping("/deploy/status")
    public GraceJSONResult deployStatus(
            @RequestBody @Valid String dir) throws IOException, ApiException;
}
