package kr.co.cobot.conf;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
 


public class MyBatisFactory {
	private static SqlSessionFactory sqlSessionFactory;
	
	/**
	 * Returns a DataSource object.
	 *
	 * @return a DataSource.
	 */  
	public static DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		//dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		//dataSource.setUrl("jdbc:mariadb://172.30.1.13:33067/cobot");
		
		//dataSource.setDriverClassName("fr.ms.log4jdbc.Driver");
		dataSource.setDriverClassName("net.sf.log4jdbc.DriverSpy");
		dataSource.setUrl("jdbc:log4jdbc:mariadb://localhost:33067/cobot");
		dataSource.setUsername("cobot");
		dataSource.setPassword("cobot1234");
		return dataSource;
	}
	private static SqlSession session;
	static{
		try {
			Configuration config = new Configuration();

			//
			// Get DataSource object.
			//
			DataSource dataSource = getDataSource();

			//
			// Creates a transaction factory.
			//
			TransactionFactory trxFactory = new JdbcTransactionFactory();

			//
			// Creates an environment object with the specified name, transaction
			// factory and a data source.
			//
			Environment env = new Environment("cobot", trxFactory, dataSource);
			config.setCacheEnabled(false);
			
			config.setEnvironment(env);
			
			//config.addMapper(BAT_DAO.class);	// MapperRegistry
			
			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
			}
			//System.out.println( "sqlSessionFactory : " + sqlSessionFactory);
			session = sqlSessionFactory.openSession(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object getDAO( Class c ){
		return session.getMapper(c);
	}
	

	public static void main(String[] args) throws IOException {
		
		
	}
	

	public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
