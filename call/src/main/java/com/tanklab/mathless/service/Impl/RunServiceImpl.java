package com.tanklab.mathless.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.mapper.CategoryMapper;
import com.tanklab.mathless.mapper.FunctionsMapper;
import com.tanklab.mathless.mapper.UserInfoMapper;
import com.tanklab.mathless.pojo.Category;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.service.RunService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RunServiceImpl implements RunService {

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

    @Resource CategoryMapper categoryMapper;

    @Resource UserInfoMapper userInfoMapper;

    @Override
    public List<JSONObject> getDependencies() {
        List<JSONObject> packages = new ArrayList<>();
        Example categoryExample = new Example(Category.class);
        categoryExample.orderBy("id");
        List<Category> categories = categoryMapper.selectByExample(categoryExample);
        for (Category category : categories) {
            JSONObject pack = new JSONObject();
            pack.put("id", category.getId());
            pack.put("name", category.getName());
            pack.put("description", category.getDescription());
            packages.add(pack);
            System.out.println(pack.toString());
        }
        return packages;
    }

    @Override
    public String getUserPath(String userName, String folder) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("name", userName);
        return userInfoMapper.selectByExample(userExample).get(0).getUserDir();
    }

    @Override
    public ApiClient connectToCluster() throws IOException {
        ApiClient apiClient =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(K8S_CONFIG_PATH_RUN)))
                        .build();
        Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }

    @Override
    public String getRunDependency(String category) {
        String baseurl = "'http://192.168.1.6:31112/function/'";
        String baseDependency =
                "import requests\n"
                        + "import json\n"
                        + "\n"
                        + "class "
                        + category
                        + ":\n"
                        + "    def __init__(self):\n"
                        + "        self.base_url = "
                        + baseurl
                        + "\n\n";
        // 根据种类获取所有函数
        Example functionsExample = new Example(Functions.class);
        Example.Criteria functionsCriteria = functionsExample.createCriteria();
        Example categoryExample = new Example(Category.class);
        Example.Criteria categoryCriteria = categoryExample.createCriteria();
        categoryCriteria.andEqualTo("name", category);
        Long categoryId = categoryMapper.selectByExample(categoryExample).get(0).getId();
        functionsCriteria.andEqualTo("category", categoryId);
        List<Functions> functions = functionsMapper.selectByExample(functionsExample);

        // 生成依赖
        StringBuilder RunDependency = new StringBuilder(baseDependency);
        for (Functions function : functions) {
            String addDependencyModel =
                    "    def function1(self, name):\n"
                            + "        data = {}\n"
                            + "        return requests.post(url=self.base_url + 'function2', data=json.dumps(data)).text\n";
            String addDependency = addDependencyModel
                    .replace("function1", function.getName())
                    .replace("function2", function.getName().replace("_","-"));
            if (function.getParams() == null || function.getParams().equals("")) {
                addDependency = addDependency.replace("self, name", "self");
            } else {
                String params1 = "self";
                String params2 = "{";
                JSONArray paramsArray = JSONArray.parseArray(function.getParams());

                for (int i = 0; i < paramsArray.size(); i++) {
                    JSONObject jsonObject = paramsArray.getJSONObject(i);
                    String param_name = jsonObject.getString("param_name");
                    params1 = params1.concat(", ").concat(param_name);
                    params2 =
                            params2.concat("\"")
                                    .concat(param_name)
                                    .concat("\":")
                                    .concat(param_name);
                    if (i != paramsArray.size() - 1) {
                        params2 = params2.concat(",");
                    } else {
                        params2 = params2.concat("}");
                    }
                }
                addDependency = addDependency.replace("self, name", params1);
                addDependency = addDependency.replace("{}", params2);
            }

            RunDependency.append(addDependency);
        }
        return new String(RunDependency);
    }

    @Override
    public List<JSONObject> getRunDependencies(JSONArray packages) {
        // 存放所有依赖的列表
        List<JSONObject> filesObj = new ArrayList<>();
        for (int pi = 0; pi < packages.size(); pi++) {
            JSONObject fileObj = new JSONObject();
            fileObj.put("type", "text");
            String pack = packages.getString(pi).concat(".py");
            fileObj.put("name", pack);
            String runDependency = getRunDependency(packages.getString(pi));
            fileObj.put("content", runDependency);
            filesObj.add(fileObj);
        }
        return filesObj;
    }

}
