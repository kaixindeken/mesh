package com.tanklab.mathless.pojo;

import javax.persistence.*;
import java.util.Date;

public class Functions {

    @Id
    private Long id;

    /**
     * 函数名
     */
    private String name;

    @Column(name = "real_name")
    private String realName;

    /**
     * 函数说明
     */
    private String description;

    /**
     * 函数编辑使用的语言
     */
    private String lang;


    /**
     * 函数参数（使用对象数组储存）
     */
    private String params;

    private String category;

    /**
     * 分配内存
     */
    private Long memory;

    /**
     * 超时时间
     */
    private Long timeout;

    @Column(name = "gitlab_project")
    private Long gitlabProject;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取函数名
     *
     * @return name - 函数名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置函数名
     *
     * @param name 函数名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取函数编辑使用的语言
     *
     * @return lang - 函数编辑使用的语言
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置函数编辑使用的语言
     *
     * @param lang 函数编辑使用的语言
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取分配内存
     *
     * @return memory - 分配内存
     */
    public Long getMemory() {
        return memory;
    }

    /**
     * 设置分配内存
     *
     * @param memory 分配内存
     */
    public void setMemory(Long memory) {
        this.memory = memory;
    }

    /**
     * 获取超时时间
     *
     * @return timeout - 超时时间
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return gitlab_project
     */
    public Long getGitlabProject() {
        return gitlabProject;
    }

    /**
     * @param gitlabProject
     */
    public void setGitlabProject(Long gitlabProject) {
        this.gitlabProject = gitlabProject;
    }

    /**
     * @return created_at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return updated_at
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return deleted_at
     */
    public Date getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt
     */
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * 获取函数说明
     *
     * @return description - 函数说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置函数说明
     *
     * @param description 函数说明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取函数参数（使用对象数组储存）
     *
     * @return params - 函数参数（使用对象数组储存）
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置函数参数（使用对象数组储存）
     *
     * @param params 函数参数（使用对象数组储存）
     */
    public void setParams(String params) {
        this.params = params;
    }
}