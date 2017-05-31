package dbio;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.aspectj.lang.Signature;

import cubrid.jdbc.driver.CUBRIDStatement;



public class DBIO_Cubrid_Test {
	
	static{
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CUBRIDStatement cst;
	}
	
	
	
	public static void main(String[] args) throws Throwable {
		String url = "jdbc:cubrid:127.0.0.1:30000:demodb:::";
		
	 	Connection conn = DriverManager.getConnection(url, "tester", "tester");
	 	Statement stmt = conn.createStatement();

	 	ResultSet rs = stmt.executeQuery("SELECT * FROM code"); 
	 	while(rs.next())
	 	{
	  		System.out.print(rs.getString(1) + " : ");
	  		System.out.println(rs.getString(2) + "");
	 	}

	 	rs.close();
	 	stmt.close();
	 	conn.close();
	}
	
	

}



