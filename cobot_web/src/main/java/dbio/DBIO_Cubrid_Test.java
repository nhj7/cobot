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
			Class.forName("org.mariadb.jdbc.Driver");
			Class.forName("net.sf.log4jdbc.DriverSpy");
			//System.out.println("1111");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public static void main(String[] args) throws Throwable {
		String url = "jdbc:log4jdbc:mariadb://220.230.118.187:33067/cobot";
		
	 	Connection conn = DriverManager.getConnection(url, "cobot", "cobot1234");
	 	Statement stmt = conn.createStatement();

	 	ResultSet rs = stmt.executeQuery("SELECT * FROM tb_coin"); 
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



