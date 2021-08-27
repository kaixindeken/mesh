package com.tanklab.mathless.pojo.dto;

public class DeployEntryRes {
    private Long gitlab_project;

    private String upload_status;

    private String deploy_status;

    public Long getGitlab_project() {
        return gitlab_project;
    }

    public void setGitlab_project(Long gitlab_project) {
        this.gitlab_project = gitlab_project;
    }

    public String getUpload_status() {
        return upload_status;
    }

    public void setUpload_status(String upload_status) {
        this.upload_status = upload_status;
    }

    public String getDeploy_status() {
        return deploy_status;
    }

    public void setDeploy_status(String deploy_status) {
        this.deploy_status = deploy_status;
    }
}
