package com.pcs.itext.pcsItext.bean;

import org.springframework.stereotype.Component;

@Component
public class FormDataDto {

	private String path;

	
	public FormDataDto() {}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public FormDataDto(String path) {
		super();
		this.path = path;
	}

	@Override
	public String toString() {
		return "FormDataDto [path=" + path + ", getPath()=" + getPath() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
