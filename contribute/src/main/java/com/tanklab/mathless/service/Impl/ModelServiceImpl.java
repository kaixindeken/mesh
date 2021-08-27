package com.tanklab.mathless.service.Impl;

import com.tanklab.mathless.mapper.ModelsMapper;
import com.tanklab.mathless.pojo.Models;
import com.tanklab.mathless.service.ModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Resource public ModelsMapper modelsMapper;

    /** 得到所有函数模块的id和 name */
    @Override
    public List<Models> getAllModelsInfo() {
        return modelsMapper.selectAll();
    }
}
