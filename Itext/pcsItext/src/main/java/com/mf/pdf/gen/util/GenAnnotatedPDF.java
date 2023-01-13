package com.mf.pdf.gen.util;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.File;
import java.io.IOException;

/**
 * Simple widget annotation example.
 */
public class GenAnnotatedPDF {

    public static final String DEST = "temp/template.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new GenAnnotatedPDF().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PageSize ps = PageSize.A4;
        pdf.setDefaultPageSize(ps);

        // Initialize document
        Document document = new Document(pdf);

        GenAnnotatedPDF.addAcroForm(document, ps);

        //Close document
        document.close();

    }

	/*
	 * Top corner: 842.0 Bottom Corner : 0.0 Height: 842.0 Width: 595.0 X, Y:
	 * 0.0,0.0
	 */
    public static PdfAcroForm addAcroForm(Document doc, PageSize ps) {

        Paragraph header1 = new Paragraph("Print Copy No.").setFontSize(8);
        Paragraph header2 = new Paragraph("Issued to").setFontSize(8);
        Paragraph header3 = new Paragraph("Batch Number").setFontSize(8);
    	System.out.println("Top corner: "+ps.getTop()+" \n Bottom Corner : "+ps.getBottom()+" \nHeight: "+ps.getHeight()+" \n Width: "+ps.getWidth());
    	System.out.println("X, Y: "+ps.getX()+","+ps.getY());
        doc.showTextAligned(header1, 80, 820, TextAlignment.LEFT);
        doc.showTextAligned(header2, 350, 820, TextAlignment.LEFT);
        //doc.showTextAligned("test", 400, 400, TextAlignment.LEFT, 90);
        //Add acroform
        PdfAcroForm form = PdfAcroForm.getAcroForm(doc.getPdfDocument(), true);
        PdfTextFormField printCopyNo = PdfTextFormField.createText(doc.getPdfDocument(), new Rectangle(140, 818, 100, 15), "print_copy_no");
        PdfFormField issuedTo = PdfTextFormField.createText(doc.getPdfDocument(), new Rectangle(390, 600, 10, 100), "issue_to").setRotation(-90);
        //issuedTo.setRotation(-90);
        form.addField(printCopyNo);
        form.addField(issuedTo);

        

        return form;

    }
}
