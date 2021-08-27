package com.tanklab.mathless.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterBO {

		@NotBlank(message = "用户名不能为空")
		@Pattern(regexp = "[a-zA-Z0-9_]{6,20}$", message = "用户名可由字母，数字和下划线组成，至少6位，不可超过20位")
		private String name;

		@NotBlank(message = "邮箱不能为空")
		@Pattern(regexp = "^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$")
		private String email;

		@Pattern(regexp = "^[0-9]*$", message = "只能由数字组成")
		private String phoneNumber;

		@NotBlank(message = "密码不能为空")
		@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$", message = "密码必须包含字母、数字，至少8位，不少于20位！")
		private String password;

		// 验证码
		private String code;


}
