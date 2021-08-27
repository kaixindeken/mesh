package com.tanklab.mathless.pojo.bo;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RunBO {

    private String userName;

    @NotNull(message = "请选择文件夹")
    private String folder;

    private List<String> packages;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }
}
