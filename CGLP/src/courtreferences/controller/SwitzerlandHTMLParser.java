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

import courtreferences.australia.AustralianCourtDocument;
import courtreferences.canada.CanadianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;
import courtreferences.switzerland.SwitzerlandCourtDocument;

public class SwitzerlandHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private SwitzerlandHTMLParser(){}
	
	public SwitzerlandHTMLParser(String countryname,String courtname, String processedUser){
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
			return processCaseDetails(caseFile, sourceFileName);

	}
	
	/* Processes HTML Files
	 * */
	
	CourtDocument processCaseDetails(String caseFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		SwitzerlandCourtDocument acd = new SwitzerlandCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(SwitzerlandCourtDocument scd, String pdfFile,int startpage,int endpage){
		
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
				//System.out.println("html"+htmlText );
				
				
				String caseidString = "Urteilskopf\\s?\\<\\/div\\>\\s?\\<br"
						+ "\\s?\\/\\>(.*?)\\<br\\s?"
						+ "\\/\\>";
				//String pattern1 = "Urteilskopf\\s?</div><br>";
				String pattern1 = "Urteilskopf";
				//String pattern2 = "\\d{1,}\\sIa?\\s\\d{1,}";
				String pattern2 = "<br/s?\\>";
				
				//decision date
				Pattern datePattern = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2),
		                Pattern.DOTALL);
				Matcher caseIdMatcher = datePattern.matcher(htmlText);
				
				String caseId = null;
				while (caseIdMatcher.find()) {

				  caseId = caseIdMatcher.group(1);
				  System.out.println("Date: "+caseIdMatcher.group(1));

				}
				System.out.println(caseId);
				
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
