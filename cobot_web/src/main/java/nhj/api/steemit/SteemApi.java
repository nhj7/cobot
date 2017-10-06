package nhj.api.steemit;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.websocket.Session;

import org.slf4j.LoggerFactory;

import eu.bittrade.libs.steemj.SteemApiWrapper;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.base.models.Transaction;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

public class SteemApi {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(SteemApi.class);
	
	protected static final SteemJConfig CONFIG = SteemJConfig.getInstance();
    protected static SteemApiWrapper steemApiWrapper;
    protected static final short REF_BLOCK_NUM = (short) 34294;
    protected static final long REF_BLOCK_PREFIX = 3707022213L;
    protected static final String EXPIRATION_DATE = "2016-04-06T08:29:27UTC";

    protected static Transaction transaction;
    
    public static void init(){
    	setupIntegrationTestEnvironment();
    }
    
    protected static boolean setupIntegrationTestEnvironment() {
        setupBasicTestEnvironment();

        try {
            // Change the default settings if needed.
            //CONFIG.setWebsocketEndpointURI(new URI("wss://this.piston.rocks"));
        	CONFIG.setWebsocketEndpointURI(new URI("wss://steemd-int.steemit.com"));
            CONFIG.setTimeout(0);
            CONFIG.setSslVerificationDisabled(true);

            steemApiWrapper = new SteemApiWrapper();
            
            return true;
            
        } catch (SteemCommunicationException | URISyntaxException e) {
        	
        	try {
				CONFIG.setWebsocketEndpointURI(new URI("wss://this.piston.rocks"));
				steemApiWrapper = new SteemApiWrapper();
				return true;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Could not create a SteemJ instance. - Execution stopped.", e1);
				
				return false;
			}
        	
        	
            //System.out.println("Could not create a SteemJ instance. - Test execution stopped.");
        }finally {
        	logger.info("CONFIG.uri : " + CONFIG.getWebsocketEndpointURI());
		}
        
    }
    
    
    
    public static boolean checkConnection() {
    	// communicationHandler
    	CommunicationHandler communicationHandler = null;
    	try {
			Field field = steemApiWrapper.getClass().getDeclaredField("communicationHandler");
			field.setAccessible(true);
			communicationHandler = (CommunicationHandler) field.get(steemApiWrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	try {
    		Field field = communicationHandler.getClass().getDeclaredField("session");
			field.setAccessible(true);
			Session session = (Session) field.get(communicationHandler);
			
			
			
			logger.info("[session] : "+ session.isOpen() + ", uri : " + CONFIG.getWebsocketEndpointURI());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	return true;
    	
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
    	checkConnection();
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
    	checkConnection();
    	final Discussion discussion = steemApiWrapper.getContent(ACCOUNT, PERMLINK);        
        return discussion;
    }
    
    
    public static List<Discussion> getContentReplies(String account, String permLink) throws Exception {
    	checkConnection();
        final List<Discussion> replies = steemApiWrapper.getContentReplies(account, permLink);
        /*
        for( Discussion post : replies ){
        	System.out.println( post.getBody() );
        }
        */
        return replies;
    }
    
    public static void main(String[] args) throws Throwable {
    	
    	//ConfigurationFactory.setConfigurationFactory(new Log4j2ConfigurationFactory()); 
    	
    	
        
    	init();
    	

    	org.slf4j.Logger logger = LoggerFactory.getLogger(SteemApi.class);
    	
    	//final Logger logger = LogManager.getLogger("SteemApi");
    	
    	//org.apache.logging.log4j.core.Logger logger = ctx.getLogger("Steemapi");

		
		logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        
        System.out.println("123123");
        
        
        logger.error("error2");
    	
    	//List<Discussion> discussions = getDiscussionBy("kr-market", 100);
        
        
    	Discussion discussions;
		try {						
			
			discussions = getContent("nhj12311", "steemit-steemit-more-info");
			logger.info("Hello : " + discussions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("error", e);
			
		}
		
        
		
		
    	
    	
	}
}
