package com.tanklab.mathless.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.Parameter;

import java.util.List;

public class CompleteInfo {

    private String value;

    private Double score;

    private String meta;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public static List<Parameter> convertParams(String params) throws JsonProcessingException {
        ObjectMapper ob = new ObjectMapper();
        // 将json对象转化为实体类，比如parameter的list列表（param_name 和 param_type）
        if("".equals(params.trim())) return null;
        return ob.readValue(params, new TypeReference<List<Parameter>>() {});
    }

    public static String convertToMetaString(List<Parameter> parameterList) {
        String preFix = "(";
        String postFix = ")";

        if (parameterList == null || parameterList.size() < 1) {
            return preFix + postFix;
        }
        Parameter firstParameter = parameterList.get(0);
        // use python typing hint format
        String firstParam = firstParameter.getParamName() + " :" + firstParameter.getParamType();
        StringBuilder result = new StringBuilder(preFix + firstParam);

        for (int i = 1; i < parameterList.size(); i++) {
            Parameter temp = parameterList.get(i);
            result.append(", ");
            result.append(temp.getParamName());
            result.append(" :");
            result.append(temp.getParamType());
        }

        result.append(postFix);
        // 返回（name:type, name:type的列表）
        return result.toString();
    }

    public static CompleteInfo FromFunctions(Functions functions) throws JsonProcessingException {
        CompleteInfo res = new CompleteInfo();
        res.value = functions.getName();

        List<Parameter> parameterList = convertParams(functions.getParams());
        res.meta = convertToMetaString(parameterList);

        res.score = 1.0;
        //包括 函数名， 函数参数表，函数权重
        return res;
    }
}
