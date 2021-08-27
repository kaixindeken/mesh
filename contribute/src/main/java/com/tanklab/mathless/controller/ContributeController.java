package com.tanklab.mathless.controller;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.contribute.ContributeControllerApi;
import com.tanklab.mathless.pojo.bo.DeployV2BO;
import com.tanklab.mathless.pojo.bo.NewFunctionBO;
import com.tanklab.mathless.pojo.bo.UpdateFunctionBO;
import com.tanklab.mathless.pojo.dto.FunctionInfo;
import com.tanklab.mathless.service.*;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import com.tanklab.mathless.utils.idworker.Sid;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import org.joda.time.DateTime;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ContributeController extends BaseController implements ContributeControllerApi {

    @Resource private ModelService modelService;

    @Resource private DeployService deployService;

    @Resource private FunctionDBService functionDBService;

    @Resource private GitlabService gitlabService;

    @Resource private FileService fileService;

    @Override
    public GraceJSONResult functions() {
        return GraceJSONResult.ok(functionDBService.getAllFunctionsInfo());
    }

    @Override
    public GraceJSONResult deleteFunction(Long id) {
        functionDBService.deleteFunctionInDB(id);
        return GraceJSONResult.ok("删除函数成功");
    }

    @Override
    public GraceJSONResult models() {
        return GraceJSONResult.ok(modelService.getAllModelsInfo());
    }

    @Override
    public GraceJSONResult createFunction(@Valid NewFunctionBO functionInfoBO) throws IOException {
        String realName = functionInfoBO.getName() + "-" + Sid.next();
        //创建 gitlab 项目
        int gitlabId = gitlabService.newProject(realName);
        //创建数据库记录
        functionDBService.createFunctionInDB(functionInfoBO, realName, gitlabId);
        //创建文件夹
        String dir = fileService.NewFunction(realName, functionInfoBO.getLang());
        return GraceJSONResult.ok(dir);
    }

    @Override
    public GraceJSONResult getFunctionInfo(Long id) {
        FunctionInfo functionInfo = deployService.getFunctionInfo(id);
        return GraceJSONResult.ok(functionInfo);
    }

    @Override
    public GraceJSONResult updateFunction(UpdateFunctionBO functionInfoBO) throws IOException {
        functionDBService.updateFunctionInDB(functionInfoBO);

        return GraceJSONResult.ok("更新函数成功");
    }

    @Override
    public GraceJSONResult deployV2(DeployV2BO deployV2BO) throws IOException, ApiException {
        ApiClient apiClient = deployService.connectToCluster();
        CoreV1Api V1Api = new CoreV1Api(apiClient);
        String[] labels = deployV2BO.getDir().split("/");
        String func = labels[labels.length - 1];
        String user = labels[labels.length - 2];
        String metaName = "deploy-".concat(user.toLowerCase()).concat("-").concat(func.toLowerCase());
        String[] repoSplit = deployV2BO.getRepo().split("://");
        String compUrl = "http://" + "private_token:X1N1npB5pbxudVaZop7g@" + repoSplit[1];
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
        // 创建pod并执行部署操作
        List<V1Volume> v1Volumes = new ArrayList<>();
        V1Volume v1Volume =
                new V1Volume()
                        .name("deployspace")
                        .persistentVolumeClaim(
                                new V1PersistentVolumeClaimVolumeSource()
                                        .claimName("mathless-deployspaces-claim"));
        v1Volumes.add(v1Volume);
        List<V1VolumeMount> volumeMounts = new ArrayList<>();
        V1VolumeMount volumeMount = new V1VolumeMount().name("deployspace").mountPath("/deployspaces");
        volumeMounts.add(volumeMount);
        List<String> command = new ArrayList<>();
        command.add("/bin/sh");
        command.add("-c");
        List<String> args = new ArrayList<>();
        args.add(
                "cd "
                        .concat(deployV2BO.getDir())
                        .concat(" && git config --global user.name \"Administrator\"")
                        .concat(" && git config --global user.email \"admin@example.com\"")
                        .concat(" && if [ ! -e \".git/\" ]; then git init; git remote add origin " + compUrl + "; fi")
                        .concat(" && git add .")
                        .concat(" && git commit -m \"commit\"")
                        .concat(" && git push -u origin master"));
        List<V1Container> v1Containers = new ArrayList<>();
        V1Container container =
                new V1Container()
                        .name("deploy-container")
                        .image("alpine/git:latest")
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
                new GenericKubernetesApi<>(V1Pod.class, V1PodList.class, "", "v1", "pods", apiClient);
        podClient.create(body).throwsApiException().getObject();
        return GraceJSONResult.ok("Deploying");
    }

    @Override
    public GraceJSONResult deployStatus(String dir) throws IOException, ApiException {
        ApiClient client = deployService.connectToCluster();
        String[] labels = dir.split("/");
        String func = labels[labels.length - 1];
        String user = labels[labels.length - 2];
        String metaName = "deploy-".concat(user.toLowerCase()).concat("-").concat(func.toLowerCase());
        GenericKubernetesApi<V1Pod, V1PodList> podClient =
                new GenericKubernetesApi<>(V1Pod.class, V1PodList.class, "", "v1", "pods", client);
        V1Pod pod = podClient.get("default", metaName).throwsApiException().getObject();
        DateTime start;
        DateTime end;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", pod.getStatus().getPhase());
        if (pod.getStatus().getPhase().equals("Succeeded")) {
            start =
                    pod.getStatus()
                            .getContainerStatuses()
                            .get(0)
                            .getState()
                            .getTerminated()
                            .getStartedAt();
            end =
                    pod.getStatus()
                            .getContainerStatuses()
                            .get(0)
                            .getState()
                            .getTerminated()
                            .getFinishedAt();
            jsonObject.put("started_at", start.plusHours(8));
            jsonObject.put("ended_at", end.plusHours(8));
        } else if (pod.getStatus().getPhase().equals("Running")) {
            start =
                    pod.getStatus()
                            .getContainerStatuses()
                            .get(0)
                            .getState()
                            .getRunning()
                            .getStartedAt();
            jsonObject.put("started_at", start.plusHours(8));
        }
        jsonObject.put("uid", pod.getMetadata().getUid());
        if (pod.getSpec().getContainers().get(0).getResources().getLimits() == null){
            jsonObject.put("memory", "128MB");
        } else {
            jsonObject.put("memory", pod.getSpec().getContainers().get(0).getResources().getLimits());
        }
        return GraceJSONResult.ok(jsonObject);
    }
}
