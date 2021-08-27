package com.tanklab.mathless.interceptor;

import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.constant.RedisTimeConstant;
import com.tanklab.mathless.constant.RequestUrlConstant;
import com.tanklab.mathless.pojo.HostHolder;
import com.tanklab.mathless.pojo.LoginToken;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.service.UserInfoService;
import com.tanklab.mathless.utils.RedisKeyUtil;
import com.tanklab.mathless.utils.RedisOperatorUtil;
import com.tanklab.mathless.utils.ToolUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class TokenInterceptor implements HandlerInterceptor {

		@Resource private UserInfoService userInfoService;

		@Resource private HostHolder hostHolder;

		@Resource private RedisOperatorUtil redisOperatorUtil;

		@Resource private UserControllerApi userControllerApi;

		// 在返回时添加新token
		private static String resToken = "";
		private static boolean isFresh = false;

		/**
		 * 在Controller 执行之前被调用
		 */
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
			// 从 request请求中获取凭证
			String token = request.getHeader("Authentication-Token");
			if(token != null){
				// 查询token凭证
				LoginToken loginToken = userInfoService.findLoginToken(token);
				// 检查凭证状态（是否有效），表示还在token可刷新时间内
				if(loginToken != null && loginToken.getStatus() == 0){
					// 检查成功了，检查是否是登录请求，如果是的话，跳过验证，直接登录
					// 根据凭证查询用户
					UserInfo userInfo = userInfoService.findUserById(loginToken.getUserId());
					// 在本次请求中持有用户信息
					hostHolder.setUser(userInfo);
					// 凭证过期时间 （是否记住我）
					int expiredSeconds = loginToken.isRememberMe() ?
							RedisTimeConstant.REMEMBER_EXPIRED_SECONDS : RedisTimeConstant.DEFAULT_EXPIRED_SECONDS;
					// 判断是否需要刷新token
					if(!loginToken.getExpired().after(new Date())){
						LoginToken newToken = new LoginToken(
								userInfo.getId(),
								ToolUtil.generateUUID(),  // 随机凭证
								0,  // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
								new Date(System.currentTimeMillis() +
										RedisTimeConstant.DEFAULT_TOKEN_ACCESS_SECONDS * 1000), // 设置凭证到期时间
								loginToken.isRememberMe()
						);
						// 删除原有的redis存储的TOKEN信息
						redisOperatorUtil.del(RedisKeyUtil.getTokenKey(token));
						redisOperatorUtil.set(RedisKeyUtil.getTokenKey(newToken.getToken()),
								newToken, expiredSeconds);

						isFresh = true;
						resToken = newToken.getToken();
					} else {
						//重新刷新token时间
						String redisKey = RedisKeyUtil.getTokenKey(token);
						loginToken.setStatus(0); // 若时间充足，也不用设置刷新，需要在1个小时内刷新一次
						redisOperatorUtil.set(redisKey, loginToken, expiredSeconds);
					}
				} else{ // 如果凭证不合格，超时等情况
					response.setStatus(401);
					hostHolder.clear();
					return false;
				}
			}else{
				//  必须保证传过来不再带有token，但如果是登录的话不返回401，转入Login
				// System.out.println("请求的是" + request.getRequestURI());
				if(request.getRequestURI().equalsIgnoreCase(RequestUrlConstant.LOGIN_URL)){
					// 如果是登录请求，则不用拦截，直接让过
					return true;
				}else{
					response.setStatus(401);
					// 没有token返回401，要求登录
					hostHolder.clear();
					return false;
				}
			}
			return true;
		}

		/**
		 * 在Controller执行之后（即服务端对本次请求做出响应后）被调用
		 * 清理本次请求持有的用户信息
		 */
		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
			/**
			if(isFresh) {
				response.setHeader("Authentication-Token", resToken);
				response.setHeader("Access-Control-Expose-Headers", "Authentication-Token");
				System.out.println(isFresh);
				System.out.println(resToken);
			}*/
			// 去除在threadlocal中保留的内容
			hostHolder.clear();
		}
}
