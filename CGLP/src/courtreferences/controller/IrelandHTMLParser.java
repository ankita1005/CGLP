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

import courtreferences.model.CourtDocument;
import courtreferences.ireland.*;

public class IrelandHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private IrelandHTMLParser(){}
	
	public IrelandHTMLParser(String countryname,String courtname, String processedUser){
		System.out.println("here");
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
		IrelandCourtDocument icd = new IrelandCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(icd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		icd.extractCitations(this.getCourtName(),pageContentList);
		return icd;
	}
	
	/* Gets the input HTML file name, start and end page of HTML and returns the text content using JSOUP API */
	
	String extractContentFromDocument(IrelandCourtDocument icd, String htmlfile,int startpage,int endpage){
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
				Elements metadata = document.select("div#ContentsFrame font");
	        	ListIterator<Element> metaIterator = metadata.listIterator();
	        	int index = 0;
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
	        	
	        	while(metaIterator.hasNext()){
	        		Element ele = metaIterator.next();
	        		
	        		if(index == 3){
	        			//icd.setParticipantsName(ele.child(1).text());
	        			String participantsName = ele.text();
	        			icd.setParticipantsName(participantsName);
	        		}else if(index == 9){
	        			//ccd.setDecisionDate(ele.child(1).text());
	        			String caseID = ele.text();
	        			icd.setCaseId(caseID);
//	        		}else if(index == 3){
//	        			ccd.setInternationalNumber(ele.child(1).text());
	        		}else if(index == 17){
	        			String date = ele.text();	
	        			String[] date1 = date.split("/");
	        			
	        			icd.setDecisionDate(date1[1]+" "+map1.get(date1[0]).toString()+" "+date1[2]);
	        		}
	        		index++;
	        	}
	        	
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
