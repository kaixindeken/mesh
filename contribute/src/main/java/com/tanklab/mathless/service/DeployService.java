package com.tanklab.mathless.service;

import com.tanklab.mathless.pojo.bo.DeployBO;
import com.tanklab.mathless.pojo.dto.FunctionInfo;
import io.kubernetes.client.openapi.ApiClient;

import java.io.IOException;
import java.util.List;

public interface DeployService {
    // update DBEntry for function create operation
    void insertDBEntry(DeployBO deployBO, Long gitlabProject);

    // update DBEntry for function update operation
    void updateDBEntry(DeployBO deployBO);

    // invoke deploy function(deployed on openfaas)
    Long deployToOpenFaaS(DeployBO deployBO);

    List<FunctionInfo> getAllFunctionInfo();

    FunctionInfo getFunctionInfo(Long id);

    ApiClient connectToCluster() throws IOException;
}
