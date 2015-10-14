package courtreferences.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.PartialResultException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;

import courtreferences.controller.DocumentParser;
import courtreferences.malaysia.MalaysiaCourtDocument;
import courtreferences.model.CourtDocument;
import courtreferences.southafrica.SouthAfricanCourtDocument;

public class MalaysiaPdfParser extends DocumentParser {
	/* 
	 * Contains basic structure of each pdf file which is needed to be processed
	 * The countries which have pdf document as their court documents can be instantiated with this class
	 * Have methods to parse PDF documents and extract titles and the contents themselves
	 * 
	*/
	
	public MalaysiaPdfParser(){
	}
	
	
	public MalaysiaPdfParser(String countryname,String courtname, String processedUser){
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
		//CitationCases.setRefid(1);
		//skannan-comment: changed to have one file for pdf and one for HTML 

			return processCaseDetails(pdfFile, sourceFileName);
	}
	
	/* Processes South African pdf Files
	 * Similar methods can be added for other country files. It should be written based on the structure of those documents 
	 * */
	
	CourtDocument processCaseDetails(String pdfFile, String sourceFileName){
		int startPage = 1;
		int endPage = 1;
		System.out.println(pdfFile);
		String pdfFileContent = extractContentFromDocument(pdfFile,startPage,endPage);
		int filelength = pdfFile.length();
		this.setFileLength(filelength);
		MalaysiaCourtDocument maf = new MalaysiaCourtDocument(this.getCountryName(),this.getCourtName(), this.getProcessedUserName(), sourceFileName);
		
		while(maf.extractCaseId(this.getCourtName(),pdfFileContent) == -1){
			startPage++;
			endPage++;
			pdfFileContent = extractContentFromDocument(pdfFile,startPage,endPage);
		}
		maf.extractParticipants(this.getCourtName(),pdfFileContent);
		maf.extractDecisionDate(this.getCourtName(),pdfFileContent);
		maf.extractHeardDate(this.getCourtName(), pdfFileContent);
		
		List<String> pageContentList = new ArrayList<String>();
		//for(int pageNo=startPage; pageNo<=this.getNoOfPages(); pageNo++){
			pageContentList.add(extractContentFromDocument(pdfFile,1,1));	
		//}
		//maf.extractCitations(this.getCourtName(),pageContentList);
		return maf;
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
	       	 List<String> lines= Arrays.asList(textContent.split("\r\n"));
	       	 String participant1=null,participant2=null,participant=null,caseId=null;
	       	String[] p1 = null;
	       	String[] p2 =null;
	       	 for(String line:lines)
	       		{
	       			if(line.contains("PERAYU"))
	       			{
	       				 p1 = line.split("PERAYU");
	       				//participant1 = p1[0];
	       				//break;
	       			}
	       			if(line.contains("RESPONDEN"))
	       			{
	       				p2 = line.split("RESPONDEN");
	       				//participant2 = p2[0];
	       				//break;
	       			}
	       			if(line.contains("No."))
	       			{
	       				caseId = line;
	       				
	       			}

	       		}
	       		
	       		
	       			       		
	       		participant = p1[0]+" Vs "+p2[0];
	       		if(participant!=null && caseId != null)
	       		{
	       		System.out.println(participant);
	       		System.out.println(caseId);
	       		}
	       		
	       		
	       	
	       		
	       		
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
