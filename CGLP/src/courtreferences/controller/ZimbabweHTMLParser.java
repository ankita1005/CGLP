package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import courtreferences.model.CourtDocument;
import courtreferences.peru.PeruCourtDocument;
import courtreferences.zimbabwe.ZimbabweCourtDocument;

public class ZimbabweHTMLParser extends DocumentParser {

	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	static DocumentParser d;
	
	private ZimbabweHTMLParser(){}
	
	public ZimbabweHTMLParser(String countryname,String courtname, String processedUser){
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
		ZimbabweCourtDocument acd = new ZimbabweCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
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
	
	String extractContentFromDocument(ZimbabweCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;		
		
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
									
				String number = document.select("div.field-item.odd").get(0).text();
				String caseNo = document.select("div.field-item.odd").get(2).text();
				String decisionDate = document.select("div.field-item.odd").get(3).text();
				String participant = document.select("h1.title").text();

			
				textContent = document.body().toString();
				
				caseNo= caseNo.replaceAll("\\(","");
				caseNo = caseNo.replaceAll("\\)","");
				System.out.println(caseNo);
				System.out.println(decisionDate);
				System.out.println(number);
				
				String[] parts = participant.split(" ");
				String lastWord = parts[parts.length - 1];
				acd.setCaseId(caseNo);
				acd.setDecisionDate(decisionDate);
				
				if(lastWord.equalsIgnoreCase(number) || lastWord.equalsIgnoreCase("(pvt)"))
				{
					participant = participant.replace(lastWord, "");
					System.out.println(participant);
					
				}
				
				acd.setParticipantsName(participant);
				System.out.println("*************************");

				
				
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
