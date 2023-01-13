package com.mf.pdf.gen.core;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
public class MergePDF {
    private String templatePath;
    private String finalPDFPath;
    private String inputDocPath;
    private InputStream customerPDFIS;
    private static final Logger logger = Logger.getLogger(MergePDF.class.getName());
    public MergePDF(String templatePath, String outputPDFPath, String customerPDFPath) {
		super();
		this.templatePath = templatePath;
		this.finalPDFPath = outputPDFPath;
		this.inputDocPath = customerPDFPath;
	}
    public MergePDF(String templatePath, String outputPDFPath, InputStream customerPDFIS) {
		super();
		this.templatePath = templatePath;
		this.finalPDFPath = outputPDFPath;
		this.customerPDFIS = customerPDFIS;
	}
    public void process() throws IOException {
        PdfReader pdfReader = new PdfReader(inputDocPath).setUnethicalReading(true);
        PdfDocument tempDoc = new PdfDocument(pdfReader);
        PdfDocument resultPdfDoc = printTemplate();

        for(int count=1; count<= resultPdfDoc.getNumberOfPages(); count++)
        {
        	Rectangle pageSize = resultPdfDoc.getPage(count).getPageSize();
            PdfPage tempPage = tempDoc.getPage(count);

            PdfFormXObject pageXObject = tempPage.copyAsFormXObject(resultPdfDoc);

            // Create a formXObject of a page content, in which the area to move is cut.
            PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);
            PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);
            canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());

            canvas1.addXObjectAt(pageXObject, 0, 0);


            PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getPage(count).setIgnorePageRotationForContent(true));
            canvas.addXObjectAt(formXObject1, 0, 0);

        }


        //Delete Temp file.
        File templateFile = new File(templatePath);
        templateFile.delete();
        tempDoc.close();
        resultPdfDoc.close();
        pdfReader.close();
    }
    protected PdfDocument printTemplate() throws IOException {
        List<Integer> list = prepareTempPageList();
        PdfReader pdfReader = new PdfReader(templatePath).setUnethicalReading(true);
        PdfDocument srcDoc = new PdfDocument(pdfReader);
        PdfWriter pdfWriter = new PdfWriter(finalPDFPath);
        PdfDocument resultPdfDoc = new PdfDocument(pdfWriter);

        for(int count=0; count< list.size(); count++)
        {
        	Rectangle pageSize = srcDoc.getPage(list.get(count)).getPageSize();
            resultPdfDoc.setDefaultPageSize(new PageSize(pageSize));
            resultPdfDoc.addNewPage();

            PdfFormXObject pageXObject = srcDoc.getPage(list.get(count)).copyAsFormXObject(resultPdfDoc);

            // Create a formXObject of a page content, in which the area to move is cut.
            PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);
            PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);
            canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());

            // This method uses the even-odd rule to determine which regions lie inside the clipping path.
            canvas1.eoClip();
            canvas1.endPath();
            canvas1.addXObjectAt(pageXObject, 0, 0);


            PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getPage(count+1).setRotation(srcDoc.getPage(list.get(count)).getRotation()));
            canvas.addXObjectAt(formXObject1, 0, 0);

        }
//        pdfWriter.flush();
//        pdfWriter.close();

//        resultPdfDoc.close();

//        resultPdfDoc.close();
        pdfReader.close();
        pdfWriter.flush();


        return resultPdfDoc;
    }
    private List<Integer> prepareTempPageList() throws IOException
    {
        PdfReader inputPdfReader = new PdfReader(inputDocPath).setUnethicalReading(true);
        PdfReader outputPdfReader = new PdfReader(templatePath).setUnethicalReading(true);
        PdfDocument inputDoc = new PdfDocument(inputPdfReader);
    	PdfDocument tempDoc = new PdfDocument(outputPdfReader);
    	List<Integer> list = new ArrayList<Integer>();
    	for(int count=1; count <= inputDoc.getNumberOfPages(); count++)
    	{
    		Rectangle inputPageSize = inputDoc.getPage(count).getPageSize();
    		int tempCount = 0;
    		for(tempCount=1; tempCount <= tempDoc.getNumberOfPages(); tempCount++)
    		{
    			PdfPage tempPage = tempDoc.getPage(tempCount);
    			Rectangle tempPageSize = tempPage.getPageSize();
    			if(tempPage.getRotation() == 270)
    			{
    				if(Math.round(inputPageSize.getWidth()) == Math.round(tempPageSize.getHeight()) && Math.round(inputPageSize.getHeight()) == Math.round(tempPageSize.getWidth()))
    					break;
    			}
    			else
    			{
    				if(Math.round(inputPageSize.getWidth()) == Math.round(tempPageSize.getWidth()) && Math.round(inputPageSize.getHeight()) == Math.round(tempPageSize.getHeight()))
    					break;
    			}
    		}
    		list.add(tempCount);
    	}
        tempDoc.close();
        inputDoc.close();
        outputPdfReader.close();
        inputPdfReader.close();
    	logger.log(Level.INFO, "Template Page List used for processing: "+list);
    	return list;
    }

}
