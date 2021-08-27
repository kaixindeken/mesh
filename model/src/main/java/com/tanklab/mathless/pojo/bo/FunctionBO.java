package com.tanklab.mathless.pojo.bo;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class FunctionBO {

    private Long id;

    @NotBlank(message = "请定义函数名")
    @JsonProperty("function_name")
    private String name;

    private String description;

    @NotBlank(message = "请选择语言")
    private String lang;

    private JSONArray params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONArray getParams() {
        return params;
    }

    public void setParams(JSONArray params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "FunctionBO{"
                + "id="
                + id
                + ", name='"
                + name
                + '\''
                + ", description='"
                + description
                + '\''
                + ", lang='"
                + lang
                + '\''
                + ", params="
                + params
                + '}';
    }
}
