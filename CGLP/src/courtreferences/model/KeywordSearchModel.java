package courtreferences.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KeywordSearchModel {
	/*
	 * Model class for Keyboard based searches
	 * Has not been implemented yet. Can be used in future if needed
	 */	
	private static List<String> foreignCourtKeywords;
	private static List<String> internationalCourtKeywords;
	private static List<String> softLawKeywords;
	private static final String foreignCourtKeywordsQuery = "select * from ForeignCourtKeywords";
	private static final String internationalCourtKeywordsQuery = "select * from InternationalCourtKeywords";
	private static final String softLawKeywordsQuery = "select * from SoftLawKeywords";
	
	static{
		setForeignCourtKeywords(retrieveKeywords(foreignCourtKeywordsQuery));
		setInternationalCourtKeywords(retrieveKeywords(internationalCourtKeywordsQuery));
		setSoftLawKeywords(retrieveKeywords(softLawKeywordsQuery));
	}

	private static List<String> retrieveKeywords(String inputQuery) {
		// TODO Auto-generated method stub
		List<String> keywords = new ArrayList<String>();
		ConnectionHandler connHandler = new ConnectionHandler();
		Connection conn = connHandler.getConn();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(inputQuery);
			while(rs.next()){
				String s = rs.getString(1);
				keywords.add(s);				
			}
		}
		catch(SQLException sc){
			System.out.println("SQL Exception : " + sc.getMessage());
		}
		catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
		}
		return keywords;
	}

	public static List<String> getForeignCourtKeywords() {
		return foreignCourtKeywords;
	}

	public static void setForeignCourtKeywords(List<String> foreignCourtKeywords) {
		KeywordSearchModel.foreignCourtKeywords = foreignCourtKeywords;
	}

	public static List<String> getInternationalCourtKeywords() {
		return internationalCourtKeywords;
	}

	public static void setInternationalCourtKeywords(
			List<String> internationalCourtKeywords) {
		KeywordSearchModel.internationalCourtKeywords = internationalCourtKeywords;
	}

	public static List<String> getSoftLawKeywords() {
		return softLawKeywords;
	}

	public static void setSoftLawKeywords(List<String> softLawKeywords) {
		KeywordSearchModel.softLawKeywords = softLawKeywords;
	}
	
}
