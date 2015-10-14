package courtreferences.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import courtreferences.peru.PeruCourtDocument;
import courtreferences.model.CourtDocument;

public class PeruHTMLParser extends DocumentParser {

	private String countryName;
	private String courtName;
	private String processedUserName;
	private int fileLength;
	private int noOfPages;
	static DocumentParser d;
	
	private PeruHTMLParser(){}
	
	public PeruHTMLParser(String countryname,String courtname, String processedUser){
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
	static String combine(String[] s, String glue){
	      int k=s.length;
	      if (k==0) return null;
	      StringBuilder out=new StringBuilder();
	      out.append(s[0]);
	      for (int x=1;x<k;++x)
	        out.append(glue).append(s[x]);
	      return out.toString();
	    }
	/* Processes HTML Files
	 * Similar methods can be added for other country files. It should be written based on the structure of those documents 
	 * */
	
	CourtDocument processCaseDetails(String caseFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		PeruCourtDocument acd = new PeruCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
		String htmlFileContent = extractContentFromDocument(acd, caseFile,startPage,endPage);
		
		int filelength = caseFile.length();
		this.setFileLength(filelength);
		
		List<String> pageContentList = new ArrayList<String>();
//		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
//			pageContentList.add(extractContentFromDocument(caseFile,pageNo,pageNo));	
//		}
		pageContentList.add(htmlFileContent);
		
		//acd.extractCitations(this.getCourtName(),pageContentList);
		return acd;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(PeruCourtDocument acd, String pdfFile,int startpage,int endpage){
		
		Document document = null;
		File input = new File(pdfFile);
		String textContent = null;
		
		int inputYear = Integer.parseInt(pdfFile.substring(pdfFile.length()-20, pdfFile.length()-16));
		
		
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = Jsoup.parse(input,"UTF-8");
				String[] days1 = {"primero","segundo","tercera","cuarto","quinto",
						"sexto,séptimo","octavo","noveno","décimo","undécimo","duodécimo",
						"decimotercero","decimocuarto","decimoquinto","decimosexto",
						"decimoséptimo","decimoctavo","decimonoveno","vigésimo",
						"vigésimo primero","vigesimo segundo","vigesimo tercera",
						"vigésimo cuarto","vigésimo quinto","vigesimo sexto",
						"vigesimo séptimo","vigesimo octavo","vigésimo noveno","trigésimo",
						"trigésimo primero"};
				String[] days2 = {"primera","segunda","tercero","cuarta","quinta",
						"sexta,séptima","octava","novena","décima","undécima","duodécima",
						"decimotercera","decimocuarta","decimoquinta","decimosexta",
						"decimoséptima","decimoctava","decimonovena","vigésima",
						"vigésimo primera","vigesimo segunda","vigesimo tercero",
						"vigésimo cuarta","vigésimo quinta","vigesimo sexta",
						"vigesimo séptima","vigesimo octava","vigésimo novena","trigésimo",
						"trigésimo primera"};
				
				String[] days3 = {"veinte uno","veintidós",
						"veintitres","veinticuatro","veinticinco","veintiseis","veintisiete",
						"veintiocho","ventiocho","veintinueve","de treinta","treinta y uno","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve",
						"diez","once","doce","trece","catorce","de quince","dieciséis","diecisiete",
						"dieciocho","diecinueve","de veinte" };
				
				
				String[] months = {"enero","febrero","marcha","abril","mayo","junio","julio",
									"agosto","septiembre","octubre","noviembre","diciembre","setiembre"};
				
				
				String[] year = {"mil novecientos noventa y cinco","mil novecientos noventa y seis","mil novecientos noventa y siete",
						"mil novecientos noventa y ocho","mil novecientos noventa y nueve","mil novecientos noventiséis"};
				
			/*	String[] year = {"mil novecientos noventiséis","mil novecientos noventa y cinco","mil novecientos noventa y seis","mil novecientos noventa y siete",
						"mil novecientos noventa y ocho","mil novecientos noventa y nueve","dos mil",
						"doscientos uno","dos mil dos","dos mil tres","dos mil cuatro","dos mil cinco","dos mil seis",
						"dos mil siete","dos mil ocho","dos mil nueve","dos mil diez","dos mil once","dos mil doce","dos mil trece"};
				*/
				
				String[] daysinEnglish1 = {"01","02","03","04","05","06","07","08","09","10",
						"11","12","13","14","15","16","17","18","19","20","21","22","23","24",
						"25","26","27","28","29","30","31"};
				String[] daysinEnglish2 = {"01","02","03","04","05","06","07","08","09","10",
						"11","12","13","14","15","16","17","18","19","20","21","22","23","24",
						"25","26","27","28","29","30","31"};
				
				String[] daysinEnglish3 = {"21","22","23","24","25","26","27","28","28","29","30","31","01","02","03","04","05","06","07","08","09","10",
						"11","12","13","14","15","16","17","18","19","20"};
				
				String[] monthsinEnglish = {"january","february","march","april","may","june",
						"july","august","september","october","november","decmeber","september"};
				
				String[] yearinEnglish = {"1995","1996","1997","1998","1999","1900","2000","2001","2002","2003","2004","2005",
						"2006","2007","2008","2009","2010","2011","2012","2013"};
				
				//maps
				
				HashMap<String,String> daysMap1 = new HashMap<String,String>();
				HashMap<String,String> daysMap2 = new HashMap<String,String>();
				HashMap<String,String> daysMap3 = new HashMap<String,String>();
				
				HashMap<String,String> monthsMap = new HashMap<String,String>();
				HashMap<String,String> yearsMap = new HashMap<String,String>();
				
				
				for(int i=0;i<days1.length;i++)
				{
					daysMap1.put(days1[i], daysinEnglish1[i]);
				}
				for(int i=0;i<days2.length;i++)
				{
					daysMap2.put(days2[i], daysinEnglish2[i]);
				}
				for(int i=0;i<days3.length;i++)
				{
					daysMap3.put(days3[i], daysinEnglish3[i]);
				}
				for(int i=0;i<months.length;i++)
				{
					monthsMap.put(months[i], monthsinEnglish[i]);
				}
				for(int i=0;i<year.length;i++)
				{
					yearsMap.put(year[i], yearinEnglish[i]);
				}
				
				String decisionDate=null;
				String decisionMonth=null;
				String decisionYear = null;
				
				int date1 = 0;
				int year1 = 0;
				
				Pattern patDays1 = Pattern.compile("^.*?("+combine(days1,"|")+").*$");
				Pattern patDays2 = Pattern.compile("^.*?("+combine(days2,"|")+").*$");
				Pattern patDays3 = Pattern.compile("^.*?("+combine(days3,"|")+").*$");
				
				Pattern patMonths = Pattern.compile("^.*?("+combine(months,"|")+").*$");
				Pattern patYear = Pattern.compile("^.*?("+combine(year,"|")+").*$");
				
				Pattern patDaysinEnglish = Pattern.compile("^.*?("+combine(daysinEnglish1,"|")+").*$");
				Pattern patYearinEnglish = Pattern.compile("^.*?("+combine(yearinEnglish,"|")+").*$");
				
				
				
				
				String caseId = document.select("title").text();
				//System.out.println("case id is : "+caseId);
				acd.setCaseId(caseId);
				for(int i=0;i<document.select("p").size();i++)
				{
					String extractedText1 = document.select("p").get(i).text();
					if(extractedText1.contains("Lima") || extractedText1.contains("Arequipa"))
					{
						String extractedText = extractedText1.toLowerCase();
						
						if(extractedText.length() > 4 && extractedText.length() > 10)
						{
								

							Matcher matchDaysinEnglish = patDaysinEnglish.matcher(extractedText);
							Matcher matchYearinEnglish = patYearinEnglish.matcher(extractedText);
							
							Matcher matchDays1 = patDays1.matcher(extractedText);
							Matcher matchDays2 = patDays2.matcher(extractedText);
							Matcher matchDays3 = patDays3.matcher(extractedText);
							
							Matcher matchMonths = patMonths.matcher(extractedText);
							Matcher matchYear = patYear.matcher(extractedText);
								if(matchDays1.find())
								{
									
									decisionDate = daysMap1.get(matchDays1.group(1)); 
								}
								
								if(decisionDate == null)
								{
									if(matchDays2.find())
									{
										
										decisionDate = daysMap2.get(matchDays2.group(1)); 
									}
									
								}
								
								if(decisionDate == null)
								{
									if(matchDays3.find())
									{
										
										decisionDate = daysMap3.get(matchDays3.group(1)); 
									}
									
								}
							

								
								if(matchMonths.find())
								{
									decisionMonth = matchMonths.group(1);
									
								}
							
								if(matchYear.find())
								{
									decisionYear = matchYear.group(1);
									
								}
								
								if(matchDaysinEnglish.find())
								{
									date1 = Integer.parseInt(matchDaysinEnglish.group(1));
								}
								if(matchYearinEnglish.find())
								{
									year1 = Integer.parseInt(matchYearinEnglish.group(1));
								}
								
								
								
									if(decisionMonth != null)
									{
										if(decisionDate != null && decisionYear != null)
										{
											String decision = decisionDate+" "+monthsMap.get(decisionMonth)+" "+yearsMap.get(decisionYear);
											acd.setDecisionDate(decision);
											//System.out.print(decisionDate+"-");
											//System.out.print(monthsMap.get(decisionMonth)+"-");
											//System.out.println(yearsMap.get(decisionYear)+"-");
											//System.out.println(decisionDate);
										}
										if(date1 != 0 && year1 != 0)
										{
											String decision = date1+" "+monthsMap.get(decisionMonth)+" "+year1;
											acd.setDecisionDate(decision);
											
											//System.out.print(date1+"-");
											//System.out.print(monthsMap.get(decisionMonth)+"-");
											//System.out.println(year1);
											
										}
									}
									
								System.out.println(extractedText);	
								
								break;
						}
					}
					}
				
				
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
