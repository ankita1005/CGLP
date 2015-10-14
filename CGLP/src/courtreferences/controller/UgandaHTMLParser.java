package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import courtreferences.uganda.UgandaCourtDocument;
import courtreferences.model.CourtDocument;

public class UgandaHTMLParser extends DocumentParser {

	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	static DocumentParser d;
	
	private UgandaHTMLParser(){}
	
	public UgandaHTMLParser(String countryname,String courtname, String processedUser){
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
		UgandaCourtDocument acd = new UgandaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
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
	
	String extractContentFromDocument(UgandaCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String caseNo = document.select("div.field-item").get(1).text();
				String decisionDate = document.select("div.field-item").get(2).text();
				String participantString = document.select("title").text();
				if(participantString.contains("v"))
				{
					String[] participant1 = participantString.split("v");
					System.out.println(participant1[0]);
					acd.setParticipantsName(participant1[0]);
				}
				else if(participantString.contains("and"))
				{
					String[] participant1 = participantString.split("and");
					System.out.println(participant1[0]);
					acd.setParticipantsName(participant1[0]);
				}
				System.out.println(caseNo);
				System.out.println(decisionDate);
				
				acd.setCaseId(caseNo);
				acd.setDecisionDate(decisionDate);
				textContent = document.body().toString();
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing html : " + e.getMessage());
	       	}
			
		}
	
		return textContent;
	}
	
	
	String extractContentFromDocument
	(String pdfFile,int startpage,int endpage){
		return null;
	}

	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}
	
}
