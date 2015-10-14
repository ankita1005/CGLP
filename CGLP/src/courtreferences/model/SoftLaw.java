package courtreferences.model;

public class SoftLaw {
	/*
	 * Model class for SoftLaws
	 * Defines search pattern namely Title and Format Patterns for each Soft Law
	 */	
	private int sLawId;
	private String sLawName1;
	private String sLawName2;
	private String sTitlePattern;
	private String sFormatPattern;
	
	public SoftLaw(){
		
	}
	
	public SoftLaw(int lawId, String lawName1, String lawName2, String titlePattern, String formatPattern){
		this.setsLawId(lawId);
		this.setsLawName1(lawName1);
		this.setsLawName2(lawName2);
		this.setsTitlePattern(titlePattern);
		this.setsFormatPattern(formatPattern);
	}
	
	public int getsLawId() {
		return sLawId;
	}
	public void setsLawId(int sLawId) {
		this.sLawId = sLawId;
	}
	public String getsLawName1() {
		return sLawName1;
	}
	public void setsLawName1(String sLawName1) {
		this.sLawName1 = sLawName1;
	}
	public String getsLawName2() {
		return sLawName2;
	}
	public void setsLawName2(String sLawName2) {
		this.sLawName2 = sLawName2;
	}
	public String getsTitlePattern() {
		return sTitlePattern;
	}
	public void setsTitlePattern(String sTitlePattern) {
		this.sTitlePattern = sTitlePattern;
	}
	public String getsFormatPattern() {
		return sFormatPattern;
	}
	public void setsFormatPattern(String sFormatPattern) {
		this.sFormatPattern = sFormatPattern;
	}

}
