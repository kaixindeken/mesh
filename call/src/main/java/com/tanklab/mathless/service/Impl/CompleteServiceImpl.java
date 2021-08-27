package com.tanklab.mathless.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanklab.mathless.mapper.FunctionsMapper;
import com.tanklab.mathless.mapper.TableMetaMapper;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.TableMeta;
import com.tanklab.mathless.pojo.dto.CompleteInfo;
import com.tanklab.mathless.service.CompleteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class  CompleteServiceImpl implements CompleteService {

    @Resource FunctionsMapper functionsMapper;

    @Resource TableMetaMapper tableMetaMapper;

    @Override
    public List<CompleteInfo> getCompleteInfoList() throws JsonProcessingException {
        List<Functions> functionsList = functionsMapper.selectAll();

        List<CompleteInfo> completeInfoList = new ArrayList<>();
        for (Functions func : functionsList) {
            CompleteInfo completeInfo = CompleteInfo.FromFunctions(func);
            completeInfoList.add(completeInfo);
        }
        return completeInfoList;
    }

    @Override
    public Long getCompleteInfoListVer() {
        String functionTableName = "functions";
        TableMeta tableMeta = tableMetaMapper.selectByPrimaryKey(functionTableName);
        return tableMeta.getLastUpdate().getTime();
    }
}
