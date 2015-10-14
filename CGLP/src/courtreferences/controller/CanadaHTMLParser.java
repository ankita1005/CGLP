package courtreferences.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import courtreferences.canada.CanadianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;

public class CanadaHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private CanadaHTMLParser(){}
	
	public CanadaHTMLParser(String countryname,String courtname, String processedUser){
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
		CanadianCourtDocument ccd = new CanadianCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String pdfFileContent = extractContentFromDocument(ccd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(pdfFileContent);
		
		ccd.extractCitations(this.getCourtName(),pageContentList);
		return ccd;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(CanadianCourtDocument ccd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				Elements metadata = document.select("div[class=metadata] tr");
	        	ListIterator<Element> metaIterator = metadata.listIterator();
	        	HashMap<String,String> map1 = new HashMap<String,String>();
	        	map1.put("01", "january");
	        	map1.put("02", "february");
	        	map1.put("03", "march");
	        	map1.put("04", "april");
	        	map1.put("05", "may");
	        	map1.put("06", "june");
	        	map1.put("07", "july");
	        	map1.put("08", "august");
	        	map1.put("09", "september");
	        	map1.put("10", "october");
	        	map1.put("11", "november");
	        	map1.put("12", "december");
	        	map1.put("", "january");
	        	map1.put("2", "february");
	        	map1.put("3", "march");
	        	map1.put("4", "april");
	        	map1.put("5", "may");
	        	map1.put("6", "june");
	        	map1.put("7", "july");
	        	map1.put("8", "august");
	        	map1.put("9", "september");
	        	
	        	int index = 0;
	        	while(metaIterator.hasNext()){
	        		Element ele = metaIterator.next();
	        		if(index == 0){
	        			ccd.setParticipantsName(ele.child(1).text());
	        		}else if(index == 2){
	        			String[] parts = ele.child(1).text().split("-");
	        			
	        			ccd.setDecisionDate(parts[2]+" "+map1.get(parts[1])+" "+parts[0]);
//	        		}else if(index == 3){
//	        			ccd.setInternationalNumber(ele.child(1).text());
	        		}else if(index == 3){
	        			ccd.setCaseId(ele.child(1).text());
	        		}
	        		index++;
	        	}
	        	
	        	//ccd.setHeardDate(null);
	        	textContent = document.body().toString();
	        	
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing pdf : " + e.getMessage());
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
