package com.pcs.itext.pcsItext.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.mf.pdf.gen.core.MergePDF;
import com.pcs.itext.pcsItext.bean.FormDataDto;
import com.pcs.itext.pcsItext.bean.OverlayDocumentResponse;
import com.pcs.itext.pcsItext.service.PcsService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/")
public class pdfgenController {

    private Logger logger = LoggerFactory.getLogger(pdfgenController.class);

    @Autowired
    PcsService pcsService;

    @Value("${spring.local.path}")
    private String NODE_DIR;

    @Value("${python.local.url}")
    private String URL;

    @Value("${pdf.formfield.temp1}")
    private String formFieldJsonTemp1;




    @PostMapping("/getFormFieldsInPdfBox")
    public  List<String>  callDataFormFiledPDFBox(@RequestBody FormDataDto formDataDto) throws IOException {

        String templateInputPath = NODE_DIR + "/templates/";

//        PdfDocument tempDoc = new PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(templateInputPath + formDataDto.getPath()).setUnethicalReading(true));

        PDDocument pDDocument = PDDocument.load(new File(templateInputPath + formDataDto.getPath()));
        PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
        List<PDField> fields = pDAcroForm.getFields();

        List<String> fieldList = new ArrayList<>();
        for (PDField field : fields) {
            fieldList.add(field.getFullyQualifiedName());
        }
return  fieldList;
        }



    @PostMapping("/overlayDocument")
    public OverlayDocumentResponse callDataOverlayDoc(@RequestBody JsonNode body) throws FileNotFoundException, IOException, InterruptedException {


        String appBasePath = NODE_DIR;                        // set this from properties file
        String inputFilePath = NODE_DIR + "/docs/";
        String outputPDFPath = NODE_DIR + "/output/";
        String templateInputPath = NODE_DIR + "/templates/";


        logger.info("this is the json body = " + body.toPrettyString());


        logger.info("this is the appBasePath "+ appBasePath);
        JsonNode inputformFields = body.get("formFields");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> formFieldsInputMap = mapper.convertValue(inputformFields, new TypeReference<Map<String, Object>>(){});
        logger.info("form " + formFieldsInputMap);




        String temp_path = "/home/bharat/Music/i2i/new/pcsItext 060123 Evening/pcsItext/temp/temp_bharat_"+ Instant.now().toEpochMilli()+".pdf";

        String outputPDF = outputPDFPath.concat(body.get("name").asText())+".pdf";
        logger.info("Output PDF = " + outputPDF);


        //		inputPDFFile
        String inputPDF = inputFilePath.concat(body.get("basePath").asText());
        logger.info("Input PDF = " + inputPDF);


        //		templatePath
        ArrayNode templatePathNode = (ArrayNode) body.get("templatePath");
        String templatePath = templateInputPath.concat(templatePathNode.get(0).asText());
        logger.info("Template PDF = " + templatePath);










        PDDocument pDDocument = PDDocument.load(new File(templatePath));
        PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
        List<PDField> fields = pDAcroForm.getFields();

        for (PDField field : fields) {

//            field.setValue(field.getFullyQualifiedName());
            System.out.println( "formFieldsInputMap   KEY -> "+field.getFullyQualifiedName()+" VALUE ->  "+formFieldsInputMap.get(field.getFullyQualifiedName()));

            field.setValue((String) formFieldsInputMap.get(field.getFullyQualifiedName()));
            field.setReadOnly(true);

        }

        pDDocument.save(temp_path);
        pDDocument.close();
//        Thread.sleep(TimeUnit.SECONDS.toMillis(10));


//        MergePDF  mergePDF = new MergePDF(temp_path, outputPDF, inputPDF);
//
//
//        mergePDF.process();



        // template Landscape pdf`s
        PdfDocument tempDoc = new PdfDocument(new PdfReader(templatePath).setUnethicalReading(true));
        int tempCount = 0;
        List raoteaPageList =	new ArrayList<>();
        for(tempCount=1; tempCount <= tempDoc.getNumberOfPages(); tempCount++)
        {
            PdfPage tempPage = tempDoc.getPage(tempCount);
            if(tempPage.getRotation() == 270)
            {
                raoteaPageList.add(tempCount);
            }
        }
        logger.info("Rotated Page LIST "+raoteaPageList.toString());


/// MARGE


        PdfReader pdfReaderiInputpdf = new PdfReader(inputPDF);
        pdfReaderiInputpdf.setUnethicalReading(true);
        PdfDocument inputdoc = new PdfDocument(pdfReaderiInputpdf);

//        PdfReader pdfReaderOutpdf = new PdfReader(new File(outputPDF));
//        pdfReaderOutpdf.setUnethicalReading(true);
//        PdfDocument resultPdfDoc = new PdfDocument(pdfReaderOutpdf);


        PdfWriter pdfWriter = new PdfWriter(outputPDF);

        PdfDocument resultPdfDoc = new PdfDocument(pdfWriter);

        for(int count=1; count<= inputdoc.getNumberOfPages(); count++)
        {
            Rectangle pageSize = inputdoc.getPage(count).getPageSize();
            resultPdfDoc.setDefaultPageSize(new PageSize(pageSize));
            resultPdfDoc.addNewPage();

            PdfFormXObject pageXObject = inputdoc.getPage(count).copyAsFormXObject(resultPdfDoc);

            // Create a formXObject of a page content, in which the area to move is cut.
            PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);

            PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);

            canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());

            // This method uses the even-odd rule to determine which regions lie inside the clipping path.
            canvas1.eoClip();
            canvas1.endPath();
            canvas1.addXObjectAt(pageXObject, 0, 0);


            PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getPage(count).setRotation(inputdoc.getPage(count).getRotation()));
            canvas.addXObjectAt(formXObject1, 0, 0);

        }
        pdfReaderiInputpdf.close();
//        pdfReaderOutpdf.close();
//        pdfWriter.flush();
//        pdfReaderOutpdf.close();



//        }





        return  null;




    }





}
