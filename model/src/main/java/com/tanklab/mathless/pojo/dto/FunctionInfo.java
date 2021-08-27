package com.tanklab.mathless.pojo.dto;

import com.tanklab.mathless.pojo.Functions;

public class FunctionInfo {
    private Long id;

    private String name;

    private String description;

    private String lang;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static FunctionInfo mapFrom(Functions function) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.id = function.getId();
        functionInfo.name = function.getName();
        functionInfo.description = function.getDescription();
        functionInfo.lang = function.getLang();
        return functionInfo;
    }
}
