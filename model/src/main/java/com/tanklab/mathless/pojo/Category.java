package com.tanklab.mathless.pojo;

import javax.persistence.*;
import java.util.Date;

public class Category {
    @Id private Long id;

    /** 分类名 */
    private String name;

    @Column(name = "gitlab_group")
    private Long gitlabGroup;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    /** 分类说明 */
    private String description;

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @param id */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取分类名
     *
     * @return name - 分类名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名
     *
     * @param name 分类名
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return gitlab_group */
    public Long getGitlabGroup() {
        return gitlabGroup;
    }

    /** @param gitlabGroup */
    public void setGitlabGroup(Long gitlabGroup) {
        this.gitlabGroup = gitlabGroup;
    }

    /** @return created_at */
    public Date getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /** @return updated_at */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /** @param updatedAt */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /** @return deleted_at */
    public Date getDeletedAt() {
        return deletedAt;
    }

    /** @param deletedAt */
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * 获取分类说明
     *
     * @return description - 分类说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置分类说明
     *
     * @param description 分类说明
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
