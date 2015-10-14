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
import courtreferences.uksc.UKSCCourtDocument;


public class UKHouseOfLordsHTMLParser extends DocumentParser{
	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	
	
	private UKHouseOfLordsHTMLParser(){}
	
	public UKHouseOfLordsHTMLParser(String countryname,String courtname, String processedUser){
		
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
		UKSCCourtDocument bcd = new UKSCCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(bcd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		//bcd.extractCitations(this.getCourtName(),pageContentList);
		return bcd;
	}
	
	/* Gets the input HTML file name, start and end page of HTML and returns the text content using JSOUP API */
	static String combine(String[] s, String glue){
	      int k=s.length;
	      if (k==0) return null;
	      StringBuilder out=new StringBuilder();
	      out.append(s[0]);
	      for (int x=1;x<k;++x)
	        out.append(glue).append(s[x]);
	      return out.toString();
	    }
	String extractContentFromDocument(UKSCCourtDocument uksccd, String htmlfile,int startpage,int endpage){
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
				String[] monthsinEnglish = {"january","february","march","april","may","june",
						"july","august","september","october","november","december","september"};
				
	        	textContent = htmlText;
	        	
				Pattern caseIdPattern = Pattern.compile("UKHL");
				Pattern decisionDatePatetrn =  Pattern.compile("^.*?("+combine(monthsinEnglish,"|")+").*$");

				
				String participant = document.select("title").text();
				String[] participantName = participant.split("-");
				
				System.out.println(participantName[1]);
				uksccd.setParticipantsName(participantName[1]);
				
	        	String caseID=null;
	        	String[] decisionDate = null;
	        	for(int i=0;i<document.select("p").size();i++)
	        	{
					String extractedText = document.select("p").get(i).text();
					Matcher matchCaseId = caseIdPattern.matcher(extractedText);

					if(matchCaseId.find())
					{
						
						caseID = extractedText; 
						System.out.println(caseID);
						uksccd.setCaseId(caseID);
						break;
					}
					
	        	}
	        	
	        	for(int i=0;i<document.select("p").size();i++)
	        	{
					String extractedText = document.select("p").get(i).text();
					
					Matcher matchDecisionDate = decisionDatePatetrn.matcher(extractedText.toLowerCase());
					
					
					if(matchDecisionDate.find())
					{
						String[] decStrings = extractedText.split(" ");
						if(decStrings.length == 4)
						{
						decisionDate= extractedText.split("ON"); 
						System.out.println(decisionDate[1]);
						uksccd.setDecisionDate(decisionDate[1]);
						break;
						}
						
					}
					
	        	}
	        	
	        
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

