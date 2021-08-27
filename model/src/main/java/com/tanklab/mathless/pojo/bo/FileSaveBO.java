package com.tanklab.mathless.pojo.bo;

import lombok.Data;

@Data
public class FileSaveBO {

	private String functionName;

	private String relativePath;

	private String fileContent;
}
