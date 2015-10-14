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
import courtreferences.usa.*;

public class USAHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private USAHTMLParser(){}
	
	public USAHTMLParser(String countryname,String courtname, String processedUser){
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
		USACourtDocument usacd = new USACourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(usacd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		usacd.extractCitations(this.getCourtName(),pageContentList);
		return usacd;
	}
	
	/* Gets the input HTML file name, start and end page of HTML and returns the text content using JSOUP API */
	
	String extractContentFromDocument(USACourtDocument usacd, String htmlfile,int startpage,int endpage){
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
				//String participantsName = document.select("center").first().select("h3").text();
				String textToBeExtraxcted = document.select("center").text();
				
				String participantsInvolved = document.select("center").select("p").first().text();
				System.out.println("Case Id = "+participantsInvolved);
				usacd.setParticipantsName(participantsInvolved);
				int indexOfCaseIdStartingWithNo = textToBeExtraxcted.indexOf("No.");
				if(indexOfCaseIdStartingWithNo<1){//if not found put participantsName as caseId for now
					if(participantsInvolved.length()>100){
						usacd.setCaseId(participantsInvolved.substring(0, 99));
					}
					else{
						usacd.setCaseId(participantsInvolved);
					}
					
				}
				else{
					usacd.setCaseId(textToBeExtraxcted.substring(indexOfCaseIdStartingWithNo,indexOfCaseIdStartingWithNo+14));//14 is a safe number calculated by looking at documents.May need to change
				}
				
				int indexDecidedDate = textToBeExtraxcted.indexOf("Decided"); 
				if(indexDecidedDate>0){
					usacd.setDecisionDate(textToBeExtraxcted.substring(indexDecidedDate+8,indexDecidedDate+22));
				}
				else{
					usacd.setDecisionDate(null);
				}
				System.out.println("decided date="+textToBeExtraxcted.substring(indexDecidedDate+8,indexDecidedDate+22));
				
				int indexHeardDate = textToBeExtraxcted.indexOf("Argued"); 
				int heardEndDate = textToBeExtraxcted.indexOf("--Decided");
				if(indexHeardDate>0){
					if(heardEndDate>0)
						usacd.setHeardDate(textToBeExtraxcted.substring(indexHeardDate+7,heardEndDate-1));
					else
						usacd.setHeardDate(textToBeExtraxcted.substring(indexHeardDate+7,indexHeardDate+21));
				}
				else{
					usacd.setHeardDate(null);
				}
				System.out.println("Heard date="+textToBeExtraxcted.substring(indexHeardDate+7,indexHeardDate+21));	
				
				

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
//<center> 
//<h2>U.S. Supreme Court </h2> 
//<h3> ASHCRAFT v. STATE OF TENN., 327 U.S. 274 (1946) </h3> 
//<b> <p> 327 U.S. 274 </p></b>
//<p><b> ASHCRAFT et al. <br />v. <br /> STATE OF TENNESSEE. <br /> No. 381. <br /> <br /> Argued Feb. 6, 7, 1946. <br />Decided Feb. 25, 1946. <br /> </b> </p>
//</center>
////System.out.println("USA participants text"+participantsName);
////String [] elements = extractedText.split(";");
//////System.out.println(elements[0]);
//usacd.setParticipantsName(participantsName);
//String caseID = document.select("center").first().select("p").first().text();
////System.out.println("USA Caseid text"+caseID);
//usacd.setCaseId(caseID);
//
//String prepareDateText = document.select("center").first().select("p").text();
//int indexDecidedDate = prepareDateText.indexOf("Decided"); 
//if(indexDecidedDate>0){
//usacd.setDecisionDate(prepareDateText.substring(indexDecidedDate+8));
//}
//else{
//usacd.setDecisionDate(null);
//}
//System.out.println(prepareDateText.substring(indexDecidedDate+8));
//
//int indexHeardDate = prepareDateText.indexOf("Argued"); 
//if(indexHeardDate>0){
//usacd.setHeardDate(prepareDateText.substring(indexHeardDate+7));
//}
//else{
//usacd.setHeardDate(null);
//}
//System.out.println(prepareDateText.substring(indexDecidedDate+7));
//usacd.setDecisionDate(caseAndDate.substring(index+1,endIndex));
//Ireland?irish
