package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import courtreferences.chile.ChileCourtDocument;
import courtreferences.colombia.ColombianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.util.SpanishtoEngDateHelper;

public class ChileHTMLParser extends DocumentParser {

	public ChileHTMLParser(String countryname, String courtname,
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

	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}

	@Override
	public CourtDocument processDocumentForCaseDetails(String caseFile,
			String sourceFileName) {
		// TODO Auto-generated method stub
		return processCaseDetails(caseFile, sourceFileName);
	}

	@Override
	CourtDocument processCaseDetails(String caseFile, String sourceFileName) {

		int startPage = 1;
		int endPage = 1;
		ChileCourtDocument ccd = new ChileCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(ccd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		//ccd.extractCitations(this.getCourtName(),pageContentList);
		return ccd;
	
	}

	String extractContentFromDocument(ChileCourtDocument ccd, String pdfFile,int startpage,int endpage){				
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
				String caseidString = "\\<b\\>role?s?\\sn.*\\<\\/b\\>"; 
				Pattern caseidPattern = Pattern.compile(caseidString,Pattern.CASE_INSENSITIVE);
				Matcher caseidMatcher = caseidPattern.matcher(htmlText);
				caseidMatcher.find();
				String caseId = caseidMatcher.group();
				System.out.println(caseId);
				if(caseId.contains("<b>")){
					caseId = caseId.replaceAll("<b>", "");
				}
				if(caseId.contains("</b>"))
				{
					caseId = caseId.replaceAll("</b>", "");
				}
				if(caseId.contains("</span>"))
				{
					caseId = caseId.replaceAll("</span>", "");
				}
				if(caseId.contains("<span>"))
				{
					caseId = caseId.replaceAll("<span>", "");
				}
				if(caseId.contains("<span lang=\"es-CL\">"))
				{
					caseId = caseId.replaceAll("<span lang=\"es-CL\">", "");
				}
				if(caseId.contains("<span lang=\"es-ES\">"))
				{
					caseId = caseId.replaceAll("<span lang=\"es-ES\">", "");
				}
				if(caseId.contains("<span lang=\"\">"))
				{
					caseId = caseId.replaceAll("<span lang=\"\">", "");
				}
				if(caseId.contains("<span lang=\"en-US\">"))
				{
					caseId = caseId.replaceAll("<span lang=\"en-US\">", "");
				}
				if(caseId.contains("&deg;"))
				{
					caseId = caseId.replaceAll("&deg;", "°");
				}
				if(caseId.contains("&ordm;"))
				{
					caseId = caseId.replaceAll("&ordm;", "º");
				}
				
				if(caseId.contains("."))
				{
					System.out.println("stop "+caseId.indexOf("."));
					caseId = caseId.replace(".", "");
				}
		
				System.out.println("New Case Id: "+caseId);
				String decisionDate = null;
				SpanishtoEngDateHelper spanishtoEng = new SpanishtoEngDateHelper();
				String dateString = "\\bSantiago\\,\\s.*?\\.";
				//	Pattern datePattern = Pattern.compile(dateString, (Pattern.UNICODE_CASE|Pattern.CANON_EQ|Pattern.CASE_INSENSITIVE));
					Pattern datePattern = Pattern.compile(dateString);

					Matcher dateMatcher = datePattern.matcher(htmlText);
					//dateMatcher.find();				
					String spanishDate = null;
					if(dateMatcher.find())
					{
						spanishDate = dateMatcher.group();
						System.out.println(dateMatcher.group());
						
						if(spanishDate.contains("</span>"))
						{
							spanishDate = spanishDate.replaceAll("</span>", "");
						}
						if(spanishDate.contains("<span>"))
						{
							spanishDate = spanishDate.replaceAll("<span>", "");
						}
						if(spanishDate.contains("<span lang=\"es-CL\">"))
						{
							spanishDate = spanishDate.replaceAll("<span lang=\"es-CL\">", "");
						}
						if(spanishDate.contains("<span lang=\"es-ES\">"))
						{
							spanishDate = spanishDate.replaceAll("<span lang=\"es-ES\">", "");
						}
						if(spanishDate.contains("<span lang=\"\">"))
						{
							spanishDate = spanishDate.replaceAll("<span lang=\"\">", "");
						}
						if(spanishDate.contains("<span lang=\"en-US\">"))
						{
							spanishDate = spanishDate.replaceAll("<span lang=\"en-US\">", "");
						}
						
						
					
					spanishDate = spanishDate.replace("Santiago, ", "");
					if(spanishDate.contains("</span><span lang=\"es-ES\">"))
						spanishDate = spanishDate.replaceAll("</span><span lang=\"es-ES\">", "");
					String spanishYearString = null;
					String year = null;
					if(spanishDate.contains("de dos mil")){
						int startIndex = spanishDate.indexOf("de dos mil");
						spanishYearString = spanishDate.substring(startIndex);
						spanishYearString = spanishYearString.replace(".", "");
						System.out.println("spanishYearString "+spanishYearString);
						if(spanishtoEng.spanishtoEngDosMil.containsKey(spanishYearString));
							year = spanishtoEng.spanishtoEngDosMil.get(spanishYearString);
					}
					if(spanishDate.contains("de mil")){
						int startIndex = spanishDate.indexOf("de mil");
						spanishYearString = spanishDate.substring(startIndex);
						spanishYearString = spanishYearString.replace(".", "");
						System.out.println("spanishYearString "+spanishYearString);
						if(spanishtoEng.spanishtoEngDeMil.containsKey(spanishYearString));
							year = spanishtoEng.spanishtoEngDeMil.get(spanishYearString);
					}
					
					System.out.println("year "+year);
					spanishDate = spanishDate.replace(spanishYearString, "");
					
					
					String monthString = null;
					 Iterator it = spanishtoEng.spanishtoEngMonths.entrySet().iterator();
					    while (it.hasNext()) {
					        Map.Entry pair = (Map.Entry)it.next();
					       // System.out.println(pair.getKey() + " = " + pair.getValue());
					        //if(spanishDate.contains(spanishtoEng.spanishtoEng.get(pair.getKey())))
					        if(spanishDate.toLowerCase().matches(".*\\b" + pair.getKey() + "\\b.*"))
					        {
					        	monthString = pair.getValue().toString();
					        	spanishDate = spanishDate.replace(pair.getKey().toString(), "");
					        	break;
					        }
					    }
					    System.out.println("month: "+monthString);
					    					    
					System.out.println("LeftOver Date: "+spanishDate);
					if(spanishDate.contains("de"))
						spanishDate = spanishDate.replace("de", "");
					if(spanishDate.contains("."))
						spanishDate = spanishDate.replace(".", "");
					System.out.println("final spanishDate: "+spanishDate);
					spanishDate = spanishDate.replaceAll("\\s","");
					
					String day = null;
					if(spanishtoEng.spanishtoEngNum.containsKey(spanishDate.trim()))
						day = spanishtoEng.spanishtoEngNum.get(spanishDate);
					
					System.out.println("day: "+day);
				
					decisionDate = day+" "+monthString+" "+year;
					}
					System.out.println("decisionDate"+decisionDate);
						
					
				
				ccd.setCaseId(caseId);				
				ccd.setDecisionDate(decisionDate);
				ccd.setHeardDate(null);

	        	textContent = document.body().toString();
	        	//PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
	        	//System.setOut(out);
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing html : " + e.getMessage());
	       	}
		}
		return textContent;
	}
	
	
	@Override
	String extractContentFromDocument(String pdfFile, int startpage, int endpage) {
		// TODO Auto-generated method stub
		return null;
	}

}
