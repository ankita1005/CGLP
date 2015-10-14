package courtreferences.model;

/* 
 * Contains functionalities to establish and terminate connection with mysql database
 * 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ConnectionHandler {
	private static Connection conn = null;
	public static String dbconfigfile = null;
	
	static{
		ConnectionHandler.dbconfigfile = System.getProperty("user.home");
		String unixfilepath = ConnectionHandler.dbconfigfile + "/" + "dbconfig.txt";
		File unixTestFile = new File(unixfilepath);
		if(unixTestFile.isFile())
			ConnectionHandler.dbconfigfile = unixfilepath;
		System.out.println("DBConFig File inside static: " + ConnectionHandler.dbconfigfile);
	}
	public ConnectionHandler(){
	}
	
	public Map<String,String> getDefaultDbConfigParameters(){
		Scanner sc = null;
		Map<String,String> configMap = null;
		try{
			sc = new Scanner(new File(ConnectionHandler.dbconfigfile));
			configMap = new HashMap<String,String>();
			configMap.put("hostname", sc.nextLine());
			configMap.put("port", sc.nextLine());
			configMap.put("dbname", sc.nextLine());
			configMap.put("username", sc.nextLine());
			configMap.put("password", sc.nextLine());
			
		}	
		catch(Exception e){
			System.out.println("Exception: ConnHndlr " + e.getMessage());
		}
		finally{
			sc.close();			
		}
		return configMap;
	}
	
	public int updateDBSettings(String hostname, String port, String dbname, String username, String password){
		FileWriter fstream;
		try {
			fstream = new FileWriter(ConnectionHandler.dbconfigfile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(hostname +"\n");
			out.write(port+"\n");
			out.write(dbname+"\n");
			out.write(username+"\n");
			out.write(password+"\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while updating");
			e.printStackTrace();
			return 0;
		}       
		return 1;
	}
	
	public Connection getConnection()
	{
		if(ConnectionHandler.conn == null){
			Scanner sc = null;
			try{		
				sc = new Scanner(new File(ConnectionHandler.dbconfigfile));
				String hostname = sc.nextLine();
				String port 	= sc.nextLine();
				String dbname 	= sc.nextLine();
				String username = sc.nextLine();
				String password = sc.nextLine();
			
				String connstring = "jdbc:mysql" + "://" + hostname + ":" + port + "/" + dbname + 
						"?user=" + username + "&password=" + password;	
				//System.out.println("Connection String : " + connstring);
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				this.setConn(DriverManager.getConnection(connstring));		
			}	
			catch(SQLException e){
				String error_msg = "Invalid DB settings";
				JOptionPane.showMessageDialog(null,error_msg,"Check DB Settings", JOptionPane.ERROR_MESSAGE);
				System.out.println("SQL Exception: " + e.getMessage());
			}
			catch(Exception e){
				System.out.println("Exception:ConnHndlr " + e.getMessage());
			}
			finally{			
			}
			
		}
		return this.getConn();
	}
	
	public void closeConnection(){
		try	{
			if(this.getConn() == null)
				return;
			ConnectionHandler.conn.close();
			this.setConn(null);
		}   
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		ConnectionHandler.conn = conn;
	}
}