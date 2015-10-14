package courtreferences.model;

public class InternationalCourt {
	/*
	 * Model class for International Court references
	 * Defines search pattern namely Title and Format Patterns for each International Court
	 */	
	
	private int iCourtId;
	private String iCourtName1;
	private String iCourtName2;
	private String iTitlePattern;
	private String iFormatPattern;
	
	public InternationalCourt(){
		
	}
	
	public InternationalCourt(int courtId, String courtName1, String courtName2, String titlePattern, String formatPattern){
		this.setiCourtId(courtId);
		this.setiCourtName1(courtName1);
		this.setiCourtName2(courtName2);
		this.setiFormatPattern(formatPattern);
		this.setiTitlePattern(titlePattern);
	}
	
	public int getiCourtId() {
		return iCourtId;
	}
	public void setiCourtId(int iCourtId) {
		this.iCourtId = iCourtId;
	}
	public String getiCourtName1() {
		return iCourtName1;
	}
	public void setiCourtName1(String iCourtName1) {
		this.iCourtName1 = iCourtName1;
	}
	public String getiCourtName2() {
		return iCourtName2;
	}
	public void setiCourtName2(String iCourtName2) {
		this.iCourtName2 = iCourtName2;
	}
	public String getiTitlePattern() {
		return iTitlePattern;
	}
	public void setiTitlePattern(String iTitlePattern) {
		this.iTitlePattern = iTitlePattern;
	}
	public String getiFormatPattern() {
		return iFormatPattern;
	}
	public void setiFormatPattern(String iFormatPattern) {
		this.iFormatPattern = iFormatPattern;
	}	
}
