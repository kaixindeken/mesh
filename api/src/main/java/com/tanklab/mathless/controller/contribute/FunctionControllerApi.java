package com.tanklab.mathless.controller.contribute;

import com.tanklab.mathless.pojo.bo.FileSaveBO;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "函数操作", tags = "函数操作 controller")
@RequestMapping("/api/function")
public interface FunctionControllerApi {

    @ApiOperation(value = "新建函数", notes = "新建函数", httpMethod = "POST")
    @PostMapping("/createFunction")
    public GraceJSONResult createFunction( @RequestParam String functionName) throws IOException, InterruptedException;

    @ApiOperation(value = "删除函数", notes = "删除函数", httpMethod = "POST")
    @PostMapping("/deleteFunction")
    public GraceJSONResult deleteFunction( @RequestParam String functionName) throws IOException;

    @ApiOperation(value = "获取函数目录结构", notes = "获取函数目录结构", httpMethod = "POST")
    @PostMapping("/getFileTree")
    public GraceJSONResult getFileTree( @RequestParam String functionName);

    @ApiOperation(value = "(函数内)新建文件/文件夹", notes = "(函数内)新建文件/文件夹", httpMethod = "POST")
    @PostMapping("/create")
    public GraceJSONResult create( @RequestParam String functionName, @RequestParam String relativePath, @RequestParam boolean isFile) throws IOException, InterruptedException;

    @ApiOperation(value = "(函数内)删除文件/文件夹", notes = "(函数内)删除文件/文件夹", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete( @RequestParam String functionName, @RequestParam String relativePath);

    @ApiOperation(value = "(函数内)用户文件保存", notes = "(函数内)用户文件保存", httpMethod = "POST")
    @PostMapping("/saveFile")
    public GraceJSONResult saveFile(@RequestBody FileSaveBO fileSaveBO);

    @ApiOperation(value = "获取文件内容", notes = "获取文件内容", httpMethod = "POST")
    @PostMapping("/getFileContent")
    public GraceJSONResult getFileContent(@RequestParam String functionName, @RequestParam String relativePath);

    @ApiOperation(value = "(函数内)文件上传", notes = "(函数内)文件上传", httpMethod = "POST")
    @PostMapping("/upload")
    public GraceJSONResult upload(@RequestParam String functionName, @RequestParam String relativePath, @RequestParam("files") MultipartFile[] files);

    @ApiOperation(value = "(函数内)文件下载", notes = "(函数内)文件下载", httpMethod = "POST")
    @PostMapping("/download")
    public void download( @RequestParam String functionName, @RequestParam String relativePath, HttpServletResponse response);

    @ApiOperation(value = "获取已部署函数信息", notes = "获取已部署函数信息", httpMethod = "GET")
    @GetMapping("/getOwnFunctions")
    public GraceJSONResult getOwnFunctions(@RequestParam String username);
}
