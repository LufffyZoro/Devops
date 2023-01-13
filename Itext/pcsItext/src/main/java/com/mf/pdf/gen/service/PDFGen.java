package com.mf.pdf.gen.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import com.mf.pdf.gen.core.MergePDF;
import com.mf.pdf.gen.core.TemplateMgmt;

public class PDFGen {
	
	private String templatePDFPath;
	private String customerPDFPath;
	private String pdfPrintDestination;
	private InputStream templatePDFIS;
	private InputStream customerPDFIS;
	private Map<String, String> fieldMap;
	private static final Logger logger = Logger.getLogger(PDFGen.class.getName());

	public PDFGen(String templatePDFPath, String customerPDFPath, String pdfPrintDestination,
			Map<String, String> fieldMap) {
		super();
		this.templatePDFPath = templatePDFPath;
		this.customerPDFPath = customerPDFPath;
		this.pdfPrintDestination = pdfPrintDestination;
		this.fieldMap = fieldMap;
	}
	public PDFGen(String pdfPrintDestination, InputStream templatePDFIS, InputStream customerPDFIS,
			Map<String, String> fieldMap) {
		super();
		this.pdfPrintDestination = pdfPrintDestination;
		this.templatePDFIS = templatePDFIS;
		this.customerPDFIS = customerPDFIS;
		this.fieldMap = fieldMap;
	}
	public void generatePDF() throws FileNotFoundException, IOException{
		// Invoke method of Template Management Class to generate the template.
		TemplateMgmt tm;
		if(templatePDFPath != null && customerPDFPath != null){
			tm = new TemplateMgmt(templatePDFPath, fieldMap);}
		else {
			tm = new TemplateMgmt(templatePDFIS, fieldMap);
		}
		String templatePath = tm.print();
		//Merge template PDF with customer PDF
		MergePDF mergePDF;
		if(customerPDFPath != null) {
			mergePDF = new MergePDF(templatePath, pdfPrintDestination, customerPDFPath);
		}else {
			mergePDF = new MergePDF(templatePath, pdfPrintDestination, customerPDFIS);
		}
		mergePDF.process();
	}
}
