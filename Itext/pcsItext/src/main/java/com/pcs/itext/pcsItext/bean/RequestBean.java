package com.pcs.itext.pcsItext.bean;

public class RequestBean {

	private FormFieldResponse formFields;
	
	private String name;

	private String templatePath;
	
	private String basePath;

	public FormFieldResponse getFormFields() {
		return formFields;
	}

	public void setFormFields(FormFieldResponse formFields) {
		this.formFields = formFields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public RequestBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	
	
	
}
