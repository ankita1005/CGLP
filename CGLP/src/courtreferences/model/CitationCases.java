package courtreferences.model;

public class CitationCases {
	/*
	 * There may be more than one case cited in each citations
	 * This class describes the basic structure of each case such as country and court to which the case belongs to and also the citation id where this case is referred
	 */
	
	private static int refid = 1;
	private int caseRefId;
	private int citationRefId;
	private int citationType;
	private int countryId;
	private int courtId;
	private String countryName;
	private String courtName;
	private String caseid;
	private String caseTitle;
	private int citationid;
	private String pageNumber;
	private String otherDetails;
	
	public CitationCases(){
		
	}
	
	public CitationCases(int citationRefId, int countryId, int citationType, int courtId, String countryName, String courtName, String caseid, int citationid, String pageNumber, String otherDetails){
		this.setCountryId(countryId);
		this.setCourtId(courtId);
		this.setCitationType(citationType);
		this.setCitationRefId(citationRefId);
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.setCaseid(caseid);
		this.setCitationid(citationid);
		this.setPageNumber(pageNumber);
		this.setOtherDetails(otherDetails);
	}
	
	public String getCaseid() {
		return caseid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public int getCitationid() {
		return citationid;
	}
	public void setCitationid(int citationid) {
		this.citationid = citationid;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public static int getRefid() {
		return refid;
	}

	public static void setRefid(int refid) {
		CitationCases.refid = refid;
	}

	public int getCaseRefId() {
		return caseRefId;
	}

	public void setCaseRefId(int caseRefId) {
		this.caseRefId = caseRefId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getCourtId() {
		return courtId;
	}

	public void setCourtId(int courtId) {
		this.courtId = courtId;
	}

	public int getCitationRefId() {
		return citationRefId;
	}

	public void setCitationRefId(int citationRefId) {
		this.citationRefId = citationRefId;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public int getCitationType() {
		return citationType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + caseRefId;
		result = prime * result
				+ ((caseTitle == null) ? 0 : caseTitle.hashCode());
		result = prime * result + ((caseid == null) ? 0 : caseid.hashCode());
		result = prime * result + citationRefId;
		result = prime * result + citationType;
		result = prime * result + citationid;
		result = prime * result + countryId;
		result = prime * result
				+ ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result + courtId;
		result = prime * result
				+ ((courtName == null) ? 0 : courtName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CitationCases other = (CitationCases) obj;
		if (caseRefId != other.caseRefId)
			return false;
		if (caseTitle == null) {
			if (other.caseTitle != null)
				return false;
		} else if (!caseTitle.equals(other.caseTitle))
			return false;
		if (caseid == null) {
			if (other.caseid != null)
				return false;
		} else if (!caseid.equals(other.caseid))
			return false;
		if (citationRefId != other.citationRefId)
			return false;
		if (citationType != other.citationType)
			return false;
		if (citationid != other.citationid)
			return false;
		if (countryId != other.countryId)
			return false;
		if (countryName == null) {
			if (other.countryName != null)
				return false;
		} else if (!countryName.equals(other.countryName))
			return false;
		if (courtId != other.courtId)
			return false;
		if (courtName == null) {
			if (other.courtName != null)
				return false;
		} else if (!courtName.equals(other.courtName))
			return false;
		return true;
	}

	public void setCitationType(int citationType) {
		this.citationType = citationType;
	}
}
