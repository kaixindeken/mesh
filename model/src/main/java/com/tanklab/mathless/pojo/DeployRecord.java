package com.tanklab.mathless.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "deploy_record")
public class DeployRecord {
    private Long id;

    /** 用户id */
    private Long user;

    @Column(name = "created_at")
    private Date createdAt;

    /** 提交记录 */
    private String record;

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @param id */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return user - 用户id
     */
    public Long getUser() {
        return user;
    }

    /**
     * 设置用户id
     *
     * @param user 用户id
     */
    public void setUser(Long user) {
        this.user = user;
    }

    /** @return created_at */
    public Date getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取提交记录
     *
     * @return record - 提交记录
     */
    public String getRecord() {
        return record;
    }

    /**
     * 设置提交记录
     *
     * @param record 提交记录
     */
    public void setRecord(String record) {
        this.record = record;
    }
}
