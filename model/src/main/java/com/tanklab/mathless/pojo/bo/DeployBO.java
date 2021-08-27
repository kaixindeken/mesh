package com.tanklab.mathless.pojo.bo;

import com.alibaba.fastjson.JSONArray;
import com.tanklab.mathless.pojo.Functions;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeployBO {

    @Valid private FunctionBO info;

    @NotNull(message = "请选择分类")
    private Long category;

    @NotEmpty(message = "无文件")
    private JSONArray files;

    public FunctionBO getInfo() {
        return info;
    }

    public void setInfo(FunctionBO info) {
        this.info = info;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public JSONArray getFiles() {
        return files;
    }

    public void setFiles(JSONArray files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "DeployBO{" + "info=" + info + ", category=" + category + ", files=" + files + '}';
    }

    public static Functions mapToFunctions(DeployBO deployBO) {
        FunctionBO functionBO = deployBO.getInfo();

        Functions function = new Functions();
        function.setId(functionBO.getId());
        function.setName(functionBO.getName());
        function.setLang(functionBO.getLang());
        function.setDescription(functionBO.getDescription());
        function.setCategory(deployBO.getCategory().toString());
        // store json string in database table
        function.setParams(functionBO.getParams().toString());

        // missing field: created_at, gitlab_project, updated_at, deleted_at
        return function;
    }
}
