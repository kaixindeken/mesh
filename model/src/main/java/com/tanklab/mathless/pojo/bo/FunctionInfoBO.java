package com.tanklab.mathless.pojo.bo;

import lombok.Data;

@Data
public class FunctionInfoBO {

		private Long id;
		private String name;
		private String realName;
		private String description;
		private String lang;
		private String params;
		private Long category;
		private Long memory;
		private Long timeout;
}
