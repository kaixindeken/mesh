package com.tanklab.mathless.pojo.bo;

import com.alibaba.fastjson.JSONArray;

public class DeployEntryBo {

    private FunctionEntryBo info;

    private Long category;

    private JSONArray files;

    public FunctionEntryBo getInfo() {
        return info;
    }

    public void setInfo(FunctionEntryBo info) {
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

    public static DeployEntryBo mapFrom(DeployBO deployBO) {
        DeployEntryBo deployEntryBo = new DeployEntryBo();
        deployEntryBo.setInfo(FunctionEntryBo.mapFrom(deployBO.getInfo()));
        deployEntryBo.setFiles(deployBO.getFiles());
        // missing field: category (select from db)
        return deployEntryBo;
    }
}
