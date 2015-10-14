package courtreferences.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.w3c.dom.xpath.XPathExpression;
import org.xml.sax.InputSource;

import courtreferences.australia.AustralianCourtDocument;
import courtreferences.austria.AustriaCourtDocument;
import courtreferences.canada.CanadianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;
import courtreferences.util.SpanishtoEngDateHelper;

public class AustriaHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private AustriaHTMLParser(){}
	
	public AustriaHTMLParser(String countryname,String courtname, String processedUser){
		this.setCountryName(countryname);
		this.setCourtName(courtname);
		this.setProcessedUserName(processedUser);
	}
	
	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public int getFileLength() {
		return fileLength;
	}

	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}

	public int getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}
	
	public CourtDocument processDocumentForCaseDetails(String caseFile, String sourceFileName){
		//CitationCases.setRefid(1);
		//skannan-comment: change to accomodate adjective changes 
			return processCaseDetails(caseFile, sourceFileName);

	}
	
	/* Processes HTML Files
	 * Similar methods can be added for other country files. It should be written based on the structure of those documents 
	 * */
	
	CourtDocument processCaseDetails(String caseFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		AustriaCourtDocument acd = new AustriaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		//acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(AustriaCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
		    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String htmlText = document.body().toString();

				//String caseidString = "\\<b\\>rol\\sn.*\\<\\/b\\>"; 
			/**	String caseidString = "\\<b\\>role?s?\\sn.*\\<\\/b\\>"; 
				Pattern caseidPattern = Pattern.compile(caseidString,Pattern.CASE_INSENSITIVE);
				Matcher caseidMatcher = caseidPattern.matcher(htmlText);
				caseidMatcher.find();
				String caseId = caseidMatcher.group();
				System.out.println(" Case Id: "+caseId);**/
				String decisionDate = null;
				String dateString = "\\d{2}\\.\\d{2}\\.\\d{4}";
				Pattern datePattern = Pattern.compile(dateString);
				Matcher dateMatcher = datePattern.matcher(htmlText);
					if(dateMatcher.find())
					{
						decisionDate = dateMatcher.group();
						System.out.println("Date"+dateMatcher.group()+" "+decisionDate);
					}
				
		            InputSource doc = new InputSource(new InputStreamReader(new FileInputStream(new File(pdfFile))));

		            javax.xml.xpath.XPathExpression expr = null;
		        	XPathFactory xFactory = XPathFactory.newInstance();
		        	javax.xml.xpath.XPath xpath = xFactory.newXPath();
		        	expr = xpath.compile("/html/body/div/div[3]/p[2]/text()");
		        	Object result = expr.evaluate(doc, XPathConstants.NODESET);

		            // cast the result to a DOM NodeList
		            NodeList nodes = (NodeList) result;

		        	String caseId = nodes.item(0).getNodeValue().replaceAll("\\s+","");
		   
						
					System.out.println(" Case Id: "+caseId);				
				//acd.setCaseId();
				
				//acd.setDecisionDate();
				//acd.setHeardDate(null);

	        	textContent = document.body().toString();
	        	
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing html : " + e.getMessage());
	       	}
		}
		return textContent;
	}
	
	String extractContentFromDocument(String pdfFile,int startpage,int endpage){
		return null;
	}

	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}

}
