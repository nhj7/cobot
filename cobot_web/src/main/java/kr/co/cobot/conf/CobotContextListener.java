package kr.co.cobot.conf;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import kr.co.cobot.bot.ExchManager;

public class CobotContextListener extends ContextLoaderListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextDestroyed(event);
		
		System.out.println("CobotContextListener End...");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextInitialized(event);
		
		System.out.println("CobotContextListener Start...");
		System.out.println("mode : " + System.getProperty("mode") );
		
		new Thread( new ExchManager()).start();
		
	}
	
}
