package com.tanklab.mathless.service;

import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.bo.NewFunctionBO;
import com.tanklab.mathless.pojo.bo.UpdateFunctionBO;
import com.tanklab.mathless.pojo.dto.FunctionInfo;

import java.util.List;

public interface FunctionDBService {

		// 1. 新建函数，数据库操作
		public void createFunctionInDB(NewFunctionBO functionInfoBO, String realName, int gitlab_id);

		// 2. 更新函数，数据库操作
		public void updateFunctionInDB(UpdateFunctionBO functionInfoBO);

		//3. 删除函数，数据库操作
		public void deleteFunctionInDB(Long id);

		//4. 获取所有函数信息
		public List<FunctionInfo> getAllFunctionsInfo();

		//5. 获取函数信息
		public Functions getFunctionInfo(Long id);
}
