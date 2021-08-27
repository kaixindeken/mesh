package com.tanklab.mathless.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadConfig {

		@Bean
		public MultipartConfigElement multipartConfigElement(){
				MultipartConfigFactory factory = new MultipartConfigFactory();
				// springboot 上传默认限制为1MB和10MB，此处设置为10MB和30MB。
				//上传的单个文件最大值 KB,MB 此处设置为10MB
				DataSize maxSize = DataSize.ofMegabytes(10);
				DataSize requestMaxSize = DataSize.ofMegabytes(30);
				// 设置一次上传文件的总大小
				factory.setMaxFileSize(maxSize);
				factory.setMaxRequestSize(requestMaxSize);
				return factory.createMultipartConfig();
		}
}
