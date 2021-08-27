package com.tanklab.mathless.service.Impl;

import com.tanklab.mathless.exception.MyCustomException;
import com.tanklab.mathless.mapper.CategoryMapper;
import com.tanklab.mathless.mapper.DeployRecordMapper;
import com.tanklab.mathless.mapper.FunctionsMapper;
import com.tanklab.mathless.mapper.TableMetaMapper;
import com.tanklab.mathless.pojo.Category;
import com.tanklab.mathless.pojo.DeployRecord;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.TableMeta;
import com.tanklab.mathless.pojo.bo.DeployBO;
import com.tanklab.mathless.pojo.bo.DeployEntryBo;
import com.tanklab.mathless.pojo.bo.FunctionEntryBo;
import com.tanklab.mathless.pojo.dto.DeployEntryRes;
import com.tanklab.mathless.pojo.dto.FunctionInfo;
import com.tanklab.mathless.service.DeployService;
import com.tanklab.mathless.utils.graceful.result.ResponseStatusEnum;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeployServiceImpl implements DeployService {

    public static final String K8S_CONFIG_PATH =
            "src"
                    + File.separator
                    + "main"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "k8s"
                    + File.separator
                    + "config";

    public static final String K8S_CONFIG_PATH_RUN = File.separator + "kube-config";

    @Resource FunctionsMapper functionsMapper;

    @Resource DeployRecordMapper deployRecordMapper;

    @Resource CategoryMapper categoryMapper;

    @Resource TableMetaMapper tableMetaMapper;

    static final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void insertDBEntry(DeployBO deployBO, Long gitlabProject) {
        // todo: add transaction support
        Functions function = DeployBO.mapToFunctions(deployBO);
        function.setCreatedAt(new Date());
        function.setGitlabProject(gitlabProject);
        functionsMapper.insert(function);

        DeployRecord deployRecord = new DeployRecord();
        deployRecord.setCreatedAt(new Date());
        // todo: receive record from frontend
        deployRecord.setRecord("test");
        // missing field: user, record
        deployRecordMapper.insert(deployRecord);
    }

    @Override
    public void updateDBEntry(DeployBO deployBO) {
        Functions function = DeployBO.mapToFunctions(deployBO);
        function.setUpdatedAt(new Date());
        functionsMapper.updateByPrimaryKeySelective(function);

        String functionTableName = "functions";
        TableMeta tableMeta = new TableMeta();
        tableMeta.setTableName(functionTableName);
        tableMeta.setLastUpdate(new Date());
        tableMetaMapper.updateByPrimaryKey(tableMeta);
    }

    public DeployEntryBo getDeployEntryBO(DeployBO deployBO) {
        FunctionEntryBo functionEntryBo = FunctionEntryBo.mapFrom(deployBO.getInfo());
        if (functionEntryBo.getId() != null) {
            // select gitlab_project (field of category) from DB
            Functions function = functionsMapper.selectByPrimaryKey(functionEntryBo.getId());
            if (function == null) {
                return null;
            }
            functionEntryBo.setGitlab_project(function.getGitlabProject());
        } else {
            // set default value
            functionEntryBo.setGitlab_project(-1L);
        }
        DeployEntryBo deployEntryBo = DeployEntryBo.mapFrom(deployBO);
        deployEntryBo.setInfo(functionEntryBo);
        // select gitlab_group (field of category) from DB
        Category result = categoryMapper.selectByPrimaryKey(deployBO.getCategory());
        deployEntryBo.setCategory(result.getGitlabGroup());

        return deployEntryBo;
    }

    @Override
    public Long deployToOpenFaaS(DeployBO deployBO) {
        // todo: get from env? else, use default value
        String deployUrl = "http://192.168.1.6:31112/function/deploy-entry-python";

        DeployEntryBo deployEntryBo = getDeployEntryBO(deployBO);
        if (deployEntryBo == null) {
            // for updating function, no existing table entry found
            throw new MyCustomException(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        HttpEntity<DeployEntryBo> request = new HttpEntity<>(deployEntryBo);
        // if request failed, the method will throw
        // org.springframework.web.client.RestClientException Exception
        DeployEntryRes entryRes =
                restTemplate.postForObject(deployUrl, request, DeployEntryRes.class);
        // if response is illegal, manually throw exception
        if (entryRes == null || !entryRes.getUpload_status().equals("success")) {
            // todo: throw more useful exception
            throw new MyCustomException(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        // return database function table gitlab_project field
        return entryRes.getGitlab_project();
    }

    @Override
    public List<FunctionInfo> getAllFunctionInfo() {
        List<Functions> functions = functionsMapper.selectAll();
        List<FunctionInfo> functionInfos = new ArrayList<>();
        functions.forEach(functions1 -> functionInfos.add(FunctionInfo.mapFrom(functions1)));
        return functionInfos;
    }

    @Override
    public FunctionInfo getFunctionInfo(Long id) {
        Functions function = functionsMapper.selectByPrimaryKey(id);
        return FunctionInfo.mapFrom(function);
    }

    @Override
    public ApiClient connectToCluster() throws IOException {
        ApiClient apiClient =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(K8S_CONFIG_PATH_RUN)))
                        .build();
        Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }
}
