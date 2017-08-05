package nhj.api.steemit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    
    protected static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

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
            CONFIG.setWebsocketEndpointURI(new URI("wss://this.piston.rocks"));
            // Create a new apiWrapper with your config object.
            CONFIG.setTimeout(0);
            CONFIG.setSslVerificationDisabled(true);

            steemApiWrapper = new SteemApiWrapper();
        } catch (SteemCommunicationException | URISyntaxException e) {
            LOGGER.error("Could not create a SteemJ instance. - Test execution stopped.", e);
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

        final DiscussionSortType[] sortTypes = new DiscussionSortType[] { DiscussionSortType.SORT_BY_TRENDING,
                DiscussionSortType.SORT_BY_CREATED, DiscussionSortType.SORT_BY_ACTIVE,
                DiscussionSortType.SORT_BY_CASHOUT, DiscussionSortType.SORT_BY_VOTES,
                DiscussionSortType.SORT_BY_CHILDREN, DiscussionSortType.SORT_BY_HOT, DiscussionSortType.SORT_BY_BLOG,
                DiscussionSortType.SORT_BY_PROMOTED, DiscussionSortType.SORT_BY_PAYOUT,
                DiscussionSortType.SORT_BY_FEED };

        //for (final DiscussionSortType type : sortTypes) {
        final List<Discussion> discussions = steemApiWrapper.getDiscussionsBy(tag, limit, DiscussionSortType.SORT_BY_CREATED);
        
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
		
    	init();
    	List<Discussion> discussions = getDiscussionBy("kr-seller", 10);
    	
    	Gson gson = new Gson();
    	
    	for( Discussion post : discussions ){
        	System.out.println("----------------------------------------------------------");
        	System.out.println(post.getTitle());
        	System.out.println(post.getAuthor().getAccountName());
        	System.out.println("category : "+post.getCategory());
        	
        	
        	JsonObject postMeta = gson.fromJson(post.getJsonMetadata(), JsonObject.class);
        	
        	System.out.println("postMeta : "+postMeta);
        	System.out.println("postMeta : "+postMeta.get("tags").toString().replaceAll("\"", ""));
        	
        	JsonArray arrayImg = postMeta.get("image").getAsJsonArray();
        	
        	if( arrayImg.size() > 0 ){
        		System.out.println("대문 이미지 주소 : "+ arrayImg.get(0).toString().replaceAll("\"", ""));
        	}
        	if( arrayImg.size() > 1 ){
        		System.out.println("상품 이미지 주소 : "+ arrayImg.get(1).toString().replaceAll("\"", ""));
        	}
        	
        	System.out.println("url : "+post.getUrl());
        	//System.out.println("body : "+post.getBody());
        	
        	System.out.println("----------------------------------------------------------");
        }
    	
	}
}
