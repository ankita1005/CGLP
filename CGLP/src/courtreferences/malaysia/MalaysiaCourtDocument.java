package courtreferences.malaysia;

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

public class MalaysiaCourtDocument extends CourtDocument implements ProcessCitations{

	String internationalNumber;
	
	public String getInternationalNumber() {
		return internationalNumber;
	}

	public void setInternationalNumber(String internationalNumber) {
		this.internationalNumber = internationalNumber;
	}
	
	public MalaysiaCourtDocument() {
	}
	
	public MalaysiaCourtDocument(String countryName, String courtName,
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
		//pdfFileContent = pdfFileContent.replaceAll("\\s","");
		int startIndex = checkCaseIdPattern(pdfFileContent,0,0);
		String newString = "";
		//if(searchString.equalsIgnoreCase("RAYUAN SIVIL NO. ")||searchString.equalsIgnoreCase("CIVIL APPEAL NO. "))			
		 //startIndex = pdfFileContent.indexOf(searchString)+searchString.length();
		int endIndex = checkCaseIdPattern(pdfFileContent,0,1);
		if(startIndex != -1 && endIndex != -1){
			newString = pdfFileContent.substring(startIndex,endIndex);
			if(newString.toLowerCase().indexOf("no")!=-1)
				startIndex =  newString.toLowerCase().indexOf("no");
			return pdfFileContent.substring(startIndex,endIndex);
		}
		return null;
	}

	private int checkCaseIdPattern(String inputString, int startIndex, int option) {
		//String caseidPatternString = "([N][Oo]\\.\\s[W][-]\\d{2}[-]\\d{3,4}[-]\\d{2,4})";
		//String caseidPatternString="([N][Oo][.:][A-Z]?[-]?\\d{1,2}(\\([I][M]\\))?[-]\\d{1,4}[-]\\d{2,4}(\\([A-Z]\\))?)";
		//String caseidPatternString1 = "([R][A][Y][U][A][N]\\s[S][I][V][I][L]\\s[N][Oo][.:]\\s[A-Z]?[-]?\\d{1,2}(\\([I][M]\\))?[-]\\d{1,4}[-]\\d{2,4}(\\([A-Z]\\))?)";
		//String caseidPatternString2 = "([C][I][V][I][L]\\s[A][P][P][E][A][L]\\s[N][Oo][.:]\\s[A-Z]?[-]?\\d{1,2}(\\([I][M]\\))?[-]\\d{1,4}[-]\\d{2,4}(\\([A-Z]\\))?)";
		
		//String caseidPatternString = "(([R][A][Y][U][A][N]\\s[S][I][V][I][L])?([C][I][V][I][L]\\s[A][P][P][E][A][L])?\\s[N][Oo][.:]\\s[A-Z]?[-]?\\d{1,2}(\\([I][M]\\))?[-]\\d{1,4}[-]\\d{2,4}(\\([A-Z]\\))?)";
		String caseidPatternString = "(([R][A][Y][U][A][N]\\s[J][E][N][A][Y][A][H])?([R][A][Y][U][A][N]\\s[S][I][V][I][L])?([C][I][V][I][L]\\s[A][P][P][E][A][L])?\\s[N][Oo][.:]\\s[A-Z]?[-]?\\d{1,2}(\\([I][M]\\))?(\\[[A-Z]\\])?[-]\\d{1,4}[-]\\d{2,4}(\\([A-Z]\\))?)";
		Pattern caseidPattern = Pattern.compile(caseidPatternString);
		Matcher caseidMatcher = caseidPattern.matcher(inputString);
		if(caseidMatcher.find(startIndex) && option == 0)
		{  
			return caseidMatcher.start();
		}
		else if(caseidMatcher.find(startIndex) && option == 1)
		{	
			return caseidMatcher.end();
		}
		else
		{
			return -1;
		}
	}

	@Override
	public void extractParticipants(String CourtName, String pdfFileContent) {
		this.setParticipantsName(CaseDetailsModel.retrieveParticipantsName(extractCNSTCRTParticipants(pdfFileContent)));
	}

	@Override
	public void extractDecisionDate(String CourtName, String pdfFileContent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extractHeardDate(String CourtName, String pdfFileContent) {
		// TODO Auto-generated method stub
		
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
			String searchString = "constitutional court of south africa";
			endIndex1 = pdfFileContent.indexOf(searchString)+searchString.length();
			endIndex = startIndex;
			startIndex = endIndex;
		}
		List<String> nameList = new ArrayList<String>();
		String searchString = "ANTARA";
		String searchString1 = "DAN";
		startIndex = pdfFileContent.indexOf(searchString) + searchString.length();
		
		endIndex1 = pdfFileContent.indexOf(searchString)+searchString.length();

		
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
		// TODO Auto-generated method stub
		
	}

}
