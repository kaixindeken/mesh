package com.tanklab.mathless.service;

import java.io.IOException;

public interface GitlabService {

    //新建仓库
    public Integer newProject(String function) throws IOException;

    //删除仓库
    public void deleteProject(String function) throws IOException;

    //根据id找url
    public String getUrlFromId(Integer id) throws IOException;

    //根据id找image
    public String getImageFromId(Integer id) throws IOException;
}
