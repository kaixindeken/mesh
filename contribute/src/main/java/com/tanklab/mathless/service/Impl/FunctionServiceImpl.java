package com.tanklab.mathless.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.constant.FunctionConstant;
import com.tanklab.mathless.mapper.FunctionsMapper;
import com.tanklab.mathless.pojo.FileTreeNode;
import com.tanklab.mathless.pojo.Functions;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.FileSaveBO;
import com.tanklab.mathless.pojo.dto.FunctionInfo;
import com.tanklab.mathless.service.FunctionService;
import com.tanklab.mathless.service.UserInfoService;
import com.tanklab.mathless.utils.FileOperatorUtil;
import com.tanklab.mathless.utils.RedisOperatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Resource
    private RedisOperatorUtil redisOperatorUtils;

//    @Resource
//    private HostHolder hostHolder;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private FunctionsMapper functionsMapper;

    @Resource
    private UserControllerApi userControllerApi;

    private Logger logger = LoggerFactory.getLogger(FunctionServiceImpl.class);

    @Override
    public Message create(String functionName, String relativePath, boolean isFile) throws IOException, InterruptedException {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String filePath = FunctionConstant.function_dir + File.separator + username + File.separator + functionName + File.separator + relativePath;
        return FileOperatorUtil.create(filePath, isFile);
    }

    @Override
    public List<FileTreeNode> getFunDirStr(String functionName){
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String fileAbsolutePath = FunctionConstant.function_dir + File.separator + username + File.separator + functionName;
        return FileOperatorUtil.getFileTree(fileAbsolutePath);
    }

    @Override
    public Message delete(String functionName, String relativePath) {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String filePath = FunctionConstant.function_dir + File.separator + username + File.separator + functionName +File.separator + relativePath;
        return FileOperatorUtil.delete(filePath);
    }

    @Override
    public Message upload(String functionName, String relativePath, MultipartFile file) {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String filePath = FunctionConstant.function_dir + File.separator + username + File.separator + functionName +File.separator + relativePath;
        return FileOperatorUtil.upload(filePath , file);
    }

    @Override
    public void download(String functionName, String relativePath, HttpServletResponse response) {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String fileAbsolutePath =  FunctionConstant.function_dir + File.separator + username + File.separator + functionName + File.separator + relativePath;

        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            logger.error("???????????????");
            return;
        }

        // ??????????????????????????????
        if (file.isDirectory()) {
            FileOperatorUtil.compress(fileAbsolutePath, file.getName() + ".zip");
            file = new File(fileAbsolutePath + ".zip");
        }

        byte[] buffer = new byte[1024];
        int len = 0;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // ??????response?????????????????????????????????
            response.reset();
            response.setHeader("Content-type", "application/octet-stream");
            //response.setContentType("application/x-msdownload");

            // ????????????????????????????????????????????????????????????????????????
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(file.getName(), "GBK"));

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ServletOutputStream sos = response.getOutputStream();
            while ((len = bis.read(buffer)) != -1) sos.write(buffer, 0, len);

            sos.flush();
            sos.close();

        } catch (IOException e) {
            return;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return;
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return;
                }
            }
        }
        logger.info("????????????");
        return;
    }

    @Override
    public Message saveFile(FileSaveBO fileSaveBO) {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String function_dir = FunctionConstant.function_dir;
        // redis ???????????????????????????db
        String fileAbsolutePath = function_dir + File.separator + username +File.separator + fileSaveBO.getFunctionName() + File.separator + fileSaveBO.getRelativePath();
        String key = "user:" + userInfoService.getUserInfo(username).getId() + ":" + fileAbsolutePath;

        redisOperatorUtils.set(key, fileSaveBO.getFileContent());
        FileOperatorUtil.saveAsFileWriter(fileAbsolutePath, fileSaveBO.getFileContent());
        return new Message(true, "????????????");
    }

    @Override
    public Message getFileContent(String functionName, String relativePath) {
//        String username = hostHolder.getUser().getName();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String username = (String) userData.get("name");
        String function_dir = FunctionConstant.function_dir;
        String fileAbsolutePath = function_dir + File.separator + username + File.separator+ functionName + File.separator + relativePath;
        File file = new File(fileAbsolutePath);
        String key = "user:" + userInfoService.getUserInfo(username).getId() + ":" + fileAbsolutePath;

        // ?????????redis?????????????????????key
        if(redisOperatorUtils.keyIsExist(key)){
            String fileContent = (String) redisOperatorUtils.get(key);
            return new Message(true, fileContent);
        } else { // ??????????????????????????????????????????redis??????
            if(!file.exists() || file.isDirectory()){
                return new Message(false, "???????????????????????????????????????");
            }
            Message message = FileOperatorUtil.file2String(file);
            if(message.isSuccess()){
                redisOperatorUtils.set(key, message.getInfo());
            }
            return message;
        }
    }

    @Override
    public Message createFunction(String functionName) throws IOException, InterruptedException {
        return create(functionName,"",false);
    }

    @Override
    public Message deleteFunction(String functionName){
        return delete(functionName,"");
    }

    @Override
    public Message getOwnFunctions(String username) {
        String path = FunctionConstant.function_dir + File.separator + username;
        Message message = FileOperatorUtil.listFiles(path);
        if(!message.isSuccess()) return message;
        else{
            List<String> functionNames = (List<String>) message.getData();
            List<FunctionInfo> functionInfos = new ArrayList<>();
            for(String name : functionNames){
                Example functionsExample = new Example(Functions.class);
                Example.Criteria functionCriteria = functionsExample.createCriteria();
                functionCriteria.andEqualTo("name", name);
                Functions functions = functionsMapper.selectOneByExample(functionsExample);
                FunctionInfo functionInfo = new FunctionInfo();
                BeanUtils.copyProperties(functions, functionInfo);
                functionInfos.add(functionInfo);
            }
            return new Message(true, "????????????", functionInfos);
        }
    }
}
