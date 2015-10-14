package courtreferences.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class LoginAuthenticator {
	/*
	 * LoginDetails ModelFile
	 * Contains methods and functionalities representing LoginDetails
	 * Has functionalities for verifying the login credential and authenticating valid users 
	 */
	
	private int userstatus;
	private String username;
	private ConnectionHandler connHndlr;
	private Statement stmt = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public LoginAuthenticator(){
		
	}
	
	public LoginAuthenticator(String username){
		this.setUsername(username);
		this.setUserStatus(-1);
		this.setConnHndlr(null);
	}
	
	public int getUserStatus(){
		return this.userstatus;
	}
	
	public void setUserStatus(int status){
		this.userstatus = status;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void logOutAction(){
		this.connHndlr.closeConnection();
	}

	public ConnectionHandler getConnHndlr() {
		return connHndlr;
	}

	public void setConnHndlr(ConnectionHandler connHndlr) {
		this.connHndlr = connHndlr;
	}
	
	/* Gets the username and password and checks the validity of the user
	 * If the user is in the table returns a value based on his role
	 */
	
	public int verifyAuthentication(String uname,String pswd){
		Statement stmt = null;
		if(this.getConnHndlr() == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = connHndlr.getConnection();
		uname = uname.toLowerCase();
		this.setUsername(uname);
		String vfnQuery = "select l.user_role from LoginDetails l where l.username = '" + uname + "' AND l.password = '" + pswd + "'";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(vfnQuery);
			if(!rs.next()){
				System.out.println("User doesnt exist");
				this.setUserStatus(-1);
			}
			else{
				String userrole = rs.getString(1);
				if(userrole.equals("admin")){
					this.setUserStatus(0);
				}
				else{
					this.setUserStatus(1);
				}
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		finally{
		}
		return this.getUserStatus(); 
	}
	
/*	Inserts new user to the System	*/
	
	public int insertNewUser(String uname,String pswd,String role){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConn();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(!this.resultSet.next()){
				String insQuery = "insert into LoginDetails(username,password,user_role) values(?,?,?)";
				this.preparedStatement = conn.prepareStatement(insQuery);
				this.preparedStatement.setString(1,uname);
				this.preparedStatement.setString(2,pswd);
				this.preparedStatement.setString(3,role);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
	
	/* Removes existing user from the system	*/
	
	public int deleteExistingUser(String uname){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConnection();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(this.resultSet.next()){
				String delQuery = "delete from LoginDetails where username = '" + uname + "'" ;
				this.preparedStatement = conn.prepareStatement(delQuery);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
	
	/*	Update the password of existing user in the system	*/
	
	public int updatePassword(String uname,String pswd,String newpaswd){
		if(this.connHndlr == null)
			this.connHndlr = new ConnectionHandler();
		Connection conn = this.connHndlr.getConnection();
		int returnvalue = 0;
		
		uname = uname.toLowerCase();		
		try{
			String vfnQuery = "select * from LoginDetails l where l.username = '" + uname + "' AND l.password = '" + pswd + "'";
			this.stmt = conn.createStatement();
			this.resultSet = this.stmt.executeQuery(vfnQuery);
			if(this.resultSet.next()){
				String updtQuery = "update LoginDetails set password = ? where username = ?";
				this.preparedStatement = conn.prepareStatement(updtQuery);
				this.preparedStatement.setString(1,pswd);
				this.preparedStatement.setString(2,uname);
				this.preparedStatement.executeUpdate();
				returnvalue = 1;
			}
			else{
				returnvalue = 0;
			}			
		}
		catch(SQLException se){
			System.out.println("SQL Exception " + se.getMessage());
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
		}
		return returnvalue;
	}
}
