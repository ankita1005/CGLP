package courtreferences.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

//import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import courtreferences.model.CourtDocument;
import courtreferences.spain.SpanishCourtDocument;

public class SpainHTMLParser extends DocumentParser {
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;

	private SpainHTMLParser() {
	}
	public SpainHTMLParser(String countryname, String courtname,
			String processedUser) {
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
	
	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}
	
	@Override
	public CourtDocument processDocumentForCaseDetails(String caseFile,
			String sourceFileName) {
		return processCaseDetails(caseFile, sourceFileName);
	}

	CourtDocument processCaseDetails(String caseFile, String sourceFileName) {
		int startPage = 1;
		int endPage = 1;
		SpanishCourtDocument scd = new SpanishCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(scd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		scd.extractCitations(this.getCourtName(),pageContentList);
		return scd;
	}

	String extractContentFromDocument(SpanishCourtDocument scd, String pdfFile, int startpage, int endpage) {
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String textToBeExtracted = document.select("title").text();
	        	String [] extractedElements = textToBeExtracted.split("SENTENCIA ");
	        	String extractedCaseId = "";
	        	extractedCaseId = extractedElements[extractedElements.length-1];
	        	String extractedDecisionDate = "";


	        	textContent = document.body().toString();
	            InputSource doc = new InputSource(new InputStreamReader(new FileInputStream(new File(pdfFile))));

	        	XPathExpression expr = null;
	        	XPathFactory xFactory = XPathFactory.newInstance();
	        	javax.xml.xpath.XPath xpath = xFactory.newXPath();
	        	expr = xpath.compile("/html/body/div[@id='wrapper']/section[@id='main']/div[1]/div[3]/fieldset[1]/table/tr[5]//td[2]/text()");
	        	Object result = expr.evaluate(doc, XPathConstants.NODESET);

	            // cast the result to a DOM NodeList
	            NodeList nodes = (NodeList) result;

	        	extractedDecisionDate = nodes.item(0).getNodeValue().replaceAll("\\s+","");
	        	System.out.println("Decision Date: "+extractedDecisionDate);
	        	System.out.println("Case Id: "+extractedCaseId);
	        	scd.setCaseId(extractedCaseId);
	        	scd.setDecisionDate(extractedDecisionDate);
	        	scd.setHeardDate(null);
	        	scd.setParticipantsName(null);
	        	
	        	
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing html : " + e.getMessage());
	       	}
		}
		return textContent;
	
	}
	@Override
	String extractContentFromDocument(String pdfFile, int startpage, int endpage) {
		// TODO Auto-generated method stub
		return null;
	}

}
