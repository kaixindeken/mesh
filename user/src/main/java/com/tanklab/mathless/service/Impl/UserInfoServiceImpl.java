package com.tanklab.mathless.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.constant.RedisTimeConstant;
import com.tanklab.mathless.mapper.UserInfoMapper;
import com.tanklab.mathless.pojo.HostHolder;
import com.tanklab.mathless.pojo.LoginToken;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.UserCheckBo;
import com.tanklab.mathless.pojo.bo.UserInfoBO;
import com.tanklab.mathless.pojo.bo.UserRegisterBO;
import com.tanklab.mathless.service.UserInfoService;
import com.tanklab.mathless.utils.*;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import com.tanklab.mathless.utils.graceful.result.ResponseStatusEnum;
import com.tanklab.mathless.utils.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisOperatorUtil redisOperatorUtil;

    @Resource
    private Sid sid;

    @Resource
    HostHolder hostHolder;

    @Override
    public UserInfo createUser(UserInfo userInfo) throws IOException {

        userInfo.setId(sid.nextShort());
        userInfo.setPermissionLevel(2); // 默认为 2，即普通用户权限
        userInfo.setStatus(0);
        userInfo.setAccountBalance("0.0");
        userInfo.setRegisteredAt(new Date());
        userInfo.setLoggedInRecently(new Date());

        // 生成并创建用户目录
        String sub_dir = userInfo.getId() + "_" + FileOperatorUtil.getRandomString(8);
        String user_dir = "workspaces" + File.separator + "userspace" + File.separator + sub_dir;
        String console_dir = user_dir + File.separator + ".console";
        String dep_dir = "deployspaces" + File.separator + sub_dir;
        String console_file = console_dir + File.separator + "console-1.ipynb";
        File dir = new File(user_dir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File cdir = new File(console_dir);
        if (!cdir.exists()) {
            cdir.mkdirs();
        }
        File ddir = new File(dep_dir);
        if (!ddir.exists()) {
            ddir.mkdirs();
        }
        File cf = new File(console_file);
        if (!cf.exists()) {
            cf.createNewFile();
        }
        userInfo.setUserDir(user_dir);
        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    /**
     * 通过用户名或邮箱查找用户
     */
    @Override
    public UserInfo getUserInfo(String str) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("name", str.trim());
        userCriteria.orEqualTo("email", str.trim());
        return userInfoMapper.selectOneByExample(userExample);
    }

    /**
     * 模糊查询
     */
    @Override
    public List<UserInfo> getUsersByPartialName(String name) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andLike("name", "%" + name.trim() + "%");
        return userInfoMapper.selectByExample(userExample);
    }

    /**
     * 查找数量
     */
    @Override
    public int selectUserCount(String type, String str) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo(type, str.trim());
        return userInfoMapper.selectCountByExample(userExample);
    }

    /**
     * 删除用户，通过username或者email
     */
    @Override
    public void deleteUser(String str) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("name", str.trim());
        userCriteria.orEqualTo("email", str.trim());
        int result = userInfoMapper.deleteByExample(userExample);
        if (result != 1) {
            GraceJSONResult.exception(ResponseStatusEnum.FAILED);
        }
    }

    /**
     * 通过要求查询用户
     */
    @Override
    public List<UserInfo> getUsers(UserInfoBO userInfoBO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoBO, userInfo);
        return userInfoMapper.select(userInfo);
    }

    /**
     *  修改用户信息
     */
    @Override
    public void updateUserInfo(UserInfoBO userInfoBO){
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoBO, userInfo);
        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        if(result != 1){
            GraceJSONResult.exception(ResponseStatusEnum.FAILED);
        }
    }

    /**
     * 获取用户的信息
     * @param id
     * @return
     */
    @Override
    public UserInfo findUserById(String id){
    	UserInfo userInfo = getCache(id);
    	if(userInfo == null){
    		userInfo = initCache(id);
    	}
    	return userInfo;
    }

    @Override
    public LoginToken findLoginToken(String token) {
        String redisKey = RedisKeyUtil.getTokenKey(token);
        return (LoginToken) redisOperatorUtil.get(redisKey);
    }

    @Override
    public Message register(UserRegisterBO userRegisterBO, HttpServletRequest request) throws IOException {

        if(selectUserCount("name", userRegisterBO.getName()) > 0)
            return new Message(false, "该用户名已经存在");

        if(selectUserCount("email", userRegisterBO.getEmail()) > 0)
            return new Message(false, "该邮箱已被注册");

        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userRegisterBO, info);

        //对密码进行加密
        String salt = ToolUtil.generateUUID().substring(0, 6);
        info.setPassword(EncryptUtil.encrypt(userRegisterBO.getPassword(), salt));
        info.setSalt(salt);
        info.setIpRecently(ToolUtil.getIp(request));
        UserInfo userInfo = createUser(info);

        //给注册用户发送激活邮件, 未来可以实现
        return new Message(true, "注册成功");
    }

    @Override
    public Message login(UserCheckBo userCheckBo, HttpServletRequest request) {

        UserInfo userSession = hostHolder.getUser();
        String token, name;
        if(userSession != null){
            // 说明携带正确的token进入的，则不需要刷新token，使用原来的即可。
            token = request.getHeader("Authentication-Token");
            name = userSession.getName();

        }else{
            // 验证账号
            UserInfo userInfo = getUserInfo(userCheckBo.getNameOrEmail());
            if (userInfo == null){
                return new Message(false, "用户不存在");
            }

            // 验证密码
            String ePassword = EncryptUtil.encrypt(userCheckBo.getPassword().trim(), userInfo.getSalt());
            if(!ePassword.equals(userInfo.getPassword())){
                return new Message(false, "密码错误");
            }

            // 凭证过期时间 （是否记住我）
            int expiredSeconds = userCheckBo.isRememberMe() ?
                    RedisTimeConstant.REMEMBER_EXPIRED_SECONDS : RedisTimeConstant.DEFAULT_EXPIRED_SECONDS;

            // 用户名和密码均正确，为用户生成登录凭证
            LoginToken loginToken = new LoginToken(
                    userInfo.getId(),
                    ToolUtil.generateUUID(),  // 随机凭证
                    0,  // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
                    new Date(System.currentTimeMillis() + RedisTimeConstant.DEFAULT_TOKEN_ACCESS_SECONDS * 1000), // 设置凭证到期时间
                    userCheckBo.isRememberMe()
            );

            // 将登录凭证存入 redis中，设置超时时间
            String redisKey = RedisKeyUtil.getTokenKey(loginToken.getToken());
            redisOperatorUtil.set(redisKey, loginToken, expiredSeconds);

            name = userInfo.getName();
            token = loginToken.getToken();

            // 更新最近登录时间和登录ip
            userInfo.setLoggedInRecently(new Date());
            userInfo.setIpRecently(ToolUtil.getIp(request));
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }

        // 创建Json对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("token", token);

        // 手动返回json格式，包括用户名，和token
        return new Message(true, "登录成功", jsonObject);
    }

    /**
     * 用户退出(将凭证状态设置为无效)
     */
    @Override
    public Message logout(String token) {

        String redisKey = RedisKeyUtil.getTokenKey(token);
        LoginToken loginToken = (LoginToken) redisOperatorUtil.get(redisKey);

        if(loginToken == null){
            return new Message(false, "用户认证已失效");
        }

        loginToken.setStatus(1);
        redisOperatorUtil.del(redisKey);
        return new Message(true, "用户退出成功");
    }

    @Override
    public UserInfo getUserProfile() {
        return hostHolder.getUser();
    }

    // 优先从缓存中取值
    private UserInfo getCache(String userId){
    	String redisKey = RedisKeyUtil.getUserKey(userId);
    	return (UserInfo) redisOperatorUtil.get(redisKey);
    }

    // 缓存中没有该用户信息时，则将其存入缓存
    private UserInfo initCache(String userId){
        // 从数据库中查询
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("id", userId.trim());
    	UserInfo userInfo = userInfoMapper.selectOneByExample(userExample);
    	String redisKey = RedisKeyUtil.getUserKey(userId);
    	redisOperatorUtil.set(redisKey, userInfo, RedisTimeConstant.USER_CACHE_SECONDS);
    	return userInfo;
    }

    private void clearCache(String userId){
    	String redisKey = RedisKeyUtil.getUserKey(userId);
    	redisOperatorUtil.del(redisKey);
    }
}
