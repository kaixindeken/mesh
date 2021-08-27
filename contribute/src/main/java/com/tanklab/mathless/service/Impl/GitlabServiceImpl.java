package com.tanklab.mathless.service.Impl;

import com.tanklab.mathless.service.GitlabService;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GitlabServiceImpl implements GitlabService {

    private GitlabAPI connect(){
        return GitlabAPI.connect("http://192.168.1.6","X1N1npB5pbxudVaZop7g");
    }

    @Override
    public Integer newProject(String function) throws IOException {
        GitlabAPI gitlabAPI = connect();
        GitlabGroup gitlabGroup = gitlabAPI.getGroup("model");
        GitlabProject gitlabProject = gitlabAPI.createProjectForGroup(function, gitlabGroup, "", "internal");
        return gitlabProject.getId();
    }

    @Override
    public void deleteProject(String function) throws IOException {
        GitlabAPI gitlabAPI = connect();
        GitlabProject gitlabProject = gitlabAPI.getProject("model", function);
        gitlabAPI.deleteProject(gitlabProject.getId());
    }

    @Override
    public String getUrlFromId(Integer id) throws IOException {
        GitlabAPI gitlabAPI = connect();
        GitlabProject gitlabProject = gitlabAPI.getProject(id);
        return gitlabProject.getHttpUrl();
    }

    @Override
    public String getImageFromId(Integer id) throws IOException {
        GitlabAPI gitlabAPI = connect();
        GitlabProject gitlabProject = gitlabAPI.getProject(id);
        String webUrl = gitlabProject.getWebUrl();
        String[] imgv = webUrl.split("://")[1].split("/");
        String image = "http://";
        for (int i = 0; i < imgv.length; i++) {
            if (i == 0){
                image += imgv[i];
                image += ":5555/";
            } else if (i == imgv.length - 1){
                image += imgv[i].split("\\.")[0];
                image += ":latest";
            } else {
                image += imgv[i];
                image += "/";
            }
        }
        return image;
    }
}
