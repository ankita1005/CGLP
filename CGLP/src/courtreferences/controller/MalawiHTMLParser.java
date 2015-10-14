package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.NodeList;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import courtreferences.malawi.MalawiCourtDocument;
import courtreferences.model.CourtDocument;

public class MalawiHTMLParser extends DocumentParser {
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	public MalawiHTMLParser(String countryname, String courtname,
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

	@Override
	CourtDocument processCaseDetails(String caseFile, String sourceFileName) {

		int startPage = 1;
		int endPage = 1;
		MalawiCourtDocument acd = new MalawiCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	
	}

	
	String extractContentFromDocument(MalawiCourtDocument mcd, String pdfFile,int startpage,int endpage) {
	
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
				String participantsText = document.select("h1").text();
				if(participantsText!=null)
					
				System.out.println("Participants: "+participantsText);
				
				String caseidString = " \\[\\d{4}\\]\\s\\bMWSC\\s\\d+"; 
				Pattern caseidPattern = Pattern.compile(caseidString,Pattern.CASE_INSENSITIVE);
				Matcher caseidMatcher = caseidPattern.matcher(htmlText);
				caseidMatcher.find();
				String caseId = caseidMatcher.group();
				System.out.println("Case id: "+caseId);
				Elements decisionDateNode = document.select("div.field.field-type-datestamp.field-field-jdate").select("div.field-items").select("div.field-item.odd").select("span.date-display-single");
				String decisionDateString, decisionDate = null;
				if(decisionDateNode!=null){
					decisionDateString = decisionDateNode.first().ownText();
					decisionDate = decisionDateString.split(",")[1].trim();
				}
					
				System.out.println("decision Date: "+decisionDate);
				mcd.setCaseId(caseId);
				mcd.setDecisionDate(decisionDate);
				mcd.setParticipantsName(participantsText);
				
				
	        	textContent = document.body().toString();
	        	
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing html : " + e.getMessage());
	       	}
		}
		return textContent;
	
	}
	@Override
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
