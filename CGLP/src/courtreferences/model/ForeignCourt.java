package courtreferences.model;


public class ForeignCourt{
	/*
	 * Model class for ForeignCase objects
	 * Defines search pattern for each Country and Court name combination
	 */
	
	private int countryId;
	private int courtId;
	private String countryName;
	private String courtName;
	private String searchRegex;
	
	public ForeignCourt(){
		
	}
	public ForeignCourt(int countryId, int courtId, String countryName, String courtName, String regex){
		this.setCountryId(countryId);
		this.setCourtId(courtId);
		this.setCountryName(countryName);
		this.setCourtName(courtName);
		this.setSearchRegex(regex);
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
	public String getSearchRegex() {
		return searchRegex;
	}
	public void setSearchRegex(String searchRegex) {
		this.searchRegex = searchRegex;
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
}
