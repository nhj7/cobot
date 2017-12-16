package kr.co.cobot.conf;

import java.lang.reflect.Field;

import javax.servlet.ServletContextEvent;

import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.web.context.ContextLoaderListener;

import kr.co.cobot.bot.AlarmBot;
import kr.co.cobot.bot.CacheImgBot;
import kr.co.cobot.bot.SteemitBot;
import kr.co.cobot.bot.TickerBot;
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
		
		/*
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for ( Logger logger : loggers ) {
		    logger.setLevel(Level.OFF);
		}
		*/
		
		
		//LogConfiguration.init();
		
		try {
			if( false && NetUtil.isMyLocal() ){
				Object source = event.getSource();
				Field field = source.getClass().getDeclaredField("context");
				field.setAccessible(true);
				ApplicationContext ac = (ApplicationContext) field.get(source);
				field = ac.getClass().getDeclaredField("context");
				field.setAccessible(true);
				StandardContext standardContext = (StandardContext) field.get(ac);
				SecurityConstraint security = new SecurityConstraint();				 
				boolean flag = !NetUtil.isMyLocal();
				SecurityConstraint[] arrSecurity = standardContext.findConstraints();
				
				for(int i = 0 ; i < arrSecurity.length;i++){
					arrSecurity[i].setAuthConstraint(false);
					standardContext.removeConstraint(arrSecurity[i]);
				}
				
				System.out.println("Set Tomcat AuthConstraint : "+flag);
			}
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.println("CobotContextListener Start......");
		System.out.println("mode: " + System.getProperty("mode") );
		
		try {
			new Thread( new TickerBot()).start();			
			if( !NetUtil.isMyLocal() ){
				
			}
			
			AlarmBot.init();
			//CacheImgBot.init();
			
			//System.out.println("SteemitBot.init().");
			//SteemitBot.init();
			
			
			//new Thread( new ExchManager()).start();
			
			
			
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
