package courtreferences.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import courtreferences.australia.AustralianCourtDocument;
import courtreferences.colombia.ColombianCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.util.SpanishtoEngDateHelper;

public class ColombiaHTMLParser extends DocumentParser {

	public ColombiaHTMLParser(String countryname, String courtname,
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
		ColombianCourtDocument ccd = new ColombianCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		String htmlFileContent = extractContentFromDocument(ccd, caseFile,startPage,endPage);
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		
		List<String> pageContentList = new ArrayList<String>();
		pageContentList.add(htmlFileContent);
		
		ccd.extractCitations(this.getCourtName(),pageContentList);
		return ccd;
	}

/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(ColombianCourtDocument ccd, String pdfFile,int startpage,int endpage){				
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
				String caseidString = "\\bSentencia\\b.*\\d{1}\\<"; 
				Pattern caseidPattern = Pattern.compile(caseidString,Pattern.CASE_INSENSITIVE);
				Matcher caseidMatcher = caseidPattern.matcher(htmlText);
				caseidMatcher.find();
			//	System.out.println(caseidMatcher.group());
				String caseId = caseidMatcher.group();
				System.out.println(caseId.substring(0, caseId.length()-1));
				//String htmlText1 = removeDiacriticalMarks(htmlText);
				String decisionDate = null;
			//	String dateString = ".*\bBogot\u00E1.*";
			//	String dateString = "\\bBogot\u00E1.*\\d{4}";
				
				String dateString = "\\bD\\.?C\\.?,.*\\d{4}";
			//	Pattern datePattern = Pattern.compile(dateString, (Pattern.UNICODE_CASE|Pattern.CANON_EQ|Pattern.CASE_INSENSITIVE));
				Pattern datePattern = Pattern.compile(dateString);

				Matcher dateMatcher = datePattern.matcher(htmlText);
				//dateMatcher.find();				
				String spanishDate = null;
				if(dateMatcher.find())
				{
					spanishDate = dateMatcher.group();
					System.out.println(dateMatcher.group());
				}
				
				else 	
				{
					dateString = "\\bBogot\\u00E1.*\\b\\d{4}";
					datePattern = Pattern.compile(dateString);

					dateMatcher = datePattern.matcher(htmlText);
					dateMatcher.find();
					System.out.println(dateMatcher.group());
					spanishDate = dateMatcher.group();

				}
				String dayExp = "\\b\\d{1,2}\\b";
				Pattern dayPattern = Pattern.compile(dayExp);
				Matcher dayMatcher = dayPattern.matcher(spanishDate);
				dayMatcher.find();
				String day = dayMatcher.group();
				//System.out.println(dayMatcher.group());
				String year = spanishDate.substring(spanishDate.length()-4, spanishDate.length());
				//System.out.println(year);
				SpanishtoEngDateHelper spanishtoEng = new SpanishtoEngDateHelper();
				String monthString = null;
				 Iterator it = spanishtoEng.spanishtoEngMonths.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				       // System.out.println(pair.getKey() + " = " + pair.getValue());
				        //if(spanishDate.contains(spanishtoEng.spanishtoEng.get(pair.getKey())))
				        if(spanishDate.toLowerCase().matches(".*\\b" + pair.getKey() + "\\b.*"))
				        {
				        	monthString = pair.getValue().toString();
				        	break;
				        }
				    }
					    
				//System.out.println(monthString);
				decisionDate = day+" "+monthString+" "+year;
				System.out.println("decisionDate: "+decisionDate);
				

			//	String caseID = document.select("span:contains(Sentencia)").first().text();
				
				//String caseID = document.select("span:matchesOwn(Sentencia)").first().text();

			//	System.out.println(caseID);
				//String decisionDate = document.select("p:contains(Bogotá)").first().text();
				//System.out.println("date: "+decisionDate);
				
				/*String extractedText = document.select("h2").text();
				System.out.println("Colombian extracted text"+extractedText);
				String [] elements = extractedText.split(";");
				//System.out.println(elements[0]);
				ccd.setParticipantsName(elements[0]);
				
				String caseAndDate = "";
				
				caseAndDate = elements[elements.length-1];
				
				int index = caseAndDate.length()-1;
				
				while(caseAndDate.charAt(index) != '('){
					index--;
				}
				int endIndex = index;
				while(caseAndDate.charAt(endIndex) != ')'){
					endIndex++;
				}**/
				ccd.setCaseId(caseId.substring(0, caseId.length()-1));				
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

	public static String removeDiacriticalMarks(String string) {
	    return Normalizer.normalize(string, Form.NFD)
	        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
