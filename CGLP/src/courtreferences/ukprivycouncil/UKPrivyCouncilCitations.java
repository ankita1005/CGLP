package courtreferences.ukprivycouncil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import courtreferences.model.CitationCases;
import courtreferences.model.Citations;
import courtreferences.model.ForeignCourt;
import courtreferences.model.InternationalCourt;
import courtreferences.model.LoadSearchObjects;
import courtreferences.model.SoftLaw;
import courtreferences.model.Treaty;

public class UKPrivyCouncilCitations extends Citations{
	/*
	 * Contains attributes from Citations class from where it is extended
	 * Contains methods to search foreign citations, international references and treaties
	 * It has to implement methods in Search interface since it has extracted the abstract method which implemented the Search interface
	 */

	public UKPrivyCouncilCitations() {
		// TODO Auto-generated constructor stub
	}
	
	public UKPrivyCouncilCitations(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, int pageNo){
		/* initialize the citation object with the values passed to the function	*/
		initializeValues(caseid, countryName, courtName, pdfcitationid, citationString, pageNo);
	}
	
	/* Calls respective functions to extract the citation content from the documents	*/
	
	private void initializeValues(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, int pageNo){
		this.setCaseId(caseid);
		this.setCountryname(countryName);
		this.setCourtname(courtName);
		this.setCitationid(pdfcitationid);
		this.setCitationString(citationString);
		this.setPageNo(pageNo);
		this.refCases = new ArrayList<CitationCases>();
		this.searchForeignReferences();
	}
	
	/* Searches the occurrence of all the foreign references in each and every citation	*/
	
	public void searchForeignReferences() {
		// TODO Auto-generated method stub
		for(ForeignCourt fObj : LoadSearchObjects.getSearchObjs()){
			if(fObj.getSearchRegex() != null){
				this.searchForeignCourtReferences(fObj);
			}
		}
		//System.out.println("Searching international laws");
		for(InternationalCourt iCObj : LoadSearchObjects.getiCSearchObjs()){
			if(iCObj.getiFormatPattern() != null || iCObj.getiTitlePattern() != null)
				this.searchInternationalCourtReferences(iCObj);
		}
		for(SoftLaw sLawObj : LoadSearchObjects.getsLawSearchObjs()){
			if(sLawObj.getsTitlePattern() != null || sLawObj.getsTitlePattern() != null)
				this.searchSoftLaws(sLawObj);
		}
		for(Treaty tObj : LoadSearchObjects.getTreatySearchObjs()){
			if(tObj.gettTitlePattern() != null || tObj.gettFormatPattern() != null)
				this.searchTreaties(tObj);
		}
		return;
	}
	
	
	
	
	/* Gets the foreign reference object as input which contain country name, court name and corresponding format which will be searched
	 * Uses the citation string and the body string to find out whether any case has been cited and adds the cited case to RefCases list
	 */
	
	public void searchForeignCourtReferences(ForeignCourt fObj){
		String citationPatternString = fObj.getSearchRegex();
		Pattern citationPattern = Pattern.compile(citationPatternString);
		Matcher citationMatcher = citationPattern.matcher(this.getCitationString());
		while(citationMatcher.find()){
			int startIndex = citationMatcher.start();
			int endIndex = citationMatcher.end();
			int refid = CitationCases.getRefid();
			int CountryId = fObj.getCountryId();
			int CitationType = 1; //skannan-comment: changed citation type for Professor's ease
			CitationCases.setRefid(refid+1);
			String refString = formRefString(this.getCitationString(), startIndex, endIndex);
			String caseDetails = this.getCitationString().substring(startIndex,endIndex);
			CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, fObj.getCourtId(), fObj.getCountryName(), fObj.getCourtName(), caseDetails, this.getCitationid(), String.valueOf(this.getPageNo()), refString);
			this.getRefCases().add(currentCitation);
		}
		return;
	}
	
	/* Searches for the title of the cases in the citation	*/
	public void searchTitleOfForeignCourtReference(){
		this.setCitationCaseTitles(new ArrayList<String>());
		
		/*	Searches for foreign court titles within the citation string	*/
		for(String title : extractTitleOfForeignCourtReference(this.getCitationString())){
			this.getCitationCaseTitles().add(title);
		}
		
		
		/*	Searches for foreign court titles from citation body string	
		for(String title : extractTitleOfForeignCourtReference(this.getCitationBodyString())){
			this.getCitationCaseTitles().add(title);
		}
		*/
	}
	
	/*	Searches for the case titles from the input String */
	
	private List<String> extractTitleOfForeignCourtReference(String citationString){
		String csubString = null;
		List<String> outputList = new ArrayList<String>();
		String caseTitlePattern = "([A-Z][^0-9^\\s]*\\s)+v ([A-Z][^0-9]+\\s)+";
		Pattern citationPattern = Pattern.compile(caseTitlePattern);
		while(true){
			Matcher citationMatcher = citationPattern.matcher(citationString);
			if(citationMatcher.find()){
				int startIndex = citationMatcher.start();
				int middleIndex = citationString.indexOf(" v ", startIndex);
				int endIndex = citationMatcher.end();
				if(middleIndex != -1 && startIndex == -1 && endIndex == -1){
					csubString = citationString.substring(middleIndex - 50, middleIndex + 50);
					outputList.add(csubString);
					break;
				}
				if(middleIndex - startIndex > 70)
					startIndex = middleIndex - 70;
				if(endIndex - middleIndex > 70)
					endIndex = middleIndex + 70;
				csubString = citationString.substring(startIndex, endIndex);
				int semiIndex = citationString.indexOf(";", middleIndex);
				if(semiIndex >= 0 && semiIndex < csubString.length()){
					csubString = csubString.substring(0,semiIndex);
					endIndex = semiIndex;
				}
				outputList.add(csubString);
				citationString = citationString.substring(endIndex);
			}
			else{
				if(citationString.indexOf(" v ") != -1){
					int middleIndex = citationString.indexOf(" v ");
					int startIndex = 0;
					int endIndex = citationString.length();
					if(startIndex + 50 < middleIndex)
						startIndex = middleIndex - 50;
					if(endIndex - 50 > middleIndex)
						endIndex = middleIndex + 50;
					csubString = citationString.substring(startIndex, endIndex);
					outputList.add(csubString);
				}
				break;
			}
		}
		return outputList;
	}

	//@Override
	public void searchInternationalCourtReferences(InternationalCourt iCObj) {
		// TODO Auto-generated method stub
		String titlePatternString = iCObj.getiTitlePattern();
		if(titlePatternString != null){
			String refString = this.searchEntities(titlePatternString);
			if(refString != null){
				int refid = CitationCases.getRefid();
				int CountryId = -1;
				int CitationType = 2; //skannan-comment: changed citation type for Professor
				CitationCases.setRefid(refid+1);
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, iCObj.getiCourtId(), "IC", iCObj.getiCourtName1(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = iCObj.getiFormatPattern();
		if(formatPatternString != null){
			String refString = this.searchEntities(formatPatternString);
			if(refString != null){

				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 2; //skannan-comment: changed citation type for Professor
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, iCObj.getiCourtId(), "IC", iCObj.getiCourtName1(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}

	@Override
	public void searchSoftLaws(SoftLaw sLawObj) {
		// TODO Auto-generated method stub
		//System.out.println("Searching SoftLaws");
		String titlePatternString = sLawObj.getsTitlePattern();
		if(titlePatternString != null){
			String refString = this.searchEntities(titlePatternString);
			if(refString != null){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 4; //skannan-comment: changed citation type for Professor
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, sLawObj.getsLawId(), "SL", sLawObj.getsLawName1(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = sLawObj.getsFormatPattern();
		if(formatPatternString != null){
			String refString = this.searchEntities(formatPatternString);
			if(refString != null){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 4; //skannan-comment: changed citation type for Professor
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, sLawObj.getsLawId(), "SL", sLawObj.getsLawName1(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}

	//@Override
	public void searchTreaties(Treaty tObj) {
		// TODO Auto-generated method stub
		String titlePatternString = tObj.gettTitlePattern();
		if(titlePatternString != null){
			String refString = this.searchEntities(titlePatternString);
			if(refString != null){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 3; //skannan-comment: changed citation type for Professor
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, tObj.gettId(), "TR", tObj.gettTitle1_Eng(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
		String formatPatternString = tObj.gettFormatPattern();
		if(formatPatternString != null){
			String refString = this.searchEntities(formatPatternString);
			if(refString != null){
				int refid = CitationCases.getRefid();
				CitationCases.setRefid(refid+1);
				int CountryId = -1;
				int CitationType = 3;
				CitationCases currentCitation = new CitationCases(refid, CountryId, CitationType, tObj.gettId(), "TR", tObj.gettTitle1_Eng(), refString, this.getCitationid(), String.valueOf(this.getPageNo()), this.getCitationString());
				this.getRefCases().add(currentCitation);
				return;
			}
		}
	}
	
	public String searchEntities(String patternString){
		Pattern titlePattern = Pattern.compile(patternString);
		Matcher titleMatcher = titlePattern.matcher(this.getCitationString());
		String refString = null;
		if(titleMatcher.find()){
			int startIndex = titleMatcher.start();
			int endIndex = titleMatcher.end();
			refString = this.getCitationString().substring(startIndex,endIndex);
		}
		return refString;
	}
	
	private String formRefString(String inputString, int startIndex, int endIndex){
		int boundarylength = 400;
		startIndex = startIndex - boundarylength >=0 ? startIndex - boundarylength : 0;
		endIndex = endIndex + boundarylength < inputString.length() ? endIndex + boundarylength : inputString.length();
		return inputString.substring(startIndex,endIndex);
	}
}
