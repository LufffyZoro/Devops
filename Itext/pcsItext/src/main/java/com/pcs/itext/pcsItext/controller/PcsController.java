package com.pcs.itext.pcsItext.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.itextpdf.text.Header;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.mf.pdf.gen.service.PDFGen;
import com.pcs.itext.pcsItext.bean.FormDataDto;
import com.pcs.itext.pcsItext.bean.OverlayDocumentResponse;
import com.pcs.itext.pcsItext.bean.PageCountResponse;
import com.pcs.itext.pcsItext.service.PcsService;


@RestController
@RequestMapping("/pyApi")
public class PcsController<T> {

	private Logger logger = LoggerFactory.getLogger(PcsController.class);

	@Autowired
	PcsService pcsService;

	@Value("${spring.local.path}")
	private String NODE_DIR;

	@Value("${python.local.url}")
	private String URL;
	
	@Value("${pdf.formfield.temp1}")
	private String formFieldJsonTemp1;

	@PostMapping("/getPDFPageCount")
	public PageCountResponse pageCountResponse(@RequestBody FormDataDto formDataDto) {

		try {


			PdfReader pdfReader = new PdfReader(NODE_DIR +"/docs/" + formDataDto.getPath());


			Integer pages = pdfReader.getNumberOfPages();

			logger.info("this is the pdfPages logs" + pdfReader);
			
			return new PageCountResponse(pages);

		}catch(Exception e) {

			e.printStackTrace();

		}
		return null;


	}






	@PostMapping("/getFormFields")
	public String callDataFormFiled(@RequestBody FormDataDto formDataDto){

		logger.info("this is formDataDto = " + formDataDto.toString());
		
		String url = URL;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		String json =null;

		try {

			json = new ObjectMapper().writeValueAsString(formDataDto);

		} catch (JsonProcessingException e) {

			
			e.printStackTrace();

		}
		HttpEntity entity = new HttpEntity(json,httpHeaders);
		logger.info("this is json "+ json);
		logger.info("this is the entity" + entity);

		RestTemplate restTemplate = new RestTemplate();
		
		logger.info("printing getFormFields URL = "+ url);
		
		String res = restTemplate.exchange(url, HttpMethod.POST,entity,String.class).getBody();
		ResponseEntity<String> res1 = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
		logger.info("this is res " + res.toString());

		logger.info("this is res1 = "+res1.toString());
		
		return res.toString();
		

	}







	@PostMapping("/overlayDocument")
	public OverlayDocumentResponse callDataOverlayDoc(@RequestBody JsonNode body) throws FileNotFoundException, IOException {


		String appBasePath = NODE_DIR;						// set this from properties file 
		String inputFilePath = NODE_DIR + "/docs/";
		String outputPDFPath = NODE_DIR + "/output/";
		String templateInputPath = NODE_DIR + "/templates/";

		
		logger.info("this is the json body = " + body.toPrettyString());
		logger.info("this is the json body = " + body.toPrettyString());

		try {

			//		String appBasePath = "temp/";
			logger.info("this is the appBasePath "+ appBasePath);

			JsonNode inputformFields = body.get("formFields");
			logger.info("112 :: "+inputformFields);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> formFieldsInputMap = mapper.convertValue(inputformFields, new TypeReference<Map<String, Object>>(){});
			logger.info("form " + formFieldsInputMap);

			Map<String,String> newMap1= new HashMap<>();

			List<String> list = Arrays.asList("site", "batch_no", "disclaimer", "revision_lbl","issued_copy_lbl","controlled_copy");

			for(String key :formFieldsInputMap.keySet()){
			if(!list.contains(key)){
				newMap1.put(key, String.valueOf(formFieldsInputMap.get(key)));
			}
			}


			String outputPDF = outputPDFPath.concat(body.get("name").asText())+".pdf";
			logger.info("Output PDF = " + outputPDF);


			//		inputPDFFile 
			String inputPDF = inputFilePath.concat(body.get("basePath").asText());
			logger.info("Input PDF = " + inputPDF);


			//		templatePath
			ArrayNode templatePathNode = (ArrayNode) body.get("templatePath");		
			String templatePath = templateInputPath.concat(templatePathNode.get(0).asText());
			logger.info("Template PDF = " + templatePath);

			Map<String, String> inputMap1 = new HashMap<String, String>();

			PDFGen pdfGen = new PDFGen(templatePath, inputPDF, outputPDF, newMap1);
			logger.info("Document is overlayed in new PDF " + pdfGen.toString());
			pdfGen.generatePDF();
			OverlayDocumentResponse overlayDocumentResponse = new OverlayDocumentResponse();
			overlayDocumentResponse.setSuccess("true");
			return overlayDocumentResponse;

		}
		catch(Exception e) {
			e.printStackTrace();
			logger.info("Error at OverLayDocument" + e.getMessage());
			OverlayDocumentResponse overlayDocumentResponse = new OverlayDocumentResponse();
			overlayDocumentResponse.setSuccess("false");

			return overlayDocumentResponse;

		}

	}
	
	
	@GetMapping("/fileDownload")
	public ResponseEntity<InputStreamResource> fileDownload(@RequestParam String path){
//		public ResponseEntity<InputStreamResource> fileDownload(@RequestBody FormDataDto formDataDto) throws FileNotFoundException{	
//		File file = new File(NODE_DIR + "\\output\\"+"op1.pdf");
	
		File file = new File(path);
		logger.info("This is the File Path "+path);
		
//		FileInputStream inputStream = new FileInputStream(file);
//		InputStreamResource inputStreamResource =  new InputStreamResource(inputStream);
		try {
		
		FileInputStream fileInputStream = new FileInputStream(file);



		HttpHeaders  httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition","inline; filename="+file.getName());
		ResponseEntity.BodyBuilder ok = ResponseEntity.ok();
		ok.headers(httpHeaders);
		ok.contentType(MediaType.APPLICATION_PDF);
		ResponseEntity<InputStreamResource> body;
		body = ok.body(new InputStreamResource(fileInputStream ));
		
		logger.info("PDF Download Successfully");
		
		return body;
		
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Download Failed");
		}
		return null;

	}








	@PostMapping("/getFormFieldsInPdf")
	public String callDataFormFiledPDF(@RequestBody FormDataDto formDataDto) throws IOException {

		String templateInputPath = NODE_DIR + "/templates/";

		PdfDocument tempDoc = new PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(templateInputPath+formDataDto.getPath()).setUnethicalReading(true));


		PdfAcroForm form = PdfAcroForm.getAcroForm(tempDoc, true);
		Map<String, PdfFormField> formFields = form.getFormFields();
		Set<String> keySet = formFields.keySet();

		for (String s : keySet) {
				PdfFormField pdfFormField = formFields.get(s);
		}


		return  keySet.toString();

	}















	}
