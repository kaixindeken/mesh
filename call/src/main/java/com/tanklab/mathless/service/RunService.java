package com.tanklab.mathless.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.kubernetes.client.openapi.ApiClient;

import java.io.IOException;
import java.util.List;

public interface RunService {

    ApiClient connectToCluster() throws IOException;

    String getRunDependency(String category);

    List<JSONObject> getRunDependencies(JSONArray packages);

    List<JSONObject> getDependencies();

    String getUserPath(String userName, String folder);
}
