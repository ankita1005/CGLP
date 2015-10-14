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

import courtreferences.australia.AustralianCourtDocument;
import courtreferences.canada.CanadianCourtDocument;
import courtreferences.france.FranceCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;

public class FranceHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	static DocumentParser d;
	
	private FranceHTMLParser(){}
	
	public FranceHTMLParser(String countryname,String courtname, String processedUser){
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
		FranceCourtDocument acd = new FranceCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
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
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(FranceCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String extractedText = document.select("title").text();
				
				HashMap<String,String> map1 = new HashMap<String,String>();
				map1.put("janvier", "january");
				map1.put("février", "february");
				map1.put("mars", "march");
				map1.put("avril", "april");
				map1.put("mai", "may");
				map1.put("juin", "june");
				map1.put("juillet", "july");
				map1.put("août", "august");
				map1.put("septembre", "september");
				map1.put("octobre", "october");
				map1.put("novembre", "november");
				map1.put("décembre", "december");
				String[] data = extractedText.split("du");
				String[] decisionNumber = data[0].split("n°");
				//System.out.println("Decison Number "+decisionNumber[1]);
				
				String[] decisionDate = data[1].split(" ");
				//System.out.println("Decision Date "+map1.get(decisionDate[2])+"-"+decisionDate[1]+"-"+decisionDate[3]);
				
				acd.setCaseId(decisionNumber[1]);
				acd.setParticipantsName(null);
				acd.setDecisionDate(decisionDate[1]+" "+map1.get(decisionDate[2])+" "+decisionDate[3]);
				acd.setHeardDate(null);
				
				/*acd.setParticipantsName(elements[0]);
				
				String caseAndDate = "";
				
				caseAndDate = elements[elements.length-1];
				
				int index = caseAndDate.length()-1;
				
				while(caseAndDate.charAt(index) != '('){
					index--;
				}
				//System.out.println("length =  "+ caseAndDate.length());
				int endIndex = index;
				while(caseAndDate.charAt(endIndex) != ')'){
					//System.out.println("End Index="+endIndex);
					endIndex++;
				}
				//System.out.println("Case is:"+caseAndDate.substring(0, index-1));
				acd.setCaseId(caseAndDate.substring(0, index-1));
				
				//System.out.println("decision date"+ caseAndDate.substring(index+1,endIndex));
				acd.setDecisionDate(caseAndDate.substring(index+1,endIndex));
				acd.setHeardDate(null);

	        	textContent = document.body().toString();
	        	*/
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
