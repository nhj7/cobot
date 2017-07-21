package kr.co.cobot.conf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import nhj.util.NetUtil;

public class HibernateCfg {
	private static SessionFactory sessionFactory;

	static {
		init();
	}
	
	public static void init(){
		try {
			String jdbcParam = "?autoReconnect=true&useUnicode=true&characterEncoding=utf8mb4&connectionCollation=utf8mb4_unicode_ci";
			String url = "jdbc:mariadb://localhost:33067" + jdbcParam;

			if ( NetUtil.isMyLocal() ) {
				url = "jdbc:mariadb://220.230.118.187:33067" + jdbcParam;
			}

			// Create the SessionFactory from hibernate.cfg.xml
			Configuration config = new Configuration()
					.setProperty("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver")
					.setProperty("hibernate.connection.url", url).setProperty("hibernate.connection.username", "cobot")
					.setProperty("hibernate.connection.password", "cobot1234")
					.setProperty("hibernate.connection.timeout", "0")
					.setProperty("hibernate.connection.autoRecoonect", "true")
					//.setProperty("hibernate.connection.pool_size", "10")
					//.setProperty("show_sql", "true")
					;
			List<Class> classes = new ArrayList<Class>();

			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

			// only register classes within "com.fooPackage" package
			for (BeanDefinition bd : scanner.findCandidateComponents("kr.co.cobot")) {
				String name = bd.getBeanClassName();
				try {
					Class c = Class.forName(name);
					
					System.out.println("Load Persistans Class : " + c);
					
					classes.add(c);
					config.addAnnotatedClass(c);
				} catch (Exception E) {
					// TODO: handle exception - couldn't load class in question
				}
			} // for
			
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
		if (s == null) {
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
		sessionFactory.close();
	}

}
