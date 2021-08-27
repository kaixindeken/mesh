package com.tanklab.mathless.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.mapper.FunctionsMapper;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.NewFunctionBO;
import com.tanklab.mathless.pojo.bo.UpdateFunctionBO;
import com.tanklab.mathless.pojo.dto.FunctionInfo;
import com.tanklab.mathless.service.FunctionDBService;
import com.tanklab.mathless.utils.graceful.result.GraceJSONResult;
import com.tanklab.mathless.utils.graceful.result.ResponseStatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FunctionDBServiceImpl implements FunctionDBService {

		@Resource private FunctionsMapper functionsMapper;

		@Resource
		private UserControllerApi userControllerApi;

		@Override
		public void createFunctionInDB(NewFunctionBO newFunctionBO, String realName, int gitlab_id) {
				LinkedHashMap<String, Object> userData =
						(LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
				Functions functions = new Functions();
				BeanUtils.copyProperties(newFunctionBO, functions);
				functions.setRealName(realName);
				functions.setCategory((String) userData.get("id"));
				functions.setGitlabProject((long) gitlab_id);
				functions.setCreatedAt(new Date());
				functions.setUpdatedAt(new Date());

				int result = functionsMapper.insert(functions);
				if(result != -1){
						GraceJSONResult.exception(ResponseStatusEnum.FAILED);
				}
		}

		@Override
		public void updateFunctionInDB(UpdateFunctionBO updateFunctionBO) {
				Functions functions = new Functions();
				BeanUtils.copyProperties(updateFunctionBO, functions);
				functions.setUpdatedAt(new Date());
				int result = functionsMapper.updateByPrimaryKeySelective(functions);
				if(result != -1){
						GraceJSONResult.exception(ResponseStatusEnum.FAILED);
				}
		}

		@Override
		public void deleteFunctionInDB(Long id) {
				Functions functions = new Functions();
				functions.setId(id);
				int result = functionsMapper.delete(functions);
				if(result != -1){
						GraceJSONResult.exception(ResponseStatusEnum.FAILED);
				}
		}

		@Override
		public List<FunctionInfo> getAllFunctionsInfo() {
			LinkedHashMap<String, Object> userData =
					(LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
			Example functionExample = new Example(Functions.class);
			Example.Criteria functionCriteria = functionExample.createCriteria();
			functionCriteria.andEqualTo("category", userData.get("id"));
			List<Functions> funcs = functionsMapper.selectByExample(functionExample);
			List<FunctionInfo> functionInfos = new ArrayList<>();
			for (Functions f: funcs) {
				FunctionInfo fitmp = new FunctionInfo();
				BeanUtils.copyProperties(f, fitmp);
				functionInfos.add(fitmp);
			}
			return functionInfos;
		}

		@Override
		public Functions getFunctionInfo(Long id) {
			return functionsMapper.selectByPrimaryKey(id);
		}
}
