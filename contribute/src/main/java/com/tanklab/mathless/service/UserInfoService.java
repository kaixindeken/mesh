package com.tanklab.mathless.service;

import com.tanklab.mathless.pojo.LoginToken;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.UserCheckBo;
import com.tanklab.mathless.pojo.bo.UserInfoBO;
import com.tanklab.mathless.pojo.bo.UserRegisterBO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface UserInfoService {

    // 1. 创建用户
    public UserInfo createUser(UserInfo userInfo) throws IOException;

    // 2. 通过用户名/邮箱获取用户信息
    public UserInfo getUserInfo(String str);

    // 3. 模糊查询用户
    public List<UserInfo> getUsersByPartialName(String name);

    // 4. 通过属性查询用户数量
    public int selectUserCount(String type, String str);

    // 5. 删除用户
    public void deleteUser(String username);

    // 6. 通过id查询用户
    public UserInfo findUserById(String id);

    // 7. 通过token至redis中查询token对象，用以获取用户信息
    public LoginToken findLoginToken(String token);

    // 10. 注册方法
    public Message register(UserRegisterBO userRegisterBO, HttpServletRequest request) throws IOException;

    // 11. 登录
    public Message login(UserCheckBo userCheckBo, HttpServletRequest request);

    // 12. 登出
    public Message logout(String ticket);

    //14. 根据条件查询users
    public List<UserInfo> getUsers(UserInfoBO userInfoBO);

    //15. 修改用户信息
    public void updateUserInfo(UserInfoBO userInfoBO);
}
