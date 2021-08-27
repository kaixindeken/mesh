package com.tanklab.mathless.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parameter {
    @JsonProperty("param_name")
    private String paramName;

    @JsonProperty("param_type")
    private String paramType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
