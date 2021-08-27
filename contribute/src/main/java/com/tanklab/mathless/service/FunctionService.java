package com.tanklab.mathless.service;

import com.tanklab.mathless.pojo.FileTreeNode;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.bo.FileSaveBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FunctionService {

    // 1. 下载文件/文件夹
    public void download(String functionName,String relativePath, HttpServletResponse response);

    // 2. 保存用户文件修改
    public Message saveFile(FileSaveBO fileSaveBO);

    // 3. 从文件中读取内容
    public Message getFileContent(String functionName, String relativePath);

    // 4. 创建文件、文件夹
    public Message create( String functionName, String relativePath, boolean isFile) throws IOException, InterruptedException;

    // 5. 删除文件、文件夹
    public Message delete( String functionName, String relativePath);

    // 6. 文件上传
    public Message upload( String functionName, String relativePath, MultipartFile file);

    // 7. 函数目录结构
    public List<FileTreeNode> getFunDirStr( String functionName);

    // 8. 创建函数
    public Message createFunction( String functionName) throws IOException, InterruptedException;

    // 9. 删除函数
    public Message deleteFunction( String functionName) throws IOException;

    // 10. 获取该用户部署的函数信息
    public Message getOwnFunctions(String username);
}
