package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import courtreferences.australia.AustralianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.papuaguinea.PapuaGuineaCourtDocument;

public class PapuaHTMLParser extends DocumentParser {
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;

	public PapuaHTMLParser(String countryname, String courtname,
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
	

	@Override
	public CourtDocument processDocumentForCaseDetails(String caseFile,
			String sourceFileName) {
		return processCaseDetails(caseFile, sourceFileName);
	}


	/* Processes HTML Files
	 * */
	
	CourtDocument processCaseDetails(String caseFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		PapuaGuineaCourtDocument acd = new PapuaGuineaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}

/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(PapuaGuineaCourtDocument pgcd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String extractedText = document.select("h2").text();
				System.out.println(extractedText);
				String participants, decisionDate, caseId = null;
				// Note extracting only the first case Id in case of multiple case IDs
				String[] extractedArray = extractedText.split("\\[");
				participants = extractedArray[0];
				String[] caseAndDate = extractedArray[1].split("\\(");
				caseId = "["+caseAndDate[0];
				decisionDate = caseAndDate[1].substring(0, caseAndDate[1].toString().length()-1);
			
			/**	if(caseId.contains(";")){
					//String caseIdArray[] = caseId.split(";");
					caseId = caseId.split("[; ]")[0];
					System.out.println("Multiple Case Id: "+caseId);
				}**/
			
				//String[] extractedArray = extractedText.split("[");
				//participants = extractedArray[0];
				
				System.out.println("Case Id: "+caseId+" participants: "+participants+" Decision date: "+decisionDate);
				pgcd.setCaseId(caseId);
				pgcd.setParticipantsName(participants);
				pgcd.setDecisionDate(decisionDate);
				pgcd.setHeardDate(null);


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
