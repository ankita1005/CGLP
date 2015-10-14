package courtreferences.model;

import java.sql.Date;
import java.util.List;

public class CourtDocument {


	/*
	 * Base Class to every court documents
	 * In our case, it is the base class to SouthAfricanCourtDocument
	 * Methods in this class needs to be over-ridden in the sub classes
	 */
	protected int caseRefId;
	protected int countryId;
	protected int courtId;
	protected String countryName;
	protected String courtName;
	protected String caseId;
	protected String participantsName;
	protected String decisionDate;
	protected String heardDate;
	protected Date processedDate;
	protected String processedUser;
	protected String status;
	protected List<Citations> citationObjs;
	protected List<PageContent> documentPages;
	protected String sourceFileName;
	protected int duplicateCaseof;
	
	public String getSourceFileName(){
		return this.sourceFileName;
	}
	
	public void setSourceFileName(String sourceFileName){
		this.sourceFileName = sourceFileName;
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

	public String getParticipantsName() {
		return participantsName;
	}

	public void setParticipantsName(String participantsName) {
		this.participantsName = participantsName;
	}

	public String getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}

	public String getHeardDate() {
		return heardDate;
	}

	public void setHeardDate(String heardDate) {
		this.heardDate = heardDate;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
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

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getProcessedUser() {
		return processedUser;
	}

	public void setProcessedUser(String processedUser) {
		this.processedUser = processedUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<Citations> getCitationObjs() {
		return citationObjs;
	}

	public void setCitationObjs(List<Citations> citationObjs) {
		this.citationObjs = citationObjs;
	}
	
	public List<PageContent> getDocumentPages(){
		return this.documentPages;
	}
	
	public void setDocumentPages(List<PageContent> documentPages){
		this.documentPages = documentPages;
	}
	
	public int getDuplicateCaseof() {
		return duplicateCaseof;
	}

	public void setDuplicateCaseof(int duplicateCaseof) {
		this.duplicateCaseof = duplicateCaseof;
	}
	
	public void writeToDb(){
		
	}
}
