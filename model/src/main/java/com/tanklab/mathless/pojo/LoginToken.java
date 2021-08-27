package com.tanklab.mathless.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginToken {

		private String userId;
		private String token; // 凭证
		private int status; // 状态（是否有效）
		private Date expired; // 过期时间
		private boolean rememberMe;

		public LoginToken(){

		}

		public LoginToken(String userId, String token, int status, Date expired, boolean rememberMe) {
				this.userId = userId;
				this.token = token;
				this.status = status;
				this.expired = expired;
				this.rememberMe = rememberMe;
		}
}
