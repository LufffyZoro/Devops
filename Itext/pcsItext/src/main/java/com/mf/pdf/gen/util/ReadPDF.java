package com.mf.pdf.gen.util;

import java.io.IOException;
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

public class ReadPDF {

	public static void main(String[] args) throws IOException {
		String src = "temp/sample_template.pdf";
		String dest = "temp/op_template.pdf";
		PdfReader pdfReader = new PdfReader(src);
		PdfDocument document = new PdfDocument(pdfReader, new PdfWriter(dest));
		PdfAcroForm form = PdfAcroForm.getAcroForm(document, false);
		form.getField("#batchNumber").setValue("12").setRotation(-90);
		form.getField("#printReason").setValue("test500").setRotation(-90);
		form.getField("#printCopyNo").setValue("IP-MD-SOP-0003-03-0016");
		form.getField("#recipient").setValue("DINESH NERKAR");
		form.getField("#printRequestDateTime").setValue("2022-12-13T04:46:07.790Z");
		form.getField("^userIp1").setValue("12").setRotation(-90);
		form.getField("@revision").setValue("03").setRotation(-90);
		form.getField("#userId").setValue("BHUSHAN DEORE");
		form.getField("@infocardNumber").setValue("Test").setRotation(-90);//IP-MD-SOP-0003
		/*
		 * PdfFormField test = form.getField("@infocardNumber");//IP-MD-SOP-0003
		 * PdfNumber demo =
		 * (PdfNumber)test.getWidgets().get(0).getAppearanceCharacteristics().get(
		 * PdfName.R); System.out.println(demo.getValue());
		 */
		form.flattenFields();
		document.close();
		/*
		 * Map<String, PdfFormField> fieldMap = form.getFormFields();
		 * Set<Entry<String,PdfFormField>> fields = fieldMap.entrySet();
		 * Iterator<Entry<String, PdfFormField>> itr = fields.iterator();
		 * while(itr.hasNext()) { Entry<String, PdfFormField> entry = itr.next();
		 * System.out.println("Key : "+entry.getKey());
		 * System.out.println("Value: "+entry.getValue().getValueAsString()); }
		 */
		

	}

}
