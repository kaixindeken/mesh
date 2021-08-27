package com.tanklab.mathless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanklab.mathless.pojo.dto.CompleteInfo;

import java.util.List;

public interface CompleteService {

    List<CompleteInfo> getCompleteInfoList() throws JsonProcessingException;

    Long getCompleteInfoListVer();
}
