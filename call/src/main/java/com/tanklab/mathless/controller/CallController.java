package com.tanklab.mathless.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.call.CallControllerApi;
import com.tanklab.mathless.pojo.LogPages;
import com.tanklab.mathless.pojo.bo.ConsoleBO;
import com.tanklab.mathless.pojo.bo.LogBO;
import com.tanklab.mathless.pojo.bo.OutputBO;
import com.tanklab.mathless.pojo.bo.RunBO;
import com.tanklab.mathless.service.RunService;
import com.tanklab.mathless.utils.FileOperatorUtil;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CallController implements CallControllerApi {
    @Resource
    private RunService runService;

    @Override
    public GraceJSONResult getDependencies() {
        return GraceJSONResult.ok(runService.getDependencies());
    }

    @Override
    public GraceJSONResult runInPod(RunBO runBO) throws IOException, ApiException, InterruptedException {
        // 获取运行需要的所有依赖
        JSONArray PackagesArray = JSONArray.parseArray(JSON.toJSONString(runBO.getPackages()));
        List<JSONObject> runDependencies = runService.getRunDependencies(PackagesArray);
        // 获取user_dir和用户名_随机数_目录字符串
        String userPath = runService.getUserPath(runBO.getUserName(), runBO.getFolder());
        String[] pathArray = userPath.split("/");
        Date date = new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = df.format(date);
        String nameRandomDate =
                pathArray[pathArray.length - 1].concat("_").concat(dateStr);
        String packagePath =
                "/workspaces/tmpspace".concat(File.separator).concat(nameRandomDate).concat(File.separator);

        File file = new File("workspaces/tmpspace");
        File[] files;
        files = file.listFiles();
        if (files != null && files.length != 0) {
            for (File f : files) {
                if (f.getName().indexOf(pathArray[pathArray.length - 1]) == 0) {
                    FileOperatorUtil.delete(f.getPath());
                }
            }
        }

        // 创建依赖目录
        packagePath = packagePath.replaceAll("\\\\", "/");
        FileOperatorUtil.create(packagePath, false);
        // 将生成的依赖文件存入目录
        for (JSONObject runDependency : runDependencies) {
            String name = (String) runDependency.get("name");
            String content = (String) runDependency.get("content");
            FileOutputStream fileOutputStream = new FileOutputStream(packagePath.concat(name));
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        }

        ApiClient apiClient = runService.connectToCluster();
        CoreV1Api V1Api = new CoreV1Api(apiClient);
        String metaName = "run-".concat(runBO.getUserName().toLowerCase()).concat("-").concat(runBO.getFolder().replace(File.separatorChar, '-').toLowerCase());
        // 若存在同名pod则删除
        try {
            V1PodList podList =
                    V1Api.listNamespacedPod(
                            "default",
                            null,
                            false,
                            null,
                            "metadata.name=".concat(metaName).concat(",metadata.namespace=default"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
            List<V1Pod> pods = podList.getItems();
            if (!pods.isEmpty()) {
                V1Api.deleteNamespacedPod(metaName, "default", null, null, null, null, null, null);
            }

        } catch (ApiException e) {
            e.printStackTrace();
        }

        // 创建pod并运行
        List<V1Volume> v1Volumes = new ArrayList<>();
        V1Volume v1Volume =
                new V1Volume()
                        .name("workspaces")
                        .persistentVolumeClaim(
                                new V1PersistentVolumeClaimVolumeSource()
                                        .claimName("mathless-workspaces-claim"));
        v1Volumes.add(v1Volume);
        List<V1VolumeMount> volumeMounts = new ArrayList<>();
        V1VolumeMount volumeMount = new V1VolumeMount().name("workspaces").mountPath("/workspaces");
        volumeMounts.add(volumeMount);
        List<String> command = new ArrayList<>();
        command.add("/bin/sh");
        command.add("-c");
        List<String> args = new ArrayList<>();
        args.add(
                "cd "
                        .concat(File.separator + userPath)
                        .concat(" && export PYTHONPATH=")
                        .concat(packagePath)
                        .concat(" && python ")
                        .concat(runBO.getFolder()));

        List<V1Container> v1Containers = new ArrayList<>();
        V1Container container =
                new V1Container()
                        .name("run-container")
                        .image("192.168.1.6:5555/customize-images/python:3.8-mathless")
                        .volumeMounts(volumeMounts)
                        .command(command)
                        .args(args);
        v1Containers.add(container);

        V1Pod body =
                new V1Pod()
                        .apiVersion("v1")
                        .kind("Pod")
                        .metadata(new V1ObjectMeta().name(metaName).namespace("default"))
                        .spec(
                                new V1PodSpec()
                                        .restartPolicy("Never")
                                        .volumes(v1Volumes)
                                        .containers(v1Containers));

        GenericKubernetesApi<V1Pod, V1PodList> podClient =
                new GenericKubernetesApi<>(V1Pod.class, V1PodList.class,
                        "", "v1", "pods", apiClient);
        podClient.create(body).throwsApiException().getObject();
        return GraceJSONResult.ok("Running");
    }

    @Override
    public GraceJSONResult output(OutputBO outputBO) throws IOException {
        ApiClient apiClient = runService.connectToCluster();
        CoreV1Api V1Api = new CoreV1Api(apiClient);
        String metaName = "console-";
        //存在同名 pod 删除
        try {
            V1PodList podList =
                    V1Api.listNamespacedPod(
                            "default",
                            null,
                            false,
                            null,
                            "metadata.name=".concat(metaName).concat(",metadata.namespace=default"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
            List<V1Pod> pods = podList.getItems();
            if (!pods.isEmpty()) {
                V1Api.deleteNamespacedPod(metaName, "default", null, null, null, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GraceJSONResult execConsole(ConsoleBO consoleBO) {
        return null;
    }

    @Override
    public GraceJSONResult logs(LogBO logBO) {
        JSONObject object = LogPages.getInstance().readLog(logBO);
        if (object.containsKey("error")) return GraceJSONResult.errorMsg("该id不存在");
        else return GraceJSONResult.ok(object);
    }
}
