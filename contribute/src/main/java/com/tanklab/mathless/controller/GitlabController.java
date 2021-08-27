package com.tanklab.mathless.controller;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.contribute.GitlabControllerApi;
import com.tanklab.mathless.service.GitlabService;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class GitlabController implements GitlabControllerApi {

    @Resource
    private GitlabService gitlabService;

    @Override
    public GraceJSONResult create(String functionName) throws IOException {
        Integer id = gitlabService.newProject(functionName);
        String url = gitlabService.getUrlFromId(id);
        String image = gitlabService.getImageFromId(id);
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("url", url);
        object.put("image", image);
        return GraceJSONResult.ok(object);
    }

    @Override
    public GraceJSONResult delete(String functionName) throws IOException {
        gitlabService.deleteProject(functionName);
        return GraceJSONResult.ok();
    }
}
