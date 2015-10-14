package courtreferences.model;

public class Treaty {
	/*
	 * Model class for Treaty
	 * Defines search pattern namely Title and Format Patterns for each Treaty
	 */	
	private int tId;
	private String tTitle1_Eng;
	private String tTitle2_Eng;
	private String tTitle3_Eng;
	private String tAbbreviation;
	private String tTitle_Fr;
	private String tTitle_Ge;
	private String tTitle_Sp;
	private String tTitle_Po;
	private String tDate;
	private String tDateType;
	private String tTitlePattern;
	private String tFormatPattern;
	
	public Treaty(){
		
	}
	
	public Treaty(int Id, String title1_Eng, String title2_Eng, String title3_Eng, String abbreviation, String title_Fr, String title_Ge, String title_Sp, String title_Po, String date, String dateType, String titlePattern, String formatPattern){
		this.settId(Id);
		this.settTitle1_Eng(title1_Eng);
		this.settTitle2_Eng(title2_Eng);
		this.settTitle3_Eng(title3_Eng);
		this.settAbbreviation(abbreviation);
		this.settTitle_Fr(title_Fr);
		this.settTitle_Ge(title_Ge);
		this.settTitle_Po(title_Po);
		this.settTitle_Sp(title_Sp);
		this.settDate(date);
		this.settDateType(dateType);
		this.settTitlePattern(titlePattern);
		this.settFormatPattern(formatPattern);
	}
	
	public int gettId() {
		return tId;
	}
	public void settId(int tId) {
		this.tId = tId;
	}
	public String gettTitle1_Eng() {
		return tTitle1_Eng;
	}
	public void settTitle1_Eng(String tTitle1_Eng) {
		this.tTitle1_Eng = tTitle1_Eng;
	}
	public String gettTitle2_Eng() {
		return tTitle2_Eng;
	}
	public void settTitle2_Eng(String tTitle2_Eng) {
		this.tTitle2_Eng = tTitle2_Eng;
	}
	public String gettTitle3_Eng() {
		return tTitle3_Eng;
	}
	public void settTitle3_Eng(String tTitle3_Eng) {
		this.tTitle3_Eng = tTitle3_Eng;
	}
	public String gettAbbreviation() {
		return tAbbreviation;
	}
	public void settAbbreviation(String tAbbreviation) {
		this.tAbbreviation = tAbbreviation;
	}
	public String gettTitle_Fr() {
		return tTitle_Fr;
	}
	public void settTitle_Fr(String tTitle_Fr) {
		this.tTitle_Fr = tTitle_Fr;
	}
	public String gettTitle_Ge() {
		return tTitle_Ge;
	}
	public void settTitle_Ge(String tTitle_Ge) {
		this.tTitle_Ge = tTitle_Ge;
	}
	public String gettTitle_Sp() {
		return tTitle_Sp;
	}
	public void settTitle_Sp(String tTitle_Sp) {
		this.tTitle_Sp = tTitle_Sp;
	}
	public String gettTitle_Po() {
		return tTitle_Po;
	}
	public void settTitle_Po(String tTitle_Po) {
		this.tTitle_Po = tTitle_Po;
	}
	public String gettDate() {
		return tDate;
	}
	public void settDate(String tDate) {
		this.tDate = tDate;
	}
	public String gettDateType() {
		return tDateType;
	}
	public void settDateType(String tDateType) {
		this.tDateType = tDateType;
	}
	public String gettTitlePattern() {
		return tTitlePattern;
	}
	public void settTitlePattern(String tTitlePattern) {
		this.tTitlePattern = tTitlePattern;
	}
	public String gettFormatPattern() {
		return tFormatPattern;
	}
	public void settFormatPattern(String tFormatPattern) {
		this.tFormatPattern = tFormatPattern;
	}
}
