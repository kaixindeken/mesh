package com.tanklab.mathless.pojo.bo;

import javax.validation.constraints.NotNull;

public class LogBO {

    private String userName;

    @NotNull(message = "请提供日志id")
    private String id;

    private int pageNum = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
