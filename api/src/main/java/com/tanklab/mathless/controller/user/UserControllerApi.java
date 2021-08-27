package com.tanklab.mathless.controller.user;

import com.tanklab.mathless.constant.ServiceList;
import com.tanklab.mathless.pojo.bo.UserCheckBo;
import com.tanklab.mathless.pojo.bo.UserInfoBO;
import com.tanklab.mathless.pojo.bo.UserRegisterBO;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@Api(value = "用户操作", tags = "用户操作 controller")
@RequestMapping("/api/user")
@FeignClient(value = ServiceList.USER, url = "http://user:8081")
public interface UserControllerApi {

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public GraceJSONResult register(@RequestBody @Valid UserRegisterBO userRegisterBO) throws IOException;

    @ApiOperation(value = "获取用户个人信息", notes = "获取用户个人信息", httpMethod = "GET")
    @GetMapping("/profile")
    public GraceJSONResult getUserProfile();

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public GraceJSONResult login(@RequestBody @Valid UserCheckBo userCheckBo);

    @ApiOperation(value = "用户登出", notes = "用户登出", httpMethod = "GET")
    @GetMapping("/logout")
    public GraceJSONResult logout();

    @ApiOperation(value = "用户注销", notes = "用户注销", httpMethod = "POST")
    @PostMapping("/deleteUser")
    public GraceJSONResult deleteUser(@RequestBody @Valid UserCheckBo userCheckBo);

    @ApiOperation(value = "查询用户", notes = "查询用户", httpMethod = "POST")
    @PostMapping("/selectUsers")
    public GraceJSONResult selectUsers(@RequestBody @Valid UserInfoBO userInfoBO);

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    @PostMapping("/updateUser")
    public GraceJSONResult updateUser(@RequestBody @Valid UserInfoBO userInfoBO);
}
