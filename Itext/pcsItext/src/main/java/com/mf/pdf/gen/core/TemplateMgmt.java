package com.mf.pdf.gen.core;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;
import com.itextpdf.pdfa.PdfAAgnosticPdfDocument;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

public class TemplateMgmt {
	private String templatePath;
    private InputStream is;
    private Map<String, String> fieldMap;
    private static final Logger logger = Logger.getLogger(TemplateMgmt.class.getName());

    public TemplateMgmt(String templatePath, Map<String, String> fieldMap) {
		super();
		this.templatePath = templatePath;
		this.fieldMap = fieldMap;
	}
    public TemplateMgmt(InputStream is, Map<String, String> fieldMap) {
		super();
		this.is = is;
		this.fieldMap = fieldMap;
	}
    public String print() throws FileNotFoundException, IOException{
    	String temp_path = "temp/temp_"+Instant.now().toEpochMilli()+".pdf"; // Need to add logic to prepare dyanmic name
        
    	PdfReader srcPDFReader; 
    	logger.log(Level.INFO, "Template Path: "+templatePath);
    	logger.log(Level.INFO, "Field Map:"+fieldMap);
    	if(templatePath != null) {
			srcPDFReader = new PdfReader(templatePath).setUnethicalReading(true);
		}else {
			srcPDFReader = new PdfReader(is).setUnethicalReading(true);
		}
		PdfWriter pdfWriter = new PdfWriter(temp_path);
		PdfDocument pdfDoc = new PdfDocument(srcPDFReader, pdfWriter);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        //This code is just to print the all the fields and its rotation.
        Set<Entry<String, String>> entrySet = fieldMap.entrySet();
        Iterator<Entry<String, String>> itr = entrySet.iterator();
        while(itr.hasNext())
        {
        	Entry<String, String> entry = itr.next();
        	String value = entry.getValue();
        	if(form.getField(entry.getKey()) != null)
        	{
        		form.getField(entry.getKey()).setValue(value);
            }
        	else
        	{
        		//TODO: Needed to add code based on the expected behavior when a field is missing in the template.
        		logger.log(Level.INFO, "Field "+entry.getKey()+" is not present in the template.");
        	}
        }
        form.flattenFields();
        pdfDoc.close();

		pdfWriter.close();
		pdfWriter.flush();
		srcPDFReader.close();
        logger.log(Level.INFO, "Template is successfully printed at: "+temp_path);
        return temp_path;
    }
   
    
}
