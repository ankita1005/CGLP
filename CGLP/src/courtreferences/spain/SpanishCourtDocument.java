package courtreferences.spain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import courtreferences.australia.AustralianCitations;
import courtreferences.australia.AustralianPageContent;
import courtreferences.model.CaseDetailsModel;
import courtreferences.model.CitationCases;
import courtreferences.model.CitationReferenceModel;
import courtreferences.model.Citations;
import courtreferences.model.CourtDocument;
import courtreferences.model.PageContent;
import courtreferences.model.ProcessCitations;

public class SpanishCourtDocument extends CourtDocument implements ProcessCitations{

	String internationalNumber;
	
	public String getInternationalNumber() {
		return internationalNumber;
	}

	public void setInternationalNumber(String internationalNumber) {
		this.internationalNumber = internationalNumber;
	}
	
	public SpanishCourtDocument() {
	}
	
	public SpanishCourtDocument(String countryName, String courtName,
			String processedUserName, String sourceFileName) {
		this.setCountryId(CaseDetailsModel.retrieveCountryID(countryName));
		this.setCourtId(CaseDetailsModel.retrieveCourtID(this.getCountryId(),courtName));
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.setProcessedDate(new Date(System.currentTimeMillis()));
		this.setProcessedUser(processedUserName);
		this.setStatus("N");
		this.setSourceFileName(sourceFileName);
		this.setDuplicateCaseof(-1);
		this.citationObjs = new ArrayList<Citations>();
		this.documentPages = new ArrayList<PageContent>();
	}


	@Override
	public int extractCaseId(String CourtName, String pdfFileContent) {
		String caseId = extractCNSTCRTCaseId(pdfFileContent);
		if(null != caseId){
			this.setCaseId(caseId);
			return 0;
		}else{
			return -1;
		}
	}

	private String extractCNSTCRTCaseId(String pdfFileContent) {
		int startIndex = checkCaseIdPattern(pdfFileContent,0,0);
		int endIndex = checkCaseIdPattern(pdfFileContent,0,1);
		if(startIndex != -1 && endIndex != -1){
			return pdfFileContent.substring(startIndex,endIndex);
		}
		return null;
	}

	private int checkCaseIdPattern(String pdfFileContent, int i, int j) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void extractParticipants(String CourtName, String pdfFileContent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extractDecisionDate(String CourtName, String pdfFileContent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extractHeardDate(String CourtName, String pdfFileContent) {
		// TODO Auto-generated method stub
		
	}
	
	/* Writes the output to the Database	*/
	
	public void writeToDb(){
		try{
			CaseDetailsModel.insertCaseDetails(this);
			for(Citations c : this.getCitationObjs()){
				for(CitationCases cs : c.getRefCases()){
					cs.setCaseRefId(this.getCaseRefId());
					CitationReferenceModel.insertNewCitation(cs);
				}
			}
			for(PageContent p : this.getDocumentPages()){
				Set<String> insertedSet = new HashSet<String>();
				for(CitationCases cs : p.getRefCases()){
					cs.setCaseRefId(this.caseRefId);
					//if(CitationReferenceModel.checkCitationExistance(cs.getCaseRefId(), cs.getCitationid(), cs.getPageNumber(), cs.getCountryId(), cs.getCourtId()) > 0)
						//continue;
					String currentString = cs.getCaseRefId() + "\t" + cs.getCitationid() + "\t" + cs.getPageNumber() + "\t" + cs.getCountryId() + "\t" + cs.getCourtId(); 
					if(insertedSet.contains(currentString))
						continue;
					insertedSet.add(currentString);
					CitationReferenceModel.insertNewCitation(cs);
				}
			}
		}
		catch(Exception ex){
			
		}
	}

	@Override
	public void extractCitations(String CourtName, List<String> fileContent) {
		extractCNSTCRTCitations(fileContent);
	}
	
	public void extractCNSTCRTCitations(List<String> fileContent){
		int currentCitationId = 1;
		int citationStartIndex = -1;
		int citationEndIndex = -1;
		int currentPageNo = 0;
		for(String pageContent : fileContent){
			int startIndex = 0;
			int footerStartIndex = -1;
			boolean footerStartFlag = false;
			while(true){
				citationStartIndex = this.checkCitationPattern(pageContent, currentCitationId, startIndex);
				if(!footerStartFlag){
					footerStartIndex = citationStartIndex;
					footerStartFlag = true;
				}
				if(citationStartIndex == -1)
					break;
				citationEndIndex = this.checkCitationPattern(pageContent, currentCitationId + 1, citationStartIndex);
				if(citationEndIndex == -1)
					citationEndIndex = pageContent.length() - 1;
				String currCitationString = pageContent.substring(citationStartIndex, citationEndIndex).replaceAll("\n", " ");
				
				/*	 Australian Citation object is created for each citation found in the footer	*/
				SpanishCitations currentCitationObj = new SpanishCitations(this.getCaseId(),this.getCountryName(),this.getCourtName(),currentCitationId,currCitationString.trim(),currentPageNo);
				this.getCitationObjs().add(currentCitationObj);
				currentCitationId += 1;
				startIndex = citationEndIndex;
			}
			currentPageNo += 1;
			if(footerStartIndex == -1){
				footerStartIndex = pageContent.length();
			}
			
			//changed the pageContent.substring(0,footerStartIndex) to pageContent as it was not extracting important references
			//changed from pagecontent.tolowercase to see if pattern is matched
			PageContent currentPage = new SpanishPageContent(currentPageNo, pageContent.toLowerCase());
			this.getDocumentPages().add(currentPage);
			currentPage.searchForeignReferencesInBodyContent();
		}
		return;
	}
	
/* Pattern which matches the inputString and searches for the citationid	*/
	
	int checkCitationPattern(String inputString, int citationid, int startindex){
		String citationPatternString = "\n" + citationid + " .*";
		Pattern citationPattern = Pattern.compile(citationPatternString);
		Matcher citationMatcher = citationPattern.matcher(inputString);
		if(citationMatcher.find(startindex))
			return citationMatcher.start();
		else
			return -1;
	}

}
