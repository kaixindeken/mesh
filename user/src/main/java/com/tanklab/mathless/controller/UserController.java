package com.tanklab.mathless.controller;

import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.UserCheckBo;
import com.tanklab.mathless.pojo.bo.UserInfoBO;
import com.tanklab.mathless.pojo.bo.UserRegisterBO;
import com.tanklab.mathless.service.UserInfoService;
import com.tanklab.mathless.utils.EncryptUtil;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Resource private UserInfoService userInfoService;

    @Resource private HttpServletRequest request;

    @Override
    public GraceJSONResult register(UserRegisterBO userRegisterBO) throws IOException {

        //Message checkCaptcha = userInfoService.checkKaptcha(kaptchaOwner, userRegisterBO.getCode());
        //if(!checkCaptcha.isSuccess()) return GraceJSONResult.errorMsg("验证码错误");

        Message message = userInfoService.register(userRegisterBO, request);
        if(message.isSuccess()){
            return GraceJSONResult.ok(message.getInfo());
        }else{
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult getUserProfile() {
        return GraceJSONResult.ok(userInfoService.getUserProfile());
    }

    @Override
    public GraceJSONResult login(UserCheckBo userCheckBo) {

        Message message = userInfoService.login(userCheckBo, request);
        if(message.isSuccess()){
            //response.setHeader("Authentication-Token", message.getInfo());
            return GraceJSONResult.ok(message.getData());
        }else{
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult logout() {
        String token = request.getHeader("Authentication-Token");
        Message message = userInfoService.logout(token);
        if(message.isSuccess()){
            return GraceJSONResult.ok("登出成功");
        } else {
            return GraceJSONResult.errorMsg(message.getInfo());
        }
    }

    @Override
    public GraceJSONResult deleteUser(UserCheckBo userCheckBo) {

        UserInfo userInfo = userInfoService.getUserInfo(userCheckBo.getNameOrEmail());

        if (userInfo == null) {
            return GraceJSONResult.errorMsg("不存在该用户");
        }

        String ePassword = EncryptUtil.encrypt(userCheckBo.getPassword().trim(), userInfo.getSalt());

        if(ePassword.equals(userInfo.getPassword())){
            return GraceJSONResult.ok("删除成功");
        } else {
            return GraceJSONResult.errorMsg("密码不正确");
        }
    }

    @Override
    public GraceJSONResult selectUsers(UserInfoBO userInfoBO) {
        List<UserInfo> userInfoList = userInfoService.getUsers(userInfoBO);
        return GraceJSONResult.ok(userInfoList);
    }

    @Override
    public GraceJSONResult updateUser(UserInfoBO userInfoBO) {
        userInfoService.updateUserInfo(userInfoBO);
        return GraceJSONResult.ok("更新成功");
    }
}
