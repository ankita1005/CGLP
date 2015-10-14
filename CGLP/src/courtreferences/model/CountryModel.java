package courtreferences.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class CountryModel {
	/*
	 * Country and Court Details model file
	 * This class has functionalities to accesses the Country and Court data models to get those data to be loaded into the view combo boxes
	 */
	
	private Statement stmt = null;
	private ResultSet resultSet = null;
	private ConnectionHandler connHndlr = null;
	
	public CountryModel(){
		
	}	
	
	/* 
	 * Fetches the names of the country in the database and returns to the view	
	 */
	
	public Vector<String> getCountryValues(){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		this.stmt = null;
		Connection conn = this.connHndlr.getConnection();
		Vector<String> countryNames = new Vector<String>();
		this.resultSet = null;
		String getCntryNamesQuery = "select CountryName from CountryDetails";
		try{
			stmt = conn.createStatement();
			this.resultSet = stmt.executeQuery(getCntryNamesQuery);
			while(this.resultSet.next()){
			    String cntryname = resultSet.getString("CountryName");
			    countryNames.add(cntryname);
			}
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return countryNames;
	}
	
	/* 
	 * Fetches the names of the court based on the country name given as input	and returns to the view 
	 */ 
	
	public Vector<String> getCourtValues(String countryname){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Statement stmt = null;
		Statement stmt1 = null;
		Connection conn = this.connHndlr.getConnection();
		Vector<String> courtNames = new Vector<String>();
		int cntryid = 0;
		String getCntryIdQuery = "select CountryId from CountryDetails where CountryName ='" + countryname + "'";
		try{
			stmt = conn.createStatement();
			this.resultSet = stmt.executeQuery(getCntryIdQuery);
			if(this.resultSet.next()){
			    cntryid = this.resultSet.getInt("CountryId");
			    String getCourtDetailsQuery = "select CourtName from CourtDetails where CountryId = " + cntryid;
			    stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(getCourtDetailsQuery);
				while(rs1.next()){
				    String courtname = rs1.getString("CourtName");
				    courtNames.add(courtname);
				}
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return courtNames;
	}
}
