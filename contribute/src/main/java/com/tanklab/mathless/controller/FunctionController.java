package com.tanklab.mathless.controller;

import com.tanklab.mathless.controller.contribute.FunctionControllerApi;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.bo.FileSaveBO;
import com.tanklab.mathless.service.FunctionService;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FunctionController extends BaseController implements FunctionControllerApi {

    @Resource
    private FunctionService functionService;

    @Override
    public GraceJSONResult upload(String functionName, String relativePath, MultipartFile[] files) {

        for(MultipartFile file : files){
            Message message = functionService.upload(functionName, relativePath, file);
            if (!message.isSuccess()) {
                return GraceJSONResult.errorMsg(message.getInfo());
            }
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFileTree(String functionName){
        return GraceJSONResult.ok(functionService.getFunDirStr(functionName));
    }

    @Override
    public void download(String functionName, String relativePath, HttpServletResponse response) {
        functionService.download(functionName, relativePath, response);
    }

    @Override
    public GraceJSONResult getOwnFunctions(String username) {
        Message message = functionService.getOwnFunctions(username);
        if(message.isSuccess()){
            return GraceJSONResult.ok(message.getData());
        }else{
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult create(String functionName, String relativePath, boolean isFile) throws IOException, InterruptedException {
        Message message = functionService.create(functionName, relativePath, isFile);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult createFunction(String functionName) throws IOException, InterruptedException {
        Message message = functionService.createFunction(functionName);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult delete(String functionName, String relativePath) {
        Message message = functionService.delete(functionName ,relativePath);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult deleteFunction(String functionName) throws IOException {
        Message message = functionService.deleteFunction(functionName);
        if (message.isSuccess()) {
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult saveFile(FileSaveBO fileSaveBO) {
        System.out.println(fileSaveBO);
        Message message = functionService.saveFile(fileSaveBO);
        if (message.isSuccess()) {
            return GraceJSONResult.ok(message.getInfo());
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult getFileContent(String functionName, String relativePath) {
        Message message = functionService.getFileContent(functionName, relativePath);
        if (message.isSuccess()) {
            return GraceJSONResult.ok(message.getInfo());
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

}
