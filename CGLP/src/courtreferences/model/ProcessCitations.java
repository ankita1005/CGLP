package courtreferences.model;

import java.util.List;

public interface ProcessCitations {
	/*
	 * Defines the basic methods which are necessary to process citations in any document
	 */
	abstract public int extractCaseId(String CourtName,String pdfFileContent);
	abstract public void extractParticipants(String CourtName,String pdfFileContent);
	abstract public void extractDecisionDate(String CourtName,String pdfFileContent);
	abstract public void extractHeardDate(String CourtName,String pdfFileContent);
	abstract public void extractCitations(String CourtName,List<String> fileContent);
}
