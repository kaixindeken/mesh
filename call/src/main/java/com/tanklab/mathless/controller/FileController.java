package com.tanklab.mathless.controller;

import com.tanklab.mathless.controller.call.FileControllerApi;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.bo.FileBO;
import com.tanklab.mathless.service.FileService;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileController extends BaseController implements FileControllerApi {

    @Resource
    private FileService fileService;

    @Resource
    private HttpServletResponse response;

    @Override
    public GraceJSONResult upload(MultipartFile[] files) {

        for(MultipartFile file : files){
            Message message = fileService.upload(file);
            if (!message.isSuccess()) {
                return GraceJSONResult.errorMsg(message.getInfo());
            }
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFileTree(String relativePath){
        return GraceJSONResult.ok(fileService.getFileTree(relativePath));
    }

    @Override
    public void download(String relativePath) {
        fileService.download(relativePath, response);
    }

    @Override
    public GraceJSONResult create(String relativePath, boolean isFile) throws IOException, InterruptedException {
        Message message = fileService.create(relativePath, isFile);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult delete(String relativePath) {
        Message message = fileService.delete(relativePath);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult saveFile(FileBO fileBO) {
        Message message = fileService.saveFile(fileBO);
        if (message.isSuccess()) {
            return GraceJSONResult.ok(message.getInfo());
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult getFileContent(String relativePath) {
        Message message = fileService.getFileContent(relativePath);
        if (message.isSuccess()) {
            return GraceJSONResult.ok(message.getInfo());
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }
}
