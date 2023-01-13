package com.mf.pdf.gen.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;

public class TemplateUtils {
	String templateName = "CPS_Print_Overlay_3_Header 1_Updated"; // Please provide without extension i.e. .pdf
	String src = "temp/org_temp/"+templateName+".pdf";
	String dest = "temp/updated_temp/"+templateName+"_Updated.pdf";

	//String[] formParameters = {"^userIp1","@infocardNumber","@revision","^userIp2"}; // CPS_Print_Overlay_2 1
//	String[] formParameters = {"@revision","#printReason","@infocardNumber","#batchNumber","^userIp1"}; // IPS_Print_Overlay_1_batchID and userIP 1
//	String[] formParameters = {"@revision","#printReason","@infocardNumber","#batchNumber","^userIp1"}; // IPS_Print_Overlay_1_batchID and userIP 1
	String[] formParameters = {"#printReason"}; // IPS_Print_Overlay_1_batchID and userIP 1
	public static void main(String[] args) throws IOException {
		TemplateUtils util = new TemplateUtils();
		util.getFieldAngles(); // If you want to print the angle. Uncomment this line but comment below one.
//		util.updateTemplate();
		util.printSidebarFields();
	}
	private void printSidebarFields() throws IOException
	{
		PdfReader pdfReader = new PdfReader(src);
		PdfDocument document = new PdfDocument(pdfReader, new PdfWriter(dest));
		PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
		Iterator<Entry<String, PdfFormField>> itr = form.getFormFields().entrySet().iterator();
		while(itr.hasNext())
		{
			Entry<String, PdfFormField> entry = itr.next();
			entry.getValue().setValue(entry.getKey());
		}
		document.close();
	}
	private Map<Integer, Map<String, Integer>> getFieldAngles() throws IOException
	{
		PdfReader pdfReader = new PdfReader(src);
		PdfDocument document = new PdfDocument(pdfReader);
		PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
		Map<Integer, Map<String, Integer>> resultMap = new HashMap<Integer, Map<String,Integer>>();
		Map<String, PdfFormField> fieldMap = form.getFormFields();
		for(String param: formParameters)
		{
			if(fieldMap.get(param) == null)
			{
				System.out.println("This field is not present in the template : "+param);
				continue;
			}
			String[] paramList = {param+".1", param+".2",param+".3",param+".4"};
			for(String p: paramList)
			{
				if(fieldMap.get(p) == null) // This code is four pages in template. 
					continue;
				PdfWidgetAnnotation ann = fieldMap.get(p).getWidgets().get(0);
				PdfPage page = ann.getPage();
				Integer pageNumber = document.getPageNumber(page);
				PdfDictionary rotation =  ann.getAppearanceCharacteristics();
	    		PdfNumber ro = (PdfNumber)rotation.get(PdfName.R);
	    		System.out.println("Page: "+pageNumber+" Key:"+p+" Angle: "+ro);
	    		Integer angle = 0;
	    		if(ro != null)
	    			angle = ro.intValue();
	    		if(resultMap.get(pageNumber) == null)
	    			resultMap.put(pageNumber, new HashMap<String, Integer>());
	    		resultMap.get(pageNumber).put(p, angle);
			}
		}
		document.close();
		return resultMap;
	}
	private void updateTemplate() throws IOException
	{
		PdfReader pdfReader = new PdfReader(src);
		PdfDocument document = new PdfDocument(pdfReader, new PdfWriter(dest));
		PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
		Map<String, PdfFormField> fieldMap = form.getFormFields();
		// Angles for sidebar fields (A4 portrate, A4 Landscape, Letter portrate, Letter Landscape): 0, 180, 0, 180
		
		
		Map<Integer, Map<String, Integer>> resultMap = getFieldAngles();
		Iterator<Entry<Integer, Map<String, Integer>>> itr = resultMap.entrySet().iterator();
		while(itr.hasNext())
		{
			Entry<Integer, Map<String, Integer>> entry = itr.next();
			Integer key = entry.getKey(); // page number
			
			Map<String, Integer> value = entry.getValue();
			Iterator<Entry<String, Integer>> valueItr = value.entrySet().iterator();
			while(valueItr.hasNext())
			{
				Entry<String, Integer> valueEntry =valueItr.next();
				String fieldName = valueEntry.getKey();
				Integer fieldAngle = valueEntry.getValue();
				if(key == 1 || key == 3) // angle : 0
				{
					fieldAngle = -fieldAngle;
				}
				else // 2 & 4, angle: 180
				{
					if(fieldAngle < 0)
						fieldAngle = 360 + fieldAngle; // This is to get positive value of negative angle.
					fieldAngle = 180 - fieldAngle;
				}
				fieldMap.get(fieldName).setRotation(fieldAngle);
			}
			
		}
		/*
		 * Map<String, int[]> formParameters = new HashMap<String, int[]>();
		 * formParameters.put("^userIp1", new int[] {-90, 180, -90, 180});
		 * formParameters.put("@infocardNumber", new int[] {-90, 180, -90, 180});
		 * formParameters.put("@revision", new int[] {-180, 180, -90, -90});
		 * formParameters.put("^userIp2", new int[] {-90, 180, -90, 180});
		 * 
		 * Map<String, PdfFormField> fieldMap = form.getFormFields();
		 * 
		 * Iterator<Entry<String, int[]>> itr = formParameters.entrySet().iterator();
		 * while(itr.hasNext()) { Entry<String, int[]> entry = itr.next(); String param
		 * = entry.getKey(); int[] value = entry.getValue();
		 * fieldMap.get(param+".1").setRotation(value[0]);
		 * fieldMap.get(param+".2").setRotation(value[1]);
		 * fieldMap.get(param+".3").setRotation(value[2]);
		 * fieldMap.get(param+".4").setRotation(value[3]);
		 * 
		 * }
		 */
		document.close();
	}

}
