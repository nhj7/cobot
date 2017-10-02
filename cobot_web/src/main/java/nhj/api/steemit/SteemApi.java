package nhj.api.steemit;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.bittrade.libs.steemj.SteemApiWrapper;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.base.models.Transaction;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

public class SteemApi {
	protected static final SteemJConfig CONFIG = SteemJConfig.getInstance();
    protected static SteemApiWrapper steemApiWrapper;
    protected static final short REF_BLOCK_NUM = (short) 34294;
    protected static final long REF_BLOCK_PREFIX = 3707022213L;
    protected static final String EXPIRATION_DATE = "2016-04-06T08:29:27UTC";

    protected static Transaction transaction;
    
    public static void init(){
    	setupIntegrationTestEnvironment();
    }
    
    protected static void setupIntegrationTestEnvironment() {
        setupBasicTestEnvironment();

        try {
            // Change the default settings if needed.
            //CONFIG.setWebsocketEndpointURI(new URI("wss://this.piston.rocks"));
        	CONFIG.setWebsocketEndpointURI(new URI("wss://steemd-int.steemit.com"));
            
            // Create a new apiWrapper with your config object.
            CONFIG.setTimeout(0);
            CONFIG.setSslVerificationDisabled(true);

            steemApiWrapper = new SteemApiWrapper();
        } catch (SteemCommunicationException | URISyntaxException e) {
            //LOGGER.error("Could not create a SteemJ instance. - Test execution stopped.", e);
            System.out.println("Could not create a SteemJ instance. - Test execution stopped.");
        }
    }
    
    protected static void setupBasicTestEnvironment() {
        transaction = new Transaction();
        transaction.setExpirationDate(new TimePointSec(EXPIRATION_DATE));
        transaction.setRefBlockNum(REF_BLOCK_NUM);
        transaction.setRefBlockPrefix(REF_BLOCK_PREFIX);
        // TODO: Add extensions when supported.
        // transaction.setExtensions(extensions);
    }
    
    public static List<Discussion> getDiscussionBy(String tag, int limit) throws Exception {
    	//System.out.println("steemApi.getDiscussionBy 1");
        final DiscussionSortType[] sortTypes = new DiscussionSortType[] { DiscussionSortType.SORT_BY_TRENDING,
                DiscussionSortType.SORT_BY_CREATED, DiscussionSortType.SORT_BY_ACTIVE,
                DiscussionSortType.SORT_BY_CASHOUT, DiscussionSortType.SORT_BY_VOTES,
                DiscussionSortType.SORT_BY_CHILDREN, DiscussionSortType.SORT_BY_HOT, DiscussionSortType.SORT_BY_BLOG,
                DiscussionSortType.SORT_BY_PROMOTED, DiscussionSortType.SORT_BY_PAYOUT,
                DiscussionSortType.SORT_BY_FEED };
        
        //System.out.println("steemApi.getDiscussionBy 2");
        
        //for (final DiscussionSortType type : sortTypes) {
        final List<Discussion> discussions = steemApiWrapper.getDiscussionsBy(tag, limit, DiscussionSortType.SORT_BY_CREATED);
        
        //System.out.println("steemApi.getDiscussionBy 3");
        
        return discussions;
        
        
    }
    
    public static Discussion getContent(String ACCOUNT, String PERMLINK) throws Exception {
    	final Discussion discussion = steemApiWrapper.getContent(ACCOUNT, PERMLINK);        
        return discussion;
    }
    
    
    public static List<Discussion> getContentReplies(String account, String permLink) throws Exception {
        final List<Discussion> replies = steemApiWrapper.getContentReplies(account, permLink);
        /*
        for( Discussion post : replies ){
        	System.out.println( post.getBody() );
        }
        */
        return replies;
    }
    
    public static void main(String[] args) throws Throwable {
    	
    	ILoggerFactory factory = LoggerFactory.getILoggerFactory();		
		System.out.println("factory : " + factory);
		Logger logger = factory.getLogger("SteemApi");
		
    	SteemApi.init();
    	/*
    	 
    	 
    	Properties properties = new Properties();
		
		properties.put("log4j.rootLogger", "debug, stdout, logfile");
		properties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		properties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.stdout.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%5p] [%C{2}.%M:%L] : %m%n");
		
		PropertyConfigurator.configure(properties);
		*/
		
		
    	System.out.println("321");
    	
    	/*
    	ConfigurationBuilder< BuiltConfiguration > builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    	builder.setStatusLevel( Level.ERROR);
    	builder.setConfigurationName("RollingBuilder");
    	// create a console appender
    	AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE").addAttribute("target",
    	    ConsoleAppender.Target.SYSTEM_OUT);
    	appenderBuilder.add(builder.newLayout("PatternLayout")
    	    .addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%5p] [%C{2}.%M:%L] : %m%n"));
    	builder.add( appenderBuilder );
    	// create a rolling file appender
    	LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
    	    .addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%5p] [%C{2}.%M:%L] : %m%n");
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
    	LoggerContext ctx = Configurator.initialize(builder.build());
		*/
    	


		
		
		
		//LoggerContext ctx = Configurator.initialize(builder.build());
		
		

		

		
		
		//Logger logger = LoggerFactory.getLogger(LogConfiguration.class);
		
		
		
		logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        
        
    	
    	//List<Discussion> discussions = getDiscussionBy("kr-market", 100);
    	Discussion discussions;
		try {
			discussions = getContent("jumma", "kr-market-4-0808-1");
			System.out.println("Hello : " + discussions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("error", e);
			
		}
    	
    	//List<Discussion> discussions = getContentReplies("leesunmoo", "2017-8-11");
    	
    	
    	
	}
}
