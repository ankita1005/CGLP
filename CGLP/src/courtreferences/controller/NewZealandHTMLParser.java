package courtreferences.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import courtreferences.model.CourtDocument;
import courtreferences.newzealand.NewZealandCourtDocument;


public class NewZealandHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private NewZealandHTMLParser(){}
	
	public NewZealandHTMLParser(String countryname,String courtname, String processedUser){
		
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
		NewZealandCourtDocument nzcd = new NewZealandCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(nzcd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		nzcd.extractCitations(this.getCourtName(),pageContentList);
		return nzcd;
	}
	
	/* Gets the input HTML file name, start and end page of HTML and returns the text content using JSOUP API */
	
	String extractContentFromDocument(NewZealandCourtDocument nzcd, String htmlfile,int startpage,int endpage){
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
				String htmlText = document.body().toString();
				String textToBeExtracted = document.select("h2.make-database").text();
	        	System.out.println("text extracted ="+textToBeExtracted);
	        	int indexOfParticipantsEnd = textToBeExtracted.indexOf("[");
	        	String participantsName = textToBeExtracted.substring(0,indexOfParticipantsEnd-1);
	        	nzcd.setParticipantsName(participantsName);
	        	
	        	int indexOfDateStart = textToBeExtracted.indexOf("(");
	        	int indexOfDateEnd = textToBeExtracted.indexOf(")");
	        	String decideDate = textToBeExtracted.substring(indexOfDateStart+1, indexOfDateEnd);
	        	nzcd.setDecisionDate(decideDate);
	        	
	        	
	        	String caseIDPatternString = "SC ([0-9]+/[0-9]+|CRI [0-9]+/[0-9]+)";
	        	Pattern caseidPattern = Pattern.compile(caseIDPatternString);
	        	Matcher caseidMatcher = caseidPattern.matcher(htmlText);
	        	
	        	String caseID ="";
	        	while (caseidMatcher.find()) {
	        		caseID = caseidMatcher.group();
	        	    
	        	}
	        	nzcd.setCaseId(caseID);
	        	
	        	textContent = htmlText;
	        	
	        	
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

