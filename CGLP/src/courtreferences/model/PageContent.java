package courtreferences.model;

import java.util.List;

public abstract class PageContent {
	
	/*
	 * A court document can have content in footer section and body section
	 * This class holds the data structure for processing content in body of the text
	 */
	protected int pageNo;
	protected String pageContent;
	protected List<CitationCases> refCases;
	

	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public List<CitationCases> getRefCases() {
		return refCases;
	}
	public void setRefCases(List<CitationCases> refCases) {
		this.refCases = refCases;
	}
	
	public abstract void searchForeignReferencesInBodyContent();
}
