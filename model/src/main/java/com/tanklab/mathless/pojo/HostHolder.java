package com.tanklab.mathless.pojo;

import org.springframework.stereotype.Component;

/**
 * 持有用户信息（多线程）。用于代替session对象
 */
@Component
public class HostHolder {

		private ThreadLocal<UserInfo> users = new ThreadLocal<>();

		// 存储user信息
		public void setUser(UserInfo userInfo){
				users.set(userInfo);
		}

		// 获取user
		public UserInfo getUser(){
				return users.get();
		}

		// 清理
		public void clear(){
				users.remove();
		}
}
