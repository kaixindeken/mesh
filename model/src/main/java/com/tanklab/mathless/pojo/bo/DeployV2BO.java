package com.tanklab.mathless.pojo.bo;

import javax.validation.constraints.NotNull;

public class DeployV2BO {

    @NotNull(message = "请指定仓库")
    private String repo;

    @NotNull(message = "请指定路径")
    private String dir;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
