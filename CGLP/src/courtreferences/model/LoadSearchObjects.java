package courtreferences.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class LoadSearchObjects {
	
	/*
	 * This class contains all the search objects loaded into memory
	 * These objects are used for searching case references in each document
	 */

	static private List<ForeignCourt> searchObjs = new ArrayList<ForeignCourt>();
	static private List<InternationalCourt> iCSearchObjs = new ArrayList<InternationalCourt>();
	static private List<SoftLaw> sLawSearchObjs = new ArrayList<SoftLaw>();
	static private List<Treaty> treatySearchObjs = new ArrayList<Treaty>();
	static private Set<String> foreignCourtNames = new TreeSet<String>();
	static private Map<String, List<String>> foreignCourtCountryNames = new TreeMap<String, List<String>>();
	
	static {
		/* Loads court reference formats for foreign courts	*/
		loadForeignReferenceFormats();
	}
	
	/* Load the format of the foreign references from the table	*/
	
	private static void loadForeignReferenceFormats(){
		LoadSearchObjects.loadForeignCourtSearchObjects();
		LoadSearchObjects.loadInternationalCourtSearchObjects();
		LoadSearchObjects.loadSoftLawSearchObjects();
		LoadSearchObjects.loadTreatySearchObjects();
		for(ForeignCourt fObj : LoadSearchObjects.getSearchObjs()){
			/*Load the foreign court names in memory using Sets*/
			LoadSearchObjects.getForeignCourtNames().add(fObj.getCourtName().toLowerCase());
			String countryName = fObj.getCountryName().toLowerCase();
			List<String> countryNameList = LoadSearchObjects.getForeignCourtCountryNames().get(fObj.getCourtName().toLowerCase());
			
			if(countryNameList == null){
				List<String> countryList = new ArrayList<String>();
				countryList.add(countryName);
				LoadSearchObjects.getForeignCourtCountryNames().put(fObj.getCourtName().toLowerCase(), countryList);
			}
			else{
				LoadSearchObjects.getForeignCourtCountryNames().get(fObj.getCourtName().toLowerCase()).add(countryName);
			}
		}
		//System.out.println("Total Number of Foreign CourtNames : " + LoadSearchObjects.getForeignCourtNames().size());
		//System.out.println("Done with loading foreign references");
	}
	
	private static void loadForeignCourtSearchObjects(){
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		try{
			/*	Loading Foreign Court formats	*/
			String fetchForeignCourtsQuery = "select CountryTable.CountryId, CourtTable.CourtId, CountryTable.CountryName, CourtTable.CourtName, CourtTable.CitationRegex from CountryDetails CountryTable inner join CourtDetails CourtTable on CountryTable.CountryId = CourtTable.CountryId";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(fetchForeignCourtsQuery);
			while(resultSet.next()){
				ForeignCourt currentobj = new ForeignCourt(resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
				LoadSearchObjects.searchObjs.add(currentobj);
			}
		}
		catch(SQLException sec){
			System.out.println("SQL Exception when loading foreign court references");
		}
		catch(Exception ex){
			System.out.println("SQL Exception when loading foreign court references");
		}
		//System.out.println("Loading " + LoadSearchObjects.getSearchObjs().size() + " Foreign References");
	}
	
	private static void loadInternationalCourtSearchObjects(){
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		
		/*	Loading International Court formats	*/
		try{
			String fetchInternationalCourtsQuery = "select ICourtId, ICourtName1, ICourtName2, ITitlePattern, IFormatPattern from InternationalCourts";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(fetchInternationalCourtsQuery);
			while(resultSet.next()){
				InternationalCourt currentobj = new InternationalCourt(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4), resultSet.getString(5));
				LoadSearchObjects.iCSearchObjs.add(currentobj);
			}
		}
		catch(SQLException sec){
			System.out.println("SQL Exception when loading international court references");
		}
		catch(Exception ex){
			System.out.println("SQL Exception when loading international court references");
		}
		//System.out.println("Loading " + LoadSearchObjects.getiCSearchObjs().size() + " International Court References");
	}
		
	
	private static void loadSoftLawSearchObjects(){
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		try{
			/*	Loading Soft Law formats	*/
			String fetchSoftLawsQuery = "select SLawId, SLawName1, SLawName2, STitlePattern, SFormatPattern from SoftLaws";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(fetchSoftLawsQuery);
			while(resultSet.next()){
				SoftLaw currentobj = new SoftLaw(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4), resultSet.getString(5));
				LoadSearchObjects.sLawSearchObjs.add(currentobj);
			}
		}
		catch(SQLException sec){
			System.out.println("SQL Exception when loading SoftLaw references");
		}
		catch(Exception ex){
			System.out.println("SQL Exception when loading SoftLaw court references");
		}
		//System.out.println("Loading " + LoadSearchObjects.sLawSearchObjs.size() + " Soft Law References");
	}
	
	private static void loadTreatySearchObjects(){
		ConnectionHandler connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		try{
			/*	Loading Treaty formats	*/
			String fetchTreatiesQuery = "select TId, TTitle1_Eng, TTitle2_Eng, TTitle3_Eng, TAbbreviation, TTitle_Fr, TTitle_Ge, TTitle_Sp, TTitle_Po, TDate, TDateType, TTitlePattern, TFormatPattern from Treaties";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(fetchTreatiesQuery);
			while(resultSet.next()){
				Treaty currentobj = new Treaty(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),resultSet.getString(10), resultSet.getString(11), resultSet.getString(12), resultSet.getString(13));
				LoadSearchObjects.treatySearchObjs.add(currentobj);
			}
		}
		catch(SQLException sec){
			System.out.println("SQL Exception when loading Treaty references");
		}
		catch(Exception ex){
			System.out.println("SQL Exception when loading Treaty references");
		}
		//System.out.println("Loading " + LoadSearchObjects.treatySearchObjs.size() + " Treaty References");
	}
	
	public static int getCountryIdByName(String countryName){
		int countryId = -1;
		for(ForeignCourt fObj : LoadSearchObjects.getSearchObjs()){
			if(fObj.getCountryName().equalsIgnoreCase(countryName)){
				countryId = fObj.getCountryId();
				break;
			}
		}
		return countryId;
	}
	
	public static int getCourtIdByName(String courtName){
		int courtId = -1;
		for(ForeignCourt fObj : LoadSearchObjects.getSearchObjs()){
			if(fObj.getCourtName().equalsIgnoreCase(courtName)){//skannan-comment: It had fobj.getCountryName() before changing it to courtName
				courtId = fObj.getCourtId();
				break;
			}
		}
		return courtId;
	}

	public static List<ForeignCourt> getSearchObjs() {
		return searchObjs;
	}

	public static void setSearchObjs(List<ForeignCourt> searchObjs) {
		LoadSearchObjects.searchObjs = searchObjs;
	}

	public static List<InternationalCourt> getiCSearchObjs() {
		return iCSearchObjs;
	}

	public static void setiCSearchObjs(List<InternationalCourt> iCSearchObjs) {
		LoadSearchObjects.iCSearchObjs = iCSearchObjs;
	}

	public static List<SoftLaw> getsLawSearchObjs() {
		return sLawSearchObjs;
	}

	public static void setsLawSearchObjs(List<SoftLaw> sLawSearchObjs) {
		LoadSearchObjects.sLawSearchObjs = sLawSearchObjs;
	}

	public static List<Treaty> getTreatySearchObjs() {
		return treatySearchObjs;
	}

	public static void setTreatySearchObjs(List<Treaty> treatySearchObjs) {
		LoadSearchObjects.treatySearchObjs = treatySearchObjs;
	}

	public static Set<String> getForeignCourtNames() {
		return foreignCourtNames;
	}

	public static void setForeignCourtNames(Set<String> foreignCourtNames) {
		LoadSearchObjects.foreignCourtNames = foreignCourtNames;
	}

	public static Map<String, List<String>> getForeignCourtCountryNames() {
		return foreignCourtCountryNames;
	}

	public static void setForeignCourtCountryNames(
			Map<String, List<String>> foreignCourtCountryNames) {
		LoadSearchObjects.foreignCourtCountryNames = foreignCourtCountryNames;
	}
}
