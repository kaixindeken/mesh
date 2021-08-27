package com.tanklab.mathless.controller.call;

import com.tanklab.mathless.pojo.bo.FileBO;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "文件操作", tags = "文件操作 controller")
@RequestMapping("/api/file")
public interface FileControllerApi {

    @ApiOperation(value = "文件上传", notes = "文件上传", httpMethod = "POST")
    @PostMapping("/upload")
    public GraceJSONResult upload(@RequestParam("files") MultipartFile[] files);

    @ApiOperation(value = "文件下载", notes = "文件下载", httpMethod = "POST")
    @PostMapping("/download")
    public void download(@RequestParam String relativePath);

    @ApiOperation(value = "新建", notes = "新建", httpMethod = "POST")
    @PostMapping("/create")
    public GraceJSONResult create(@RequestParam String relativePath, @RequestParam boolean isFile) throws IOException, InterruptedException;

    @ApiOperation(value = "删除", notes = "删除", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String relativePath);

    @ApiOperation(value = "用户文件保存", notes = "用户文件保存", httpMethod = "POST")
    @PostMapping("/saveFile")
    public GraceJSONResult saveFile(@RequestBody FileBO fileBO);

    @ApiOperation(value = "获取文件内容", notes = "获取文件内容", httpMethod = "POST")
    @PostMapping("/getFileContent")
    public GraceJSONResult getFileContent(@RequestParam String relativePath);

    @ApiOperation(value = "获取文件树", notes = "获取文件树", httpMethod = "POST")
    @PostMapping("/getFileTree")
    public GraceJSONResult getFileTree(@RequestParam String relativePath);
}
