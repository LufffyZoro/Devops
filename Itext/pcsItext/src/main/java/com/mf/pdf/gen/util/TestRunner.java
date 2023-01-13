package com.mf.pdf.gen.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mf.pdf.gen.service.PDFGen;

public class TestRunner {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String customerPDFPath = "temp/input2.pdf";
		String templatePath = "temp/op_sample_template2.pdf";
		String outputPath = "temp/op_template.pdf";
		Map<String, String> inputMap = new HashMap<String, String>();
		
		  inputMap.put("#printCopyNo","IP-MD-SOP-0003-03-0016");
		  inputMap.put("#recipient","DINESH NERKAR");
		  inputMap.put("#printRequestDateTime","2022-12-13T04:46:07.790Z");
		  inputMap.put("#userId","BHUSHAN DEORE");
		  
		  inputMap.put("#batchNumber","12"); 
		  inputMap.put("#printReason","test500");
		  inputMap.put("^userIp1","12"); 
		  inputMap.put("^userIp2","IP2"); 
		  inputMap.put("@revision","03");
		  inputMap.put("@infocardNumber","Ic");
		 
		
		
		/*
		 * inputMap.put("top_#printCopyNo","IP-MD-SOP-0003-03-0016");
		 * inputMap.put("top_#recipient","DINESH NERKAR");
		 * inputMap.put("bot_#printRequestDateTime","2022-12-13T04:46:07.790Z");
		 * inputMap.put("bot_#userId","BHUSHAN DEORE");
		 * 
		 * inputMap.put("side_#batchNumber","12");
		 * inputMap.put("side_#printReason","test500");
		 * inputMap.put("side_^userIp1","12"); inputMap.put("side_@revision","03");
		 */
		 
		
		PDFGen pdfGen = new PDFGen(templatePath, customerPDFPath, outputPath, inputMap);
		pdfGen.generatePDF();
	}

}
