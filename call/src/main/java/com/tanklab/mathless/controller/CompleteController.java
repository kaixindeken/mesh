package com.tanklab.mathless.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanklab.mathless.controller.call.CompleteControllerApi;
import com.tanklab.mathless.pojo.dto.CompleteInfo;
import com.tanklab.mathless.service.CompleteService;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
public class CompleteController implements CompleteControllerApi {

    @Resource private CompleteService completeService;

    @Override
    public GraceJSONResult getCompleteList() throws JsonProcessingException {
        List<CompleteInfo> completeInfoList = completeService.getCompleteInfoList();
        return GraceJSONResult.ok(completeInfoList);
    }

    @Override
    public GraceJSONResult getCompleteListVer() {
        Long version = completeService.getCompleteInfoListVer();
        HashMap<String, String> result = new HashMap<>();
        result.put("version", String.valueOf(version));
        return GraceJSONResult.ok(result);
    }
}
