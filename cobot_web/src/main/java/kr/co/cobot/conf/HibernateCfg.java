package kr.co.cobot.conf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import nhj.util.NetUtil;

public class HibernateCfg {
	private static final Logger logger = LoggerFactory.getLogger(HibernateCfg.class);
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	
	static {
		
		
		init();
	}
	
	public static void init(){
		try {
			String jdbcParam = "?autoReconnect=true&sessionVariables=character_set_client=utf8mb4,character_set_results=utf8mb4,character_set_connection=utf8mb4,collation_connection=utf8mb4_unicode_ci";
			String url = "jdbc:mariadb://localhost:33067/cobot" + jdbcParam;
			String id = "cobot";
			String passwd = "cobot1234";
			if ( NetUtil.isMyLocal() ) {
				url = "jdbc:mariadb://localhost:3306/cobot" + jdbcParam;
				id = "cobot_test";
				passwd = "test1234";
			}

			// Create the SessionFactory from hibernate.cfg.xml
			Configuration config = new Configuration()
					.setProperty("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver")
					.setProperty("hibernate.connection.url", url).setProperty("hibernate.connection.username", id)
					.setProperty("hibernate.connection.password", passwd)
					.setProperty("hibernate.connection.timeout", "0")
					.setProperty("hibernate.connection.autoRecoonect", "true")
					.setProperty("connection.autoReconnect", "true")
					.setProperty("connection.autoReconnectForPools", "true") 
					.setProperty("connection.is-connection-validation-required", "true")    
					
					//.setProperty("hibernate.connection.pool_size", "10")
					//.setProperty("show_sql", "true")
					;
			if ( NetUtil.isMyLocal() ) {
				//config.setProperty("hibernate.show_sql","true");
			}else{
				config.setProperty("hibernate.show_sql","false");
				//log4j.logger.org.hibernate=info;
				
			}
			
			List<Class> classes = new ArrayList<Class>();

			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

			// only register classes within "com.fooPackage" package
			String entityPath = "kr.co.cobot.entity";
			
			if ( NetUtil.isMyLocal() ) {
				//entityPath = "kr.co.cobot.test.entity";
			}
			for (BeanDefinition bd : scanner.findCandidateComponents(entityPath)) {
				String name = bd.getBeanClassName();
				try {
					
					

				    Class entity = Class.forName(name);
				    
					
					Annotation[] tables =  entity.getAnnotationsByType( javax.persistence.Table.class );
					
					
					
					
					
					
					
					System.out.println("Load Persistans Class : " + entity + ", tables : " + tables[0]);
					
					classes.add(entity);
					config.addAnnotatedClass(entity);
				} catch (Exception E) {
					E.printStackTrace();
					// TODO: handle exception - couldn't load class in question
				}
			} // for
			
			
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
					config.getProperties()).build();

			config.setSessionFactoryObserver(
			        new SessionFactoryObserver() {
			            @Override
			            public void sessionFactoryCreated(SessionFactory factory) {}
			            @Override
			            public void sessionFactoryClosed(SessionFactory factory) {
			                ((StandardServiceRegistryImpl) serviceRegistry).destroy();
			            }
			        }
			);

			
			
			//config.addAnnotatedClass(TbExchange.class);

			sessionFactory = config.buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	public static Session getCurrentSession() throws HibernateException {
		Session s = session.get();
		// Open a new Session, if this thread has none yet
		if (s == null || !s.isConnected() ) {
			if( sessionFactory.isClosed()){
				init();
			}
			s = sessionFactory.openSession();
			// Store it in the ThreadLocal variable
			session.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		if (s != null)
			s.close();
		session.set(null);
	}
	
	public static void closeSessionFactory(){
		if( sessionFactory != null ){
			sessionFactory.close();
			System.out.println("closeSessionFactory");
		}
		
	}
	
	public static void close() throws Exception{
	    if(serviceRegistry!= null) {
	        StandardServiceRegistryBuilder.destroy( serviceRegistry);
	    }
	}
	
	public static void main(String[] args) throws Throwable {
		init();
		
		Session session = getCurrentSession();
		
		logger.info("[session] : "+session);
		
		session.close();
		
		closeSession();
		closeSessionFactory();		
		close();
		
		logger.info("exit");
	}

}
