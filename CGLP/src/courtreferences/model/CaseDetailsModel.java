package courtreferences.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import courtreferences.southafrica.SouthAfricanCourtDocument;

public class CaseDetailsModel {
	
	/* 
	 * This model file contains methods which facilitate db interaction for CaseDetails Table  
	 */
	
	public static void insertCaseDetails(CourtDocument saf){
		/*
		 * Inserts new case into the CaseDetails Table
		 */
		ConnectionHandler connHandler = new ConnectionHandler();
		String insertCaseDetailsQuery = "insert into CaseDetails(CountryId, CourtId, CaseId, ParticipantsName, DecisionDate, HeardDate, ProcessedDate, ProcessedUser, Status, SourceFileName, DuplicateofCase) values(?,?,?,?,?,?,?,?,?,?,?)";
		String dummy = "insert into CaseDetails(CountryId, CourtId, CaseId, ParticipantsName, DecisionDate, HeardDate, ProcessedDate, ProcessedUser, Status, SourceFileName, DuplicateofCase) values(";
		String selectCaseIdQuery = "select max(CaseRefId) from CaseDetails";
		try {
			PreparedStatement prep = connHandler.getConnection().prepareStatement(insertCaseDetailsQuery);
			prep.setInt(1, saf.getCountryId());
			dummy+=saf.getCountryId()+",";
			prep.setInt(2, saf.getCourtId());
			dummy+=saf.getCourtId()+",";
			prep.setString(3, saf.getCaseId());
			//System.out.println("Caseid="+saf.getCaseId());
			dummy+=saf.getCaseId()+",";
			if(saf.getParticipantsName()!=null)
			{
				prep.setString(4, saf.getParticipantsName().replaceAll("\r\n", " ").trim());
				dummy+=saf.getParticipantsName().replaceAll("\r\n", " ").trim()+",";
			}
			else{
				prep.setString(4, saf.getParticipantsName());
				dummy+=saf.getParticipantsName()+",";
			}
			prep.setString(5, saf.getDecisionDate());
			dummy+=saf.getDecisionDate()+",";
			prep.setString(6, saf.getHeardDate());
			dummy+=saf.getHeardDate()+",";
			prep.setDate(7, saf.getProcessedDate());
			dummy+=saf.getProcessedDate()+",";
			prep.setString(8, saf.getProcessedUser());
			dummy+=saf.getProcessedUser()+",";
			prep.setString(9, saf.getStatus());
			dummy+=saf.getStatus()+",";
			prep.setString(10, saf.getSourceFileName());
			dummy+=saf.getSourceFileName()+",";
			prep.setInt(11, saf.getDuplicateCaseof());
			dummy+=saf.getDuplicateCaseof()+",";
			//System.out.println(dummy);
			prep.executeUpdate();
			Statement st = connHandler.getConnection().createStatement();
			ResultSet rs = st.executeQuery(selectCaseIdQuery);
			if(rs.next())
				saf.setCaseRefId(rs.getInt(1));				
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while writing Doc to DB : "+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("Exception while writing Doc to DB : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String retrieveParticipantsName(List<String> participantsName){
		/*
		 * Converts multi-line participants name into a single line String
		 */
		StringBuffer participantsNameBuffer = new StringBuffer();
		for(String participant : participantsName){
			participantsNameBuffer.append(participant);
			participantsNameBuffer.append("\t");
		}
		return participantsNameBuffer.toString();
	}
	
	public static int retrieveCountryID(String countryName){
		/*
		 * Gets CountryName as input and finds corresponding CountryId
		 */
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		String countryIdQuery = "select CountryId from CountryDetails where CountryName = '" + countryName + "'";
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(countryIdQuery);
			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		return -1;
	}
	
	public static int retrieveCourtID(int countryId, String courtName){
		/*
		 * Gets the CountryId and CourtName as input and finds the corresponding court id
		 */
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		String courtIdQuery = "select CourtId from CourtDetails where CourtName = '" + courtName + "' and CountryId = " + countryId;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(courtIdQuery);
			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		return -1;
	}
	
	
	private static List<CourtDocument> selectCases(String inputQuery){
		/*
		 * Extracts cases matching constraints specified in inputQuery
		 * Output is the list of CaseDetails which is of type CourtDocument
		 * NOTE : CourtDocument should always be the super class of any specific Country Document
		 */
		List<CourtDocument> unprocessedRecords = new ArrayList<CourtDocument>();
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(inputQuery);
			while(rs.next()){
				CourtDocument hc = new CourtDocument();
				hc.setCaseRefId(rs.getInt(1));
				hc.setCountryName(rs.getString(2));
				hc.setCourtId(rs.getInt(3));
				hc.setCourtName(rs.getString(4));
				hc.setCaseId(rs.getString(5));
				hc.setParticipantsName(rs.getString(6));
				hc.setDecisionDate(rs.getString(7));
				hc.setHeardDate(rs.getString(8));
				hc.setProcessedDate(rs.getDate(9));
				hc.setProcessedUser(rs.getString(10));
				hc.setSourceFileName(rs.getString(11));
				unprocessedRecords.add(hc);
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		return unprocessedRecords;
	}
	
	public static List<CourtDocument> selectUnprocessedRecords(){
		/*
		 * This method fetches the records which were not processed and confirmed by the user
		 * Records in the CaseDetails table with 'Status' field set to 'N'
		 * 'N' - specifies unprocessed records
		 * 'Y' - specifies processed records
		 */
		/*Ashmit-update
		 * include courtid in the output result
		 * */
		String unprocessedRecordsQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtId,CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CaseDetails.Status = 'N'";
		return selectCases(unprocessedRecordsQuery);
	}
	
	public static void updateCaseDetails(int caseRefId,String caseId,String participantsName, String decisionDate, String heardDate){
		/*
		 * This method gets the user input for the existing case in the database and updates in the table
		 */
		
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();		
		try{
			String updtQuery = "update CaseDetails set CaseId = ?, ParticipantsName = ?, DecisionDate = ?,HeardDate = ? where CaseRefId = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(updtQuery);
			preparedStatement.setString(1,caseId);
			preparedStatement.setString(2,participantsName);
			preparedStatement.setString(3, decisionDate);
			preparedStatement.setString(4, heardDate);
			preparedStatement.setInt(5, caseRefId);
			preparedStatement.executeUpdate();	
			//System.out.println("Values updated to " + participantsName);
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
	}
	
	public static void updateProcessedStatus(int CaseRefId){
		/* 
		 * This method updates the 'Status' field of a record to 'N'
		 * The records with 'Status' field set to 'Y' are not displayed by default
		 */
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();		
		try{
			String processedStatus = "Y";
			String updtQuery = "update CaseDetails set Status = '" + processedStatus + "' where CaseRefId = " + CaseRefId;
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
	
	
	public static int deleteCaseDetails(int caseRefId){
		/*
		 * Deletes the particular case from the CaseDetails table
		 */
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();		
		try{
			String delQuery = "delete from CaseDetails where CaseRefId = " + caseRefId;
			PreparedStatement preparedStatement = conn.prepareStatement(delQuery);
			preparedStatement = conn.prepareStatement(delQuery);
			preparedStatement.executeUpdate();			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
			return 0; 
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
			return -1;
		}
		return 1;
	}
	
	public static List<CourtDocument> selectCaseDetails(int searchOption, String searchTerm){
		/*
		 * Returns list of cases depending on the Search Criteria
		 */
		
		String searchQuery = "";
		if(searchOption == 0){
				searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CAST(CaseDetails.CaseId as BINARY) like '%" + searchTerm.toLowerCase() + "%'";
		}
		else if(searchOption == 1){
			String searchTerms[] = searchTerm.split("\t");
			searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CountryDetails.CountryName = '" + searchTerms[0] + "' and CourtDetails.CourtName = '" + searchTerms[1] + "'";
		}
		else if(searchOption == 2){
			searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where  CAST(CaseDetails.ParticipantsName as BINARY) like '%" + searchTerm.toLowerCase() + "%'";
		}	
		else if(searchOption == 3)
			searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CaseDetails.ProcessedDate like '" + searchTerm.toLowerCase() + "'";
		else if(searchOption == 4){
				searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CaseDetails.CaseRefId = " + Integer.parseInt(searchTerm);
		}
		else if(searchOption == 5)
			searchQuery = "select CaseDetails.CaseRefId, CountryDetails.CountryName, CourtDetails.CourtName, CaseDetails.CaseId, CaseDetails.ParticipantsName, CaseDetails.DecisionDate, CaseDetails.HeardDate, CaseDetails.ProcessedDate, CaseDetails.ProcessedUser, CaseDetails.SourceFileName from CaseDetails inner join CountryDetails on CaseDetails.CountryId = CountryDetails.CountryId inner join CourtDetails on CourtDetails.CourtId = CaseDetails.CourtId where CAST(CaseDetails.ProcessedUser as BINARY) = '" + searchTerm.toLowerCase() + "'";
		return selectCases(searchQuery);
	}
	
	public static int checkForDuplicates(int countryId, int courtId, String caseId){
		/*
		 * Checks whether there is already a case with same countryid, courtid and caseid
		 */
		String searchQuery = "select CaseRefId,DuplicateofCase from CaseDetails where CaseDetails.CourtId = " + courtId + " and CaseDetails.CountryId = " + countryId + " and CaseDetails.CaseId = '" + caseId + "'";
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		int dcaseId = 0;
		int ddupRefId = 0;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(searchQuery);
			if(rs.next()){
				dcaseId = rs.getInt(1);
				ddupRefId = rs.getInt(2);
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		if(ddupRefId != -1)
			return ddupRefId;
		else if(dcaseId != 0)
			return dcaseId;
		
		return -1;
	}
}
