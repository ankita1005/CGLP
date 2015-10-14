package courtreferences.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CitationReferenceModel {
	/* 
	 * This model file contains methods which facilitate db interaction for CaseReference Table  
	 */
	private static ConnectionHandler connHndlr;
	
	public CitationReferenceModel(){
		this.setConnHndlr(null);
	}
	
	public void logOutAction(){
		connHndlr.closeConnection();
	}

	public ConnectionHandler getConnHndlr() {
		return connHndlr;
	}

	public void setConnHndlr(ConnectionHandler connHndlr) {
		CitationReferenceModel.connHndlr = connHndlr;
	}
	
	public static void insertNewCitation(CitationCases citationObj){
		/*	
		 * Inserts new case reference into the CitationReferences table
		 */
		if(CitationReferenceModel.connHndlr == null)
			CitationReferenceModel.connHndlr = new ConnectionHandler();
		Connection conn = CitationReferenceModel.connHndlr.getConn();
				
		try{
			String status = "N";
			String insQuery = "insert into CitationReferences(CaseRefId, RefId, CitationType, CountryId, CountryName, CourtId, CourtName, CaseId, CitationId, PageNumber, OtherDetails,status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insQuery);
			String pageNums = citationObj.getPageNumber();
			String[] pageNumsArray = pageNums.split(",");
			for(int i=0;i<pageNumsArray.length;i++){
				preparedStatement.setInt(1,citationObj.getCaseRefId());
				preparedStatement.setInt(2, citationObj.getCitationRefId());
				preparedStatement.setInt(3, citationObj.getCitationType());
				preparedStatement.setInt(4, citationObj.getCountryId());
				preparedStatement.setString(5, citationObj.getCountryName());
				preparedStatement.setInt(6, citationObj.getCourtId());
				preparedStatement.setString(7, citationObj.getCourtName());
				preparedStatement.setString(8, citationObj.getCaseid());
				preparedStatement.setInt(9, citationObj.getCitationid());
				preparedStatement.setInt(10, Integer.parseInt(pageNumsArray[i]));
				preparedStatement.setString(11, citationObj.getOtherDetails());
				preparedStatement.setString(12, status);
				preparedStatement.executeUpdate();	
			}
					
		}
		catch(SQLException se){
			System.out.println("SQL Exception while inserting new citation : " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return;
	}
	
	public static void updateCitationReference(int caseRefId,int citationRefId, int citationId, int pageNumber, String caseId,String otherDetails){
		/*
		 * Update the records of Citation REferences from JTable update event 
		 */
		
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		try{
			String updtQuery = "update CitationReferences set CitationId = ?, PageNumber = ?, CaseId = ?, otherDetails = ? where CaseRefId = ? and RefId = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(updtQuery);
			preparedStatement.setInt(1,citationId);
			preparedStatement.setInt(2,pageNumber);
			preparedStatement.setString(3, caseId);
			preparedStatement.setString(4, otherDetails);
			preparedStatement.setInt(5, caseRefId);
			preparedStatement.setInt(6, citationRefId);
			preparedStatement.executeUpdate();	
		}
		catch(SQLException se){
			System.out.println("SQL Exception while updating Citation Reference : " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
	}
	
	public static void deleteCitationReference(int citationRefId, int caseRefId){
		/*
		 * Delete the record from CitationReferences table with the input citationRefId and caseRefId
		 */
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();		
		try{
			String delQuery = "delete from CitationReferences where CaseRefId = " + caseRefId + " and RefId = " + citationRefId;
			//System.out.println(delQuery);
			PreparedStatement preparedStatement = conn.prepareStatement(delQuery);
			preparedStatement = conn.prepareStatement(delQuery);
			preparedStatement.executeUpdate();			
		}
		catch(SQLException se){
			System.out.println("SQL Exception while deleting Citation Referece : " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
	}
	
	private static List<CitationCases> selectCitationReferences(String inputQuery){
		/*
		 * Selects the list of records of Citation References based on Query Criteria
		 */
		List<CitationCases> unprocessedRecords = new ArrayList<CitationCases>();
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		//Might cause problem
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(inputQuery);
			while(rs.next()){
				CitationCases cc = new CitationCases();
				cc.setCaseRefId(rs.getInt(1));
				cc.setCitationRefId(rs.getInt(2));
				cc.setCitationType(rs.getInt(3));
				cc.setCountryName(rs.getString(4));
				cc.setCourtName(rs.getString(5));
				cc.setCaseid(rs.getString(6));
				cc.setCitationid(rs.getInt(7));
				cc.setOtherDetails(rs.getString(9));
				cc.setPageNumber(String.valueOf(rs.getInt(8)));
				
				//changed for changes in SouthAfricanPageContent
				if(unprocessedRecords.contains(cc)){
					int index = unprocessedRecords.indexOf(cc);
					CitationCases existingCC = unprocessedRecords.get(index);
					String pageNum = existingCC.getPageNumber();
					pageNum += ","+String.valueOf(rs.getInt(8));
					cc.setPageNumber(pageNum);
				}
								
				unprocessedRecords.add(cc);
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception while selecting Citation Reference : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		return unprocessedRecords;
	}
	
	public static int checkCitationExistance(int caseRefId, int citationId, int pageNo, int countryId, int courtId){
		/*
		 * Checks if the citation reference has already been in the database
		 */
		
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		int numberofRecords = 0;
		try{
			String selQuery = "select count(*) from CitationReferences where CaseRefId = " + caseRefId + " and CitationId = " + citationId + " and PageNumber = " + pageNo + " and CountryId = " + countryId + " and CourtId = " + courtId;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selQuery);
			while(rs.next()){
				numberofRecords = rs.getInt(1);
			}
		}
		catch(SQLException se){
			System.out.println("SQL Exception while checking Citation Existence : " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return numberofRecords;
	}
	
	public static List<CitationCases> selectUnprocessedRecords(){
		/*
		 * Selects the unprocessed records from the CitationReferences Table
		 */
		String unprocessedRecordsQuery = "select CaseRefId, RefId, CitationType, CountryName, CourtName, CaseId, CitationId, PageNumber, otherDetails from CitationReferences where CitationReferences.Status = 'N'";
		return selectCitationReferences(unprocessedRecordsQuery);
	}
	
	public static List<CitationCases> selectRecordsWithRefId(int RefId){
		/*
		 * Selects Citation references for the particular case
		 */
		String selectRecordsWithRefIdQuery = "select CaseRefId, RefId, CitationType, CountryName, CourtName, CaseId, CitationId, PageNumber, otherDetails from CitationReferences where CitationReferences.CaseRefId = " + RefId;
		return selectCitationReferences(selectRecordsWithRefIdQuery);
	}
	
	public static List<CitationCases> searchCaseReferences(int citationType, int CaseRefId){
		/*
		 * Searches citation references based on Search Criteria 
		 */
		String searchQuery = "";
		if(citationType == 0)
			searchQuery = "select CaseRefId, RefId, CitationType, CountryName, CourtName, CaseId, CitationId, PageNumber, otherDetails from CitationReferences where CaseRefId = " + CaseRefId;
		else
			searchQuery = "select CaseRefId, RefId, CitationType, CountryName, CourtName, CaseId, CitationId, PageNumber, otherDetails from CitationReferences where CaseRefId = " + CaseRefId + " and CitationType = " + (citationType - 1);
		return selectCitationReferences(searchQuery);
	}
	
	public static void updateProcessedStatus(int caseRefId, int citationRefId){
		/* 
		 * This method updates the 'Status' field of a record to 'N'
		 * The records with 'Status' field set to 'Y' are not displayed by default
		 */
		//System.out.println("Citation reference Id : " + citationRefId);
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();		
		try{
			String processedStatus = "Y";
			String updtQuery = "update CitationReferences set Status = '" + processedStatus + "' where RefId = " + citationRefId + " and CaseRefId = " + caseRefId;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(updtQuery);
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
	}
}
