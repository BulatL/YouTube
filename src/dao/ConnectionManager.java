package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";	

	private static Connection connection;

	public static Connection getConnection()
	{
		
	 try {
		String url ="jdbc:mysql://localhost:3306/youtube?autoReconnect=true&useSSL=true";
		Class.forName("com.mysql.jdbc.Driver");
		try{
			connection = DriverManager.getConnection(url, USER_NAME, PASSWORD);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	} catch (ClassNotFoundException e){
		// TODO: handle exception
		e.printStackTrace();
	}
	 return connection;
	}

	public static void close() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
}