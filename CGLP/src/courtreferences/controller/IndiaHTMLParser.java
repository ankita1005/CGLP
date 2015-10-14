package courtreferences.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import courtreferences.model.CourtDocument;
import courtreferences.india.*;

public class IndiaHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private IndiaHTMLParser(){}
	
	public IndiaHTMLParser(String countryname,String courtname, String processedUser){
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
		IndiaCourtDocument acd = new IndiaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}
	
	/* Gets the input HTML file name, start and end page of HTML and returns the text content using JSOUP API */
	
	String extractContentFromDocument(IndiaCourtDocument icd, String htmlfile,int startpage,int endpage){
		//dom tree structured in two formats example for 1st format(47-98) USA.1947.002 and for 2nd format(1998.25 - 2013) USA.2000.003
		Document document = null;
		File input = new File(htmlfile);
		String textContent = null;
	    	
		if( htmlfile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String extractedText = document.select("textarea").text();
				
				int petitionerStartIndex = extractedText.indexOf("PETITIONER");
				int versusIndex = extractedText.indexOf("Vs.");
				System.out.println("Petitoner for india ="+extractedText.substring(petitionerStartIndex+11,versusIndex-1).trim());
				int respondentStartIndex = extractedText.indexOf("RESPONDENT");
				int respondentEndIndex = extractedText.indexOf("DATE OF JUDGMENT");
				
				String particiapntsText = extractedText.substring(petitionerStartIndex+11,versusIndex-1).trim() +" Vs. " 
				+ extractedText.substring(respondentStartIndex+11,respondentEndIndex-1).trim();
				System.out.println("Particiapants Text ="+particiapntsText);
				icd.setParticipantsName(particiapntsText);
//				String caseID = document.select("center").first().select("p").first().text();
//				//System.out.println("USA Caseid text"+caseID);
//				icd.setCaseId(caseID);
//				
//				String prepareDateText = document.select("center").first().select("p").text();
//				int indexDecidedDate = prepareDateText.indexOf("Decided"); 
//				if(indexDecidedDate>0){
//					icd.setDecisionDate(prepareDateText.substring(indexDecidedDate+8));
//				}
//				else{
//					icd.setDecisionDate(null);
//				}
//				System.out.println(prepareDateText.substring(indexDecidedDate+8));
//				
//				int indexHeardDate = prepareDateText.indexOf("Argued"); 
//				if(indexHeardDate>0){
//					icd.setHeardDate(prepareDateText.substring(indexHeardDate+7));
//				}
//				else{
//					icd.setHeardDate(null);
//				}
//				System.out.println(prepareDateText.substring(indexDecidedDate+7));
////				usacd.setDecisionDate(caseAndDate.substring(index+1,endIndex));
////
	        	textContent = extractedText;
	        	
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
