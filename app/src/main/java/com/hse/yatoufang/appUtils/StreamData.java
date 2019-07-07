package com.hse.yatoufang.appUtils;

import java.io.Serializable;

public class StreamData implements Serializable {

	private static final long serialVersionUID = 3L;
	private int requestCode;
	private String content;
	private String addtionalData;

	public StreamData(int requestCode, String content) {
		this.requestCode = requestCode;
		this.content = content;
	}

	public StreamData(int requestCode) {
		this.requestCode = requestCode;
	}

	public StreamData(int requestCode, String content, String addtionalData) {
		this.requestCode = requestCode;
		this.content = content;
		this.addtionalData = addtionalData;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtionalData() {
		return addtionalData;
	}
}
