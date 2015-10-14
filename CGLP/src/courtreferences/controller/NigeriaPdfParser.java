package courtreferences.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;

import courtreferences.controller.DocumentParser;
import courtreferences.model.CourtDocument;
import courtreferences.nigeria.NigerianCourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;

public class NigeriaPdfParser extends DocumentParser {
	
	public NigeriaPdfParser(){
	}
	
	
	public NigeriaPdfParser(String countryname,String courtname, String processedUser){
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

	
	public CourtDocument processDocumentForCaseDetails(String pdfFile, String sourceFileName){
			return processCaseDetails(pdfFile, sourceFileName);
	}
	
	/* Processes Nigerian pdf Files
	 * */
	
	CourtDocument processCaseDetails(String pdfFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		String pdfFileContent = extractContentFromDocument(pdfFile,startPage,endPage);
		int filelength = pdfFile.length();
		this.setFileLength(filelength);
		NigerianCourtDocument naf = new NigerianCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
		while(naf.extractCaseId(this.getCourtName(),pdfFileContent) == -1){
			startPage++;
			endPage++;
			pdfFileContent = extractContentFromDocument(pdfFile,startPage,endPage);
		}
		naf.extractParticipants(this.getCourtName(),pdfFileContent);
		naf.extractDecisionDate(this.getCourtName(),pdfFileContent);
		naf.extractHeardDate(this.getCourtName(), pdfFileContent);
		
		List<String> pageContentList = new ArrayList<String>();
		for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
			pageContentList.add(extractContentFromDocument(pdfFile,pageNo,pageNo));	
		}
		naf.extractCitations(this.getCourtName(),pageContentList);
		return naf;
	}
	
	/* Gets the input PDF file name, start and end page of PDF and returns the text content using PDFBox API */
	
	String extractContentFromDocument(String pdfFile,int startpage,int endpage){
		boolean force = false;
		String password = null;
		String encoding = null;
		int startPage = startpage;
		int endPage = endpage;
		boolean separateBeads = true;
		boolean sort = false;
		PDDocument document = null;
		String textContent = null;
	    	
		if( pdfFile == null ){
			System.out.println("File name is not valid");
		}
		else{
			try{
				document = PDDocument.load(pdfFile, force);
	        	
				//SC263/2000
				
				/* To check password protection */        		
				if( document.isEncrypted()){
					StandardDecryptionMaterial sdm = new StandardDecryptionMaterial( password );
	        		document.openProtection( sdm );
	        		AccessPermission ap = document.getCurrentAccessPermission();
	       			if( ! ap.canExtractContent() ){
	       				throw new IOException( "You do not have permission to extract text" );
	       			}
	       		} 
				
	       		this.setNoOfPages(document.getNumberOfPages());	 
	   			PDFTextStripper txtStripper = null;
	       		txtStripper = new PDFTextStripper(encoding);
	       		txtStripper.setForceParsing( force );
	       		txtStripper.setSortByPosition( sort );
	       		txtStripper.setShouldSeparateByBeads( separateBeads );
	       		txtStripper.setStartPage( startPage );
	       		txtStripper.setEndPage( endPage );
	        		
	       		textContent = txtStripper.getText(document);
	       		//Content Extracted in NigerianCourtDocument
	    	       		if( document != null )
	       			document.close();
	       	}
	       	catch(Exception e){
	       		System.out.println("Error in parsing pdf : " + e.getMessage());
	       	}
		}
		return textContent;
	}

	public String getProcessedUserName() {
		return processedUserName;
	}

	public void setProcessedUserName(String processedUserName) {
		this.processedUserName = processedUserName;
	}
}
