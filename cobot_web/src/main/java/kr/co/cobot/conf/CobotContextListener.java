package kr.co.cobot.conf;

import java.lang.reflect.Field;

import javax.servlet.ServletContextEvent;

import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.web.context.ContextLoaderListener;

import kr.co.cobot.bot.AlarmManager;
import kr.co.cobot.bot.ExchManager;
import kr.co.cobot.bot.TickManager;
import nhj.util.NetUtil;

public class CobotContextListener extends ContextLoaderListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextDestroyed(event);
		
		System.out.println("CobotContextListener End..");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextInitialized(event);
		
		try {
			
			Object source = event.getSource();
			Field field = source.getClass().getDeclaredField("context");
			field.setAccessible(true);
			org.apache.catalina.core.ApplicationContext ac = (org.apache.catalina.core.ApplicationContext) field.get(source);
			field = ac.getClass().getDeclaredField("context");
			field.setAccessible(true);
			StandardContext standardContext = (StandardContext) field.get(ac);
			SecurityConstraint security = new SecurityConstraint();
			
			boolean flag = !NetUtil.isMyLocal();
			
			security.setAuthConstraint( flag );
			standardContext.addConstraint(security);
			
			System.out.println("Set Tomcat AuthConstraint : "+flag);
			
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("CobotContextListener Start......");
		System.out.println("mode : " + System.getProperty("mode") );
		
		new Thread( new ExchManager()).start();
		new Thread( new TickManager()).start();
		AlarmManager.init();
	}
	
}
