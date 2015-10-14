package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import courtreferences.latvia.LatviaCourtDocument;
import courtreferences.model.CourtDocument;

public class LatviaHTMLParser extends DocumentParser {

	public LatviaHTMLParser(String countryname, String courtname,
			String processedUser) {
		// TODO Auto-generated constructor stub
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
		LatviaCourtDocument acd = new LatviaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();

		pageContentList.add(htmlFileContent);
		
		acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(LatviaCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String extractedText = document.body().text();
				System.out.println(extractedText);
				
				/*
				 * 
				 acd.setParticipantsName(null);
				
				 * String caseAndDate = "";

				String caseId = null;
				textContent = document.body().toString();
				System.out.println(textContent);
				caseId = acd.extractCNSTCRTCaseId(textContent);
				System.out.println("Latvia case Id "+caseId);
				acd.setCaseId(caseId);
				
				
				//System.out.println("decision date"+ caseAndDate.substring(index+1,endIndex));
				acd.setDecisionDate(null);
				acd.setHeardDate(null);
				*/

	        	
	        	
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
