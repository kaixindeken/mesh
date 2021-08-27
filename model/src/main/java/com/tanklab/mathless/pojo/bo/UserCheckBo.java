package com.tanklab.mathless.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCheckBo {

		@NotBlank(message = "用户名不能为空")
		private String nameOrEmail;

		@NotBlank(message = "密码不能为空")
		private String password;

		// 验证码
		private String code;
		private boolean rememberMe;
}
