package com.tanklab.mathless.pojo.bo;

import lombok.Data;

@Data
public class UserInfoBO {

	private String id;
	private String name;
	private String email;
	private String phoneNumber;
	private String password;
	private Integer permissionLevel;
	private Integer status;
}
