package com.tanklab.mathless.pojo.bo;

public class FunctionEntryBo {

    private Long id;

    private String function_name;

    private String description;

    private String lang;

    private Long gitlab_project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getGitlab_project() {
        return gitlab_project;
    }

    public void setGitlab_project(Long gitlab_project) {
        this.gitlab_project = gitlab_project;
    }

    public static FunctionEntryBo mapFrom(FunctionBO functionBO) {
        FunctionEntryBo functionEntryBo = new FunctionEntryBo();
        functionEntryBo.setId(functionBO.getId());
        functionEntryBo.setFunction_name(functionBO.getName());
        functionEntryBo.setLang(functionBO.getLang());
        functionEntryBo.setDescription(functionBO.getDescription());
        // missing field: gitlab_project(select from db / default value -1)
        return functionEntryBo;
    }
}
