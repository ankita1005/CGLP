package courtreferences.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Citations{
	/*
	 * Abstract class which defines the overall structure of each citation object that needs to be extracted from the pdf content and loaded into the database
	 * Implements search interface which defines methods to extract Title information and citation information from each pdf document
	 * The classes which are extended from this class should have give implementation to those methods
	 */
	
	protected int citationid;
	protected String caseId;
	protected String countryname;
	protected String courtname;
	protected String citationString;
	protected int pageNo;
	protected List<String> citationCaseTitles;
	protected List<CitationCases> refCases;
	
	public Citations(){		
	}
	
	public Citations(String caseid, String countryName, String courtName, int pdfcitationid, String citationString, String citationBodyString, int pageNo){
	}
	
	/* Needs to be implemented in the class which is extending it	*/
	public abstract void searchForeignReferences();
	public abstract void searchForeignCourtReferences(ForeignCourt fObj);
	public abstract void searchInternationalCourtReferences(InternationalCourt iCObj);
	public abstract void searchSoftLaws(SoftLaw sLawObj);
	public abstract void searchTreaties(Treaty tObj);

	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCourtname() {
		return courtname;
	}
	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}
	public String getCitationString() {
		return citationString;
	}
	public void setCitationString(String citationString) {
		this.citationString = citationString;
	}
	public List<CitationCases> getRefCases() {
		return refCases;
	}
	public void setRefCases(ArrayList<CitationCases> refCases) {
		this.refCases = refCases;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getCitationid() {
		return citationid;
	}
	public void setCitationid(int citationid) {
		this.citationid = citationid;
	}
	public List<String> getCitationCaseTitles() {
		return citationCaseTitles;
	}
	public void setCitationCaseTitles(List<String> citationCaseTitles) {
		this.citationCaseTitles = citationCaseTitles;
	}
}
