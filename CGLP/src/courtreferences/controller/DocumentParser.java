package courtreferences.controller;

import courtreferences.model.CourtDocument;

public abstract class DocumentParser {
	protected String countryName;
	protected String courtName;
	protected String processedUserName;
	protected int fileLength;
	protected int noOfPages;
//	
	//this is an abstract class
//	public DocumentParser(){
//	}
//	
//	public DocumentParser(String countryname,String courtname, String processedUser){
//		this.setCountryName(countryname);
//		this.setCourtName(courtname);
//		this.setProcessedUserName(processedUser);
//	}
	
	public abstract CourtDocument processDocumentForCaseDetails(String pdfFile, String sourceFileName);
	
	abstract CourtDocument processCaseDetails(String pdfFile, String sourceFileName);
	
	String extractContentFromDocument(CourtDocument cd,String pdfFile,int startpage,int endpage){ return null;}
	
	abstract String extractContentFromDocument(String pdfFile,int startpage,int endpage);

}
