package courtreferences.papuaguinea;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import courtreferences.model.CaseDetailsModel;
import courtreferences.model.CitationCases;
import courtreferences.model.CitationReferenceModel;
import courtreferences.model.Citations;
import courtreferences.model.CourtDocument;
import courtreferences.model.PageContent;
import courtreferences.model.ProcessCitations;


public class PapuaGuineaCourtDocument extends CourtDocument implements ProcessCitations {
	/*
	 * Each (country,court) has its own format. Extraction mechanism varies depending on that.
	 * Contains methods to extract case title and references to the foreign court in each citation
	 * 
	 */
	
	String internationalNumber;
	
	public String getInternationalNumber() {
		return internationalNumber;
	}

	public void setInternationalNumber(String internationalNumber) {
		this.internationalNumber = internationalNumber;
	}

	public PapuaGuineaCourtDocument(){
	}
	
	public PapuaGuineaCourtDocument(String countryName, String courtName, String processedUserName, String sourceFileName){
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
		
	/* Extracts case id for each Australian case document	*/
	
	public int extractCaseId(String CourtName,String pdfFileContent){
		String caseId = extractCNSTCRTCaseId(pdfFileContent);
		if(null != caseId){
			this.setCaseId(caseId);
			return 0;
		}else{
			return -1;
		}
	
	}
	
	/* Extracts Participants name for each SouthAfrican case document	*/
	
	public void extractParticipants(String CourtName,String pdfFileContent){
		if(CourtName.equals("Supreme Court")){
			this.setParticipantsName(CaseDetailsModel.retrieveParticipantsName(extractCNSTCRTParticipants(pdfFileContent)));
		}
		else{
			this.setParticipantsName(null);
		}
	}

	/* Extracts decision date for each Australian case document	*/
	
	public void extractDecisionDate(String CourtName,String pdfFileContent){
		if(CourtName.equals("Supreme Court"))
			this.setDecisionDate(extractCNSTCRTDecisionDate(pdfFileContent));
		else
			this.setDecisionDate(null);
	}
	
	/* Extracts heard date for each Australian case document	*/
	
	public void extractHeardDate(String CourtName,String pdfFileContent){
		if(CourtName.equals("Supreme Court"))
			this.setHeardDate(extractCNSTCRTHeardDate(pdfFileContent));
		else
			this.setHeardDate(null);
	}
	
	/* Extracts citations for each Australian case document	*/
	
	public void extractCitations(String CourtName,List<String> fileContent){
		
			extractCNSTCRTCitations(fileContent);
			
	}
	
	/* Extracts Case id for Australian Constitutional Court case document*/
	
	public String extractCNSTCRTCaseId(String pdfFileContent){
		int startIndex = checkCaseIdPattern(pdfFileContent,0,0);
		int endIndex = checkCaseIdPattern(pdfFileContent,0,1);
		if(startIndex != -1 && endIndex != -1){
			return pdfFileContent.substring(startIndex,endIndex);
		}
		return null;
	}
	
	/* Extracts citations for Canadian Constitutional Court case document*/
	
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
				PapuaGuineaCitations currentCitationObj = new PapuaGuineaCitations(this.getCaseId(),this.getCountryName(),this.getCourtName(),currentCitationId,currCitationString.trim(),currentPageNo);
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
			PageContent currentPage = new PapuaGuineaPageContent(currentPageNo, pageContent.toLowerCase());
			this.getDocumentPages().add(currentPage);
			currentPage.searchForeignReferencesInBodyContent();
		}
		return;
	}
	
	public void extractCitationsFromPageBodyContent(){
		for(PageContent bodyContent : this.getDocumentPages()){
			PapuaGuineaCitations citationObj = new PapuaGuineaCitations();
			citationObj.setCaseId("");
			citationObj.setCitationid(-1);
			citationObj.setCitationString(bodyContent.getPageContent());
			citationObj.setCitationCaseTitles(new ArrayList<String>());
			citationObj.setRefCases(new ArrayList<CitationCases>());
			
		}
	}
	
	/* Extracts the participants name from SouthAfrican Constitutional Court case document*/

	public List<String> extractCNSTCRTParticipants(String pdfFileContent){
		int startIndex = checkCaseIdPattern(pdfFileContent, 0, 1) + 1;
		int caseyearIndex = checkCaseYearPattern(pdfFileContent, 0, 1);
		if(caseyearIndex != -1){
			startIndex = caseyearIndex + 1;
		}
		int endIndex = checkCaseIdPattern(pdfFileContent,startIndex, 1);
		int endIndex1 = checkHeardDatePattern(pdfFileContent, 0, 0);
		if(endIndex1 == -1){
			endIndex1 = checkDatePattern(pdfFileContent, 0, 0);
			if(endIndex1 == -1){
				endIndex1 = pdfFileContent.length();
				endIndex1 = (endIndex1 == -1)?endIndex1:endIndex1-1;
			}
			else
				endIndex1 = endIndex1 - 1;
		}
		if(endIndex != -1 && endIndex1 != -1)
			endIndex = (endIndex<endIndex1)?endIndex:endIndex1;
		else if(endIndex1 != -1)
			endIndex = endIndex1;
		
		//skannan-comment: certain documents have case id after the heard date
		//the participants are at the top in such cases and this is to handle those cases
		if(endIndex < startIndex){
			String searchString = "Papua Guinea";
			endIndex1 = pdfFileContent.indexOf(searchString)+searchString.length();
			endIndex = startIndex;
			startIndex = endIndex;
		}
		return extractNames(pdfFileContent.substring(startIndex,endIndex));
	}
	
	/* Filters just the name of the participants	*/
	
	public List<String> extractNames(String inputString){
		List<String> nameList = new ArrayList<String>();
		String[] inputLines = inputString.split("\n");
		for(int lineno = 0; lineno<inputLines.length; lineno++){
			StringBuffer outputString = new StringBuffer("");
			int startIndex = 0;
			int endIndex = 0;
			if(lineno == inputLines.length - 1){
				if(checkCaseIdPattern(inputLines[lineno], 0, 0) != -1){
					break;
				}
			}
			while(startIndex < inputLines[lineno].length()){
				endIndex = checkCapitalNamePattern(inputLines[lineno], startIndex, 1);
				startIndex = checkCapitalNamePattern(inputLines[lineno], startIndex, 0);
				if(startIndex == -1 || endIndex == -1)
					break;
				outputString.append(inputLines[lineno].substring(startIndex, endIndex));
				outputString.append(" ");
				startIndex = endIndex;
			}
			if(outputString.length() != 0)
				nameList.add(outputString.toString());
		}
		return nameList;
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
	
	/* Searches for the occurrence of any names in the input String	and returns start or end index depending on the request*/
	
	int checkCapitalNamePattern(String inputString, int startIndex, int option){
		String namePatternString = "\\b([A-Z0-9])+\\b";
		Pattern namePattern = Pattern.compile(namePatternString);
		Matcher nameMatcher = namePattern.matcher(inputString);
		if(nameMatcher.find(startIndex) && option == 0)
			return nameMatcher.start();
		else if(nameMatcher.find(startIndex) && option == 1)
			return nameMatcher.end();
		else
			return -1;
	}
	
	/* Searches the pattern with case id in the inputString	 and returns start or end index depending on the request*/ 
	
	int checkCaseIdPattern(String inputString, int startIndex, int option){
		String caseidPatternString = "[0-9]+/[0-9]+";
		Pattern caseidPattern = Pattern.compile(caseidPatternString);
		Matcher caseidMatcher = caseidPattern.matcher(inputString);
		if(caseidMatcher.find(startIndex) && option == 0)
			return caseidMatcher.start();
		else if(caseidMatcher.find(startIndex) && option == 1)
			return caseidMatcher.end();
		else
			return -1;
	}
	
	/* Searches the year pattern in the input String and returns the start or end index depending on the request	*/
	
	int checkCaseYearPattern(String inputString, int startIndex,int option){
		String caseYearPatternString = "(?i)\\[([12][0-9]{3})\\]( )?ZACC( )?[0-9]+";
		Pattern caseYearPattern = Pattern.compile(caseYearPatternString);
		Matcher caseYearMatcher = caseYearPattern.matcher(inputString);
		if(caseYearMatcher.find(startIndex) && option == 0)
			return caseYearMatcher.start();
		else if(caseYearMatcher.find(startIndex) && option == 1)
			return caseYearMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the heard Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkHeardDatePattern(String inputString, int startIndex, int opt){
		String heardDatePatternString = "(?i)Hear(d)?( )?(On)?";
		Pattern heardDatePattern = Pattern.compile(heardDatePatternString);
		Matcher heardDateMatcher = heardDatePattern.matcher(inputString);
		if(heardDateMatcher.find() && opt == 0)
			return heardDateMatcher.start();
		else if(heardDateMatcher.find() && opt == 1)
			return heardDateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Decision Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkDecisionDatePattern(String inputString, int startIndex, int opt){
		String decisionDatePatternString = "(?i)(Deci(ded|sion)?|deli(vered)?)( )?(On)?(Date)?";
		Pattern decisionDatePattern = Pattern.compile(decisionDatePatternString);
		Matcher decisionDateMatcher = decisionDatePattern.matcher(inputString);
		if(decisionDateMatcher.find() && opt == 0)
			return decisionDateMatcher.start();
		else if(decisionDateMatcher.find() && opt == 1)
			return decisionDateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Date pattern in the input string and returns the start or end index depending on the request */
	
	int checkDatePattern(String inputString, int startIndex, int opt){
		String datePatternString = "(?i)([1-9]|[12][0-9]|3[01])[- /.](Jan(uary)?|Feb(ruary)?|Mar(ch)?|Apr(il)?|May|Jun(e)?|Jul(y)?|Aug(ust)?|Sep(tember)?|Oct(ober)?|Nov(ember)?|Dec(ember)?)[- /.](1[9][0-9][0-9]|2[0-9][0-9][0-9])";
		Pattern datePattern = Pattern.compile(datePatternString);
		Matcher dateMatcher = datePattern.matcher(inputString);
		if(dateMatcher.find(startIndex) && opt == 0)
			return dateMatcher.start();
		else if(dateMatcher.find(startIndex) && opt == 1)
			return dateMatcher.end();
		else
			return -1;
	}
	
	/* Searches for the Decision Date Pattern in Papua New Guinea Constitutional court. If not found, the default date is set */
	
	String extractCNSTCRTDecisionDate(String pdfFileContent){
		String decisionDate  = null;
		int startIndex = 0;
		int endIndex = 0;
		startIndex = checkDecisionDatePattern(pdfFileContent, startIndex, 0);
		startIndex = (startIndex != -1?startIndex:0);
		endIndex = checkDatePattern(pdfFileContent, startIndex, 1);
		startIndex = checkDatePattern(pdfFileContent, startIndex, 0);
		if(endIndex != -1 && startIndex != -1)
			decisionDate = pdfFileContent.substring(startIndex,endIndex);
		else
			decisionDate = "1 January 1900";
		return decisionDate;
	}

	/* Searches for the Heard Date Pattern in Papua New Guinea Constitutional court. If not found, the default date is set */
	String extractCNSTCRTHeardDate(String pdfFileContent){
		String heardDate  = null;
		int startIndex = 0;
		int endIndex = 0;
		startIndex = checkHeardDatePattern(pdfFileContent, startIndex, 0);
		startIndex = (startIndex != -1?startIndex:0);
		endIndex = checkDatePattern(pdfFileContent, startIndex, 1);
		startIndex = checkDatePattern(pdfFileContent, startIndex, 0);
		if(endIndex != -1 && startIndex != -1)
			heardDate = pdfFileContent.substring(startIndex,endIndex);
		else
			heardDate = "1 January 1900";
		return heardDate;
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
}
