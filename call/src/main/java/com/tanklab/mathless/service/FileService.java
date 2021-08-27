package com.tanklab.mathless.service;

import com.tanklab.mathless.pojo.FileTreeNode;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.bo.FileBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileService {

		// 下载文件/文件夹
		public void download(String relativePath, HttpServletResponse response);

		// 保存用户文件修改
		public Message saveFile(FileBO fileBO);

		// 从文件中读取内容
		public Message getFileContent(String relativePath);

		// ----
		// 1. 创建文件、文件夹
		public Message create(String relativePath, boolean isFile) throws IOException, InterruptedException;

		// 2. 删除文件、文件夹
		public Message delete(String relativePath);

		// 3. 文件上传
		public Message upload(MultipartFile file);

		// 4. 输入文件结构树
		public List<FileTreeNode> getFileTree(String relativePath);

		// ----
		// 新建函数
		public String NewFunction(String realName, String lang) throws IOException;



}
