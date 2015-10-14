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
import org.jsoup.nodes.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import courtreferences.lesotho.LesothoCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.spain.SpanishCourtDocument;

public class LesothoHTMLParser extends DocumentParser {
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;

	private LesothoHTMLParser() {
	}
	public LesothoHTMLParser(String countryname, String courtname,
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
		LesothoCourtDocument scd = new LesothoCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(scd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		scd.extractCitations(this.getCourtName(),pageContentList);
		return scd;
	}

	String extractContentFromDocument(LesothoCourtDocument scd, String pdfFile, int startpage, int endpage) {
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
		String extractedCaseId = "";
		ArrayList<String> participants = new ArrayList<String>();
		String extractedDecisionDate = "";
		String participantsString = "";
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String textToBeExtracted = document.select("h1").text();
	        	String [] extractedElements = textToBeExtracted.toUpperCase().split("V");
	        	if(extractedElements!=null)
	        	{
	        		participants.add(extractedElements[0]);
	        	
	        	String [] extractedEle = null;
	        	extractedEle = (extractedElements[extractedElements.length-1]).split("\\(");
	        	participants.add(extractedEle[0]);
	        	
	        	for (String s : participants)
	        	{
	        	    participantsString += s + "\t";
	        	}

	        	System.out.println(participantsString);
	        	extractedCaseId = extractedEle[1];
	        	extractedCaseId = extractedCaseId.replaceAll("\\)", "");
	        	textContent = document.body().toString();
	        	String temp = "Judgment Date:";
	        	/**int startIndex = textContent.indexOf(temp)+temp.length();
	        	String subContent = textContent.substring(startIndex);
	        	int endIndex = subContent.indexOf("\n"); **/
	        	Element decDate = document.select("span.date-display-single").first();
	        	extractedDecisionDate = decDate.text();

	        	System.out.println("Decision Date: "+extractedDecisionDate);
	        	System.out.println("Case Id: "+extractedCaseId);
	        	}
	        	scd.setCaseId(extractedCaseId);
	        	scd.setDecisionDate(extractedDecisionDate);
	        	scd.setHeardDate(null);
	        	scd.setParticipantsName(participantsString);
	        	
	        	
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
