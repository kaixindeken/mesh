package com.tanklab.mathless.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "user_info")
public class UserInfo {
    @Id
    private String id;

    /**
     * 用户账号名称
     */
    private String name;

    /**
     * 用户电子邮箱

     */
    private String email;

    /**
     * 用户电话号码
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 权限等级 0-管理员 1-提供者 2-使用者
     */
    @Column(name = "permission_level")
    private Integer permissionLevel;

    /**
     * 用户状态 0-正常 1-停用
     */
    private Integer status;

    /**
     * 账号所剩余额
     */
    @Column(name = "account_balance")
    private String accountBalance;

    @Column(name = "registered_at")
    private Date registeredAt;

    /**
     * 最近登录时间
     */
    @Column(name = "logged_in_recently")
    private Date loggedInRecently;

    /**
     * 最近登录ip地址
     */
    @Column(name = "ip_recently")
    private String ipRecently;

    /**
     * 用户目录
     */
    @Column(name = "user_dir")
    private String userDir;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户账号名称
     *
     * @return name - 用户账号名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户账号名称
     *
     * @param name 用户账号名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户电子邮箱

     *
     * @return email - 用户电子邮箱

     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户电子邮箱

     *
     * @param email 用户电子邮箱

     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取用户电话号码
     *
     * @return phone_number - 用户电话号码
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置用户电话号码
     *
     * @param phoneNumber 用户电话号码
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取密码盐
     *
     * @return salt - 密码盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置密码盐
     *
     * @param salt 密码盐
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取权限等级 0-管理员 1-提供者 2-使用者
     *
     * @return permission_level - 权限等级 0-管理员 1-提供者 2-使用者
     */
    public Integer getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * 设置权限等级 0-管理员 1-提供者 2-使用者
     *
     * @param permissionLevel 权限等级 0-管理员 1-提供者 2-使用者
     */
    public void setPermissionLevel(Integer permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    /**
     * 获取用户状态 0-正常 1-停用
     *
     * @return status - 用户状态 0-正常 1-停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置用户状态 0-正常 1-停用
     *
     * @param status 用户状态 0-正常 1-停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取账号所剩余额
     *
     * @return account_balance - 账号所剩余额
     */
    public String getAccountBalance() {
        return accountBalance;
    }

    /**
     * 设置账号所剩余额
     *
     * @param accountBalance 账号所剩余额
     */
    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * @return registered_at
     */
    public Date getRegisteredAt() {
        return registeredAt;
    }

    /**
     * @param registeredAt
     */
    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    /**
     * 获取最近登录时间
     *
     * @return logged_in_recently - 最近登录时间
     */
    public Date getLoggedInRecently() {
        return loggedInRecently;
    }

    /**
     * 设置最近登录时间
     *
     * @param loggedInRecently 最近登录时间
     */
    public void setLoggedInRecently(Date loggedInRecently) {
        this.loggedInRecently = loggedInRecently;
    }

    /**
     * 获取最近登录ip地址
     *
     * @return ip_recently - 最近登录ip地址
     */
    public String getIpRecently() {
        return ipRecently;
    }

    /**
     * 设置最近登录ip地址
     *
     * @param ipRecently 最近登录ip地址
     */
    public void setIpRecently(String ipRecently) {
        this.ipRecently = ipRecently;
    }

    /**
     * 获取用户目录
     *
     * @return user_dir - 用户目录
     */
    public String getUserDir() {
        return userDir;
    }

    /**
     * 设置用户目录
     *
     * @param userDir 用户目录
     */
    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }
}