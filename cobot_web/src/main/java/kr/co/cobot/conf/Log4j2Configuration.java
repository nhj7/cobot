package kr.co.cobot.conf;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.slf4j.LoggerFactory;

import nhj.api.steemit.SteemApi;

public class Log4j2Configuration {
	
	private static String LOG_PATH = "/log/cobot/";
	
	
	
	
	public static void main(String[] args) {
		
		/*
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        Map appenderMap = loggerConfig.getAppenders();        
        for( java.util.Iterator it = appenderMap.keySet().iterator();it.hasNext();) {
        	loggerConfig.removeAppender(it.next().toString());
        }
        
        ConfigurationBuilder< BuiltConfiguration > builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel( Level.INFO);
        builder.setConfigurationName("RollingBuilder");
        // create a console appender
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
            ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout")
            .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
        builder.add( appenderBuilder );
        // create a rolling file appender
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
            .addAttribute("pattern", "%d [%t] %-5level: %msg%n");
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
            .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
            .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
        appenderBuilder = builder.newAppender("rolling", "RollingFile")
            .addAttribute("fileName", "target/rolling.log")
            .addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.log.gz")
            .add(layoutBuilder)
            .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);

        // create the new logger
        builder.add( builder.newLogger( "TestLogger", Level.DEBUG )
            .add( builder.newAppenderRef( "rolling" ) )
            .addAttribute( "additivity", false ) );

        builder.add( builder.newRootLogger( Level.DEBUG )
            .add( builder.newAppenderRef( "rolling" ) ) );
        
        Configurator.shutdown(ctx);
        
        ctx = Configurator.initialize(builder.build());
		*/
		
        
        
        
//        
//        String PATTERN = "[%d{yyyy-MM-dd HH:mm:ss}] [%5p] [%C{2}.%M:%L] : %m%n";
//        PatternLayout layout = PatternLayout.createLayout(PATTERN, null, null,
//                null, null, false, false, null, "");
//        
//        ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
//        consoleAppender.start();
//        config.addAppender(consoleAppender);
//        
//        AppenderRef[] refs = new AppenderRef[] { AppenderRef.createAppenderRef(consoleAppender.getName(), null, null) };
//        
//        
//        		
//        loggerConfig.addAppender(consoleAppender, null, null);
//        config.addLogger(LogManager.ROOT_LOGGER_NAME, loggerConfig);
//        
//        
//      //PatternLayout layout = PatternLayout.newBuilder().withConfiguration(config).withPattern(PatternLayout.SIMPLE_CONVERSION_PATTERN).build();
//        
//        
//		SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy("1000KB");
//		
//		TimeBasedTriggeringPolicy policy2 = TimeBasedTriggeringPolicy.createPolicy("1", "true");
//		
//		DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("31", "0", null, null, null, false, config);
//		
//		String logFile = LOG_PATH + "/log.log";
//		String logBakFile = LOG_PATH + "/bak/log-%d{yyyy-MM-dd-HH-mm-ss}-%i.log";
//		  
//		RollingFileManager fileManager = RollingFileManager.getFileManager(logFile , logBakFile, false, false, policy2, strategy, null, layout, 256, false, false, config);
//		
//		ConfigurationBuilder< BuiltConfiguration > builder = ConfigurationBuilderFactory.newConfigurationBuilder();
//		ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
//			    .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
//			    .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
//
//		
//		
//		RollingFileAppender rolling = RollingFileAppender.createAppender(logFile, logBakFile,
//			  "true", "File", "false", "256", "true", policy, strategy, layout, (Filter) null, "false", "false", (String) null, config);
//		
//		policy.initialize(fileManager);
//		policy2.initialize(fileManager);
//		
//		
//		
//		rolling.start();
//		config.addAppender(rolling);
//		AppenderRef ref = AppenderRef.createAppenderRef("File", Level.INFO, null);
//		refs = new AppenderRef[] { ref };      
//		loggerConfig.addAppender(rolling, Level.INFO, null);
//		config.addLogger(LogManager.ROOT_LOGGER_NAME, loggerConfig);
//        
//        
//        ctx.updateLoggers();
//        ctx.getLoggers();
        
        
        
        
        
        
        
        
        
        System.out.println("123");
        
        
        org.slf4j.Logger logger = LoggerFactory.getLogger(SteemApi.class);
        
        logger.info("info");
        
        for(int i = 0; i < 100000 ; i++) {
        	logger.info("info");
        }
        
        System.out.println("456");
		
		
	}
}
