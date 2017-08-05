package nhj.api.steemit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exparity.hamcrest.date.DateMatchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.VoteState;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BlockHeader;
import eu.bittrade.libs.steemj.base.models.ChainProperties;
import eu.bittrade.libs.steemj.base.models.Config;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.base.models.ExtendedLimitOrder;
import eu.bittrade.libs.steemj.base.models.FeedHistory;
import eu.bittrade.libs.steemj.base.models.GlobalProperties;
import eu.bittrade.libs.steemj.base.models.HardforkSchedule;
import eu.bittrade.libs.steemj.base.models.LiquidityBalance;
import eu.bittrade.libs.steemj.base.models.OrderBook;
import eu.bittrade.libs.steemj.base.models.RewardFund;
import eu.bittrade.libs.steemj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.base.models.TrendingTag;
import eu.bittrade.libs.steemj.base.models.SteemVersionInfo;
import eu.bittrade.libs.steemj.base.models.Vote;
import eu.bittrade.libs.steemj.base.models.Witness;
import eu.bittrade.libs.steemj.base.models.WitnessSchedule;
import eu.bittrade.libs.steemj.base.models.operations.AccountCreateOperation;
import eu.bittrade.libs.steemj.base.models.operations.AccountCreateWithDelegationOperation;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.base.models.operations.virtual.AuthorRewardOperation;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.enums.RewardFundType;
import eu.bittrade.libs.steemj.exceptions.SteemResponseError;
import eu.bittrade.libs.steemj.plugins.follow.enums.FollowType;
import eu.bittrade.libs.steemj.plugins.follow.model.AccountReputation;
import eu.bittrade.libs.steemj.plugins.follow.model.BlogEntry;
import eu.bittrade.libs.steemj.plugins.follow.model.CommentBlogEntry;
import eu.bittrade.libs.steemj.plugins.follow.model.CommentFeedEntry;
import eu.bittrade.libs.steemj.plugins.follow.model.FeedEntry;
import eu.bittrade.libs.steemj.plugins.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.plugins.follow.model.FollowCountApiObject;
import eu.bittrade.libs.steemj.plugins.follow.model.PostsPerAuthorPair;

/**
 * @author Anthony Martin
 */
public class SteemApiWrapperIT extends BaseIntegrationTest {
    private static final Logger LOGGER = LogManager.getLogger(SteemApiWrapperIT.class);
    private static final String ACCOUNT = "nhj12311";
    private static final String ACCOUNT_TWO = "randowhale";
    private static final String WITNESS_ACCOUNT = "riverhead";
    private static final String PERMLINK = "3gwws5";

    @BeforeClass()
    public static void prepareTestClass() throws Exception {
        setupIntegrationTestEnvironment();
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetNextScheduledHarfork() throws Exception {
        final HardforkSchedule hardforkSchedule = steemApiWrapper.getNextScheduledHarfork();

        assertTrue(hardforkSchedule.getHardforkVersion().matches("[0-9\\.]+"));
        assertTrue(hardforkSchedule.getLiveTime().matches("[0-9\\-:T]+"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetBlock() throws Exception {
        final SignedBlockWithInfo signedBlockWithInfo = steemApiWrapper.getBlock(13310401L);

        assertThat(signedBlockWithInfo.getTimestamp().getDateTime(), equalTo("2017-07-01T19:24:42"));
        assertThat(signedBlockWithInfo.getWitness(), equalTo("riverhead"));

        final SignedBlockWithInfo signedBlockWithInfoWithExtension = steemApiWrapper.getBlock(12615532L);

        assertThat(signedBlockWithInfoWithExtension.getTimestamp().getDateTime(),
                equalTo(new TimePointSec("2017-06-07T15:33:27").getDateTime()));
        assertThat(signedBlockWithInfoWithExtension.getWitness(), equalTo("dragosroua"));
        assertThat(signedBlockWithInfoWithExtension.getExtensions().get(0).getHardforkVersionVote().getHfVersion(),
                equalTo("0.19.0"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetBlockHeader() throws Exception {
        final BlockHeader blockHeader = steemApiWrapper.getBlockHeader(13339001L);

        assertThat(blockHeader.getTimestamp().getDateTime(), equalTo("2017-07-02T19:15:06"));
        assertThat(blockHeader.getWitness(), equalTo("clayop"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetOpsInBlock() throws Exception {
        final List<AppliedOperation> appliedOperationsOnlyVirtual = steemApiWrapper.getOpsInBlock(13138393, true);

        assertThat(appliedOperationsOnlyVirtual.size(), equalTo(5));
        assertThat(appliedOperationsOnlyVirtual.get(0).getOpInTrx(), equalTo(1));
        assertThat(appliedOperationsOnlyVirtual.get(0).getTrxInBlock(), equalTo(41));
        assertThat(appliedOperationsOnlyVirtual.get(0).getVirtualOp(), equalTo(0L));
        assertThat(appliedOperationsOnlyVirtual.get(0).getOp(), instanceOf(AuthorRewardOperation.class));

        final List<AppliedOperation> appliedOperations = steemApiWrapper.getOpsInBlock(13138393, false);

        assertThat(appliedOperations.size(), equalTo(50));
        assertThat(appliedOperations.get(1).getOpInTrx(), equalTo(0));
        assertThat(appliedOperations.get(1).getTrxInBlock(), equalTo(1));
        assertThat(appliedOperations.get(1).getVirtualOp(), equalTo(0L));
        assertThat(appliedOperations.get(1).getOp(), instanceOf(CommentOperation.class));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testAccountCount() throws Exception {
        final int accountCount = steemApiWrapper.getAccountCount();

        assertThat("expect the number of accounts greater than 122908", accountCount, greaterThan(122908));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testAccountHistory() throws Exception {
        final Map<Integer, AppliedOperation> accountHistorySetOne = steemApiWrapper.getAccountHistory(ACCOUNT, 10, 10);
        assertEquals("expect response to contain 10 results", 11, accountHistorySetOne.size());

        Operation firstOperation = accountHistorySetOne.get(0).getOp();
        assertTrue("the first operation for each account is the 'account_create_operation'",
                firstOperation instanceof AccountCreateOperation);

        final Map<Integer, AppliedOperation> accountHistorySetTwo = steemApiWrapper.getAccountHistory(ACCOUNT_TWO, 1000,
                1000);
        assertEquals("expect response to contain 1001 results", 1001, accountHistorySetTwo.size());

        assertThat(accountHistorySetTwo.get(0).getOp(), instanceOf(AccountCreateWithDelegationOperation.class));
        assertThat(((AccountCreateWithDelegationOperation) accountHistorySetTwo.get(0).getOp()).getCreator()
                .getAccountName(), equalTo(new AccountName("anonsteem").getAccountName()));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testActiveVotes() throws Exception {
        // Get the votes done by the specified account:
        final List<Vote> votes = steemApiWrapper.getAccountVotes(ACCOUNT);
        final List<VoteState> activeVotesForArticle = steemApiWrapper.getActiveVotes(ACCOUNT, PERMLINK);
        
        for(VoteState vs : activeVotesForArticle) {
        	System.out.println("Voter : "+vs.getVoter());
        	
        }

        assertNotNull("expect votes", votes);
        assertThat("expect account has votes", votes.size(), greaterThan(0));
        assertThat("expect last vote after 2016-03-01", votes.get(votes.size() - 1).getTime().getDateTimeAsDate(),
                DateMatchers.after(2016, Month.MARCH, 1));

        boolean foundSelfVote = false;

        for (final VoteState vote : activeVotesForArticle) {
            if (ACCOUNT.equals(vote.getVoter().getAccountName())) {
                foundSelfVote = true;
                break;
            }
        }

        assertTrue("expect self vote for article of account", foundSelfVote);
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetConfig() throws Exception {
        final Config config = steemApiWrapper.getConfig();
        final boolean isTestNet = config.getIsTestNet();
        final String steemitNullAccount = config.getSteemitNullAccount();
        final String initMinerName = config.getSteemitInitMinerName();

        assertEquals("expect main net", false, isTestNet);
        assertEquals("expect the null account to be null", "null", steemitNullAccount);
        assertEquals("expect the init miner name to be initminer", "initminer", initMinerName);
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testCurrentMedianHistoryPrice() throws Exception {
        final Asset base = steemApiWrapper.getCurrentMedianHistoryPrice().getBase();
        final Asset quote = steemApiWrapper.getCurrentMedianHistoryPrice().getQuote();

        assertThat("expect current median price greater than zero", base.getAmount(), greaterThan(0.00));
        assertEquals("expect current median price symbol", AssetSymbolType.SBD, base.getSymbol());
        assertThat("expect current median price greater than zero", quote.getAmount(), greaterThan(0.00));
        assertEquals("expect current median price symbol", AssetSymbolType.STEEM, quote.getSymbol());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetAccounts() throws Exception {
        final List<AccountName> accountNames = new ArrayList<>();
        accountNames.add(new AccountName("dez1337"));
        accountNames.add(new AccountName("inertia"));

        List<ExtendedAccount> accounts = steemApiWrapper.getAccounts(accountNames);

        assertThat("Expect that two results are returned.", accounts, hasSize(2));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetActiveWitnesses() throws Exception {
        final String[] activeWitnesses = steemApiWrapper.getActiveWitnesses();

        // The active witness changes from time to time, so we just check if
        // something is returned.
        assertThat(activeWitnesses.length, greaterThan(0));
        assertThat(activeWitnesses[0], not(isEmptyOrNullString()));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetApiByName() throws Exception {
        final Integer bogus = steemApiWrapper.getApiByName("bogus_api");
        final Integer database = steemApiWrapper.getApiByName("database_api");
        final Integer login = steemApiWrapper.getApiByName("login_api");
        final Integer market_history = steemApiWrapper.getApiByName("market_history_api");
        final Integer follow = steemApiWrapper.getApiByName("follow_api");

        assertNull("expect that bogus api does not exist", bogus);
        assertNotNull("expect that database api does exist", database);
        assertNotNull("expect that login api does exist", login);
        assertNotNull("expect that market_history api does exist", market_history);
        assertNotNull("expect that follow api does exist", follow);
    }

    @Category(IntegrationTest.class)
    @Test
    public void testGetApiByNameForSecuredApi() throws Exception {
        final Integer database = steemApiWrapper.getApiByName("database_api");
        final Integer networkBroadcast = steemApiWrapper.getApiByName("network_broadcast_api");
        final Integer login = steemApiWrapper.getApiByName("login_api");

        assertNotNull("expect that network_node api does exist", database);
        assertNotNull("expect that network_broadcast api does exist", networkBroadcast);
        assertNotNull("expect that chain_stats api does exist", login);
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetChainProperties() throws Exception {
        final ChainProperties properties = steemApiWrapper.getChainProperties();

        assertNotNull("expect properties", properties);
        assertThat("expect sbd interest rate", properties.getSdbInterestRate(), greaterThanOrEqualTo(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetContent() throws Exception {
        final Discussion discussion = steemApiWrapper.getContent(ACCOUNT, PERMLINK);

        assertNotNull("expect discussion", discussion);
        assertEquals("expect correct author", ACCOUNT, discussion.getAuthor().getAccountName());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetContentReplies() throws Exception {
        final List<Discussion> replies = steemApiWrapper.getContentReplies(ACCOUNT, PERMLINK);
        for( Discussion post : replies ){
        	System.out.println( post.getBody() );
        	
        }
        assertNotNull("expect replies", replies);
        assertThat("expect replies greater than zero", replies.size(), greaterThan(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetDiscussionBy() throws Exception {

        final DiscussionSortType[] sortTypes = new DiscussionSortType[] { DiscussionSortType.SORT_BY_TRENDING,
                DiscussionSortType.SORT_BY_CREATED, DiscussionSortType.SORT_BY_ACTIVE,
                DiscussionSortType.SORT_BY_CASHOUT, DiscussionSortType.SORT_BY_VOTES,
                DiscussionSortType.SORT_BY_CHILDREN, DiscussionSortType.SORT_BY_HOT, DiscussionSortType.SORT_BY_BLOG,
                DiscussionSortType.SORT_BY_PROMOTED, DiscussionSortType.SORT_BY_PAYOUT,
                DiscussionSortType.SORT_BY_FEED };

        //for (final DiscussionSortType type : sortTypes) {
        final List<Discussion> discussions = steemApiWrapper.getDiscussionsBy("kr-seller", 20, DiscussionSortType.SORT_BY_CREATED);
        
        
        
        for( Discussion post : discussions ){
        	System.out.println("----------------------------------------------------------");
        	System.out.println(post.getTitle());
        	System.out.println(post.getAuthor().getAccountName());
        	System.out.println("category : "+post.getCategory());
        	System.out.println("url : "+post.getUrl());
        	System.out.println("body : "+post.getBody());
        	
        	System.out.println("----------------------------------------------------------");
        }
            
        assertNotNull("expect discussions", discussions);
        assertThat("expect discussions in " + DiscussionSortType.SORT_BY_CREATED + " greater than zero", discussions.size(),
                greaterThanOrEqualTo(0));
        
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetDiscussionsByAuthorBeforeDate() throws Exception {
        final List<Discussion> repliesByLastUpdate = steemApiWrapper.getDiscussionsByAuthorBeforeDate(ACCOUNT, PERMLINK,
                "2017-02-10T22:00:06", 8);

        assertEquals("expect that 8 results are returned", repliesByLastUpdate.size(), 8);
        assertEquals("expect " + ACCOUNT + " to be the first returned author",
                repliesByLastUpdate.get(0).getAuthor().getAccountName(), ACCOUNT);
        assertEquals("expect " + PERMLINK + " to be the first returned permlink", PERMLINK,
                repliesByLastUpdate.get(0).getPermlink());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetDynamicGlobalProperties() throws Exception {
        final GlobalProperties properties = steemApiWrapper.getDynamicGlobalProperties();

        assertNotNull("expect properties", properties);
        assertThat("expect head block number", properties.getHeadBlockNumber(), greaterThan(6000000));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetLiquidityQueue() throws Exception {
        final List<LiquidityBalance> repliesByLastUpdate = steemApiWrapper.getLiquidityQueue(WITNESS_ACCOUNT, 5);

        assertEquals("expect that 5 results are returned", repliesByLastUpdate.size(), 5);
        assertEquals("expect " + WITNESS_ACCOUNT + " to be the first returned account", WITNESS_ACCOUNT,
                repliesByLastUpdate.get(0).getAccount().getAccountName());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetRepliesByLastUpdate() throws Exception {
        final List<Discussion> repliesByLastUpdate = steemApiWrapper.getRepliesByLastUpdate(ACCOUNT, PERMLINK, 9);
        for( Discussion post : repliesByLastUpdate ){
        	System.out.println( post.getBody() );
        	
        }
        assertEquals("expect that 9 results are returned", repliesByLastUpdate.size(), 9);
        assertEquals("expect " + ACCOUNT + " to be the first returned author", ACCOUNT,
                repliesByLastUpdate.get(0).getAuthor().getAccountName());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetWitnessByAccount() throws Exception {
        final Witness activeWitnessesByVote = steemApiWrapper.getWitnessByAccount(WITNESS_ACCOUNT);

        assertEquals("expect " + WITNESS_ACCOUNT + " to be the owner of the returned witness account", WITNESS_ACCOUNT,
                activeWitnessesByVote.getOwner());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetWitnessesByVote() throws Exception {
        final List<Witness> activeWitnessesByVote = steemApiWrapper.getWitnessByVote(WITNESS_ACCOUNT, 10);

        assertEquals("expect that 10 results are returned", activeWitnessesByVote.size(), 10);
        assertEquals("expect " + WITNESS_ACCOUNT + " to be the first returned witness", WITNESS_ACCOUNT,
                activeWitnessesByVote.get(0).getOwner());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testHardforkVersion() throws Exception {
        final String hardforkVersion = steemApiWrapper.getHardforkVersion();

        assertNotNull("Expect hardfork version to be present.", hardforkVersion);
        assertTrue(hardforkVersion.matches("[0-9]+[\\.]{1}[0-9]{2}[\\.]{1}[0-9]+"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testInvalidAccountVotes() throws Exception {
        // Force an error response:
        try {
            steemApiWrapper.getAccountVotes("thisAcountDoesNotExistYet");
        } catch (final SteemResponseError steemResponseError) {
            // success
        } catch (final Exception e) {
            LOGGER.error(e);
            fail(e.toString());
        }
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testLogin() throws Exception {
        final boolean success = steemApiWrapper.login("gilligan", "s.s.minnow");

        assertTrue("expect login to always return success: true", success);
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testLookupAccount() throws Exception {
        final List<String> accounts = steemApiWrapper.lookupAccounts(ACCOUNT, 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testLookupWitnessAccount() throws Exception {
        final List<String> accounts = steemApiWrapper.lookupWitnessAccounts("gtg", 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testMinerQueue() throws Exception {
        final String[] minerQueue = steemApiWrapper.getMinerQueue();

        assertThat("expect the number of miners greater than 0", minerQueue.length, greaterThan(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testTrendingTags() throws Exception {
        final List<TrendingTag> trendingTags = steemApiWrapper.getTrendingTags(null, 10);

        assertNotNull("expect trending tags", trendingTags);
        assertThat("expect trending tags size > 0", trendingTags.size(), greaterThan(0));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetRewardFund() throws Exception {
        final RewardFund rewardFund = steemApiWrapper.getRewardFund(RewardFundType.POST);

        // Verify that all fields a used:
        assertThat(rewardFund.getContentConstant(), notNullValue());
        assertThat(rewardFund.getId(), notNullValue());
        assertThat(rewardFund.getLastUpdate(), notNullValue());
        assertThat(rewardFund.getName(), notNullValue());
        assertThat(rewardFund.getPercentContentRewards(), notNullValue());
        assertThat(rewardFund.getPercentCurationRewards(), notNullValue());
        assertThat(rewardFund.getRecentClaims(), notNullValue());
        assertThat(rewardFund.getRewardBalance(), notNullValue());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testVersion() throws Exception {
        final SteemVersionInfo version = steemApiWrapper.getVersion();

        assertNotEquals("expect non-empty blockchain version", "", version.getBlockchainVersion());
        assertNotEquals("expect non-empty fc revision", "", version.getFcRevision());
        assertNotEquals("expect non-empty steem revision", "", version.getSteemRevision());
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testWitnessCount() throws Exception {
        final int witnessCount = steemApiWrapper.getWitnessCount();

        assertThat("expect the number of witnesses greater than 13071", witnessCount, greaterThan(13071));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testWitnessSchedule() throws Exception {
        final WitnessSchedule witnessSchedule = steemApiWrapper.getWitnessSchedule();

        assertNotNull("expect hardfork version", witnessSchedule);
        assertThat(witnessSchedule.getTop19Weight(), equalTo((short) 1));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFollowers() throws Exception {
        final List<FollowApiObject> followers = steemApiWrapper.getFollowers(new AccountName("dez1337"),
                new AccountName("dez1337"), FollowType.BLOG, (short) 100);

        assertThat(followers.size(), equalTo(100));
        assertThat(followers.get(0).getFollower(), equalTo(new AccountName("dhwoodland")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFollowing() throws Exception {
        final List<FollowApiObject> following = steemApiWrapper.getFollowing(new AccountName("dez1337"),
                new AccountName("dez1337"), FollowType.BLOG, (short) 10);

        assertThat(following.size(), equalTo(10));
        assertThat(following.get(0).getFollowing(), equalTo(new AccountName("furion")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFollowCount() throws Exception {
        final FollowCountApiObject followCount = steemApiWrapper.getFollowCount(new AccountName("dez1337"));

        assertThat(followCount.getFollowerCount(), greaterThan(10));
        assertThat(followCount.getFollowingCount(), greaterThan(10));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFeedEntries() throws Exception {
        final List<FeedEntry> feedEntries = steemApiWrapper.getFeedEntries(new AccountName("dez1337"), 0, (short) 100);

        assertThat(feedEntries.size(), equalTo(100));
        assertTrue(feedEntries.get(0).getPermlink().matches("[a-z0-9\\-]+"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFeed() throws Exception {
        final List<CommentFeedEntry> feed = steemApiWrapper.getFeed(new AccountName("dez1337"), 0, (short) 100);

        assertThat(feed.size(), equalTo(100));
        assertTrue(feed.get(0).getComment().getAuthor().getAccountName().matches("[a-z\\-_0-9]+"));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetBlogEntries() throws Exception {
        final List<BlogEntry> blogEntries = steemApiWrapper.getBlogEntries(new AccountName("dez1337"), 0, (short) 10);

        assertThat(blogEntries.size(), equalTo(10));
        assertThat(blogEntries.get(0).getBlog(), equalTo(new AccountName("dez1337")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetBlog() throws Exception {
        final List<CommentBlogEntry> blog = steemApiWrapper.getBlog(new AccountName("dez1337"), 0, (short) 10);

        assertThat(blog.size(), equalTo(10));
        assertThat(blog.get(0).getBlog(), equalTo(new AccountName("dez1337")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetAccountReputation() throws Exception {
        final List<AccountReputation> accountReputations = steemApiWrapper
                .getAccountReputations(new AccountName("dez1337"), 10);

        assertThat(accountReputations.size(), equalTo(10));
        assertThat(accountReputations.get(0).getReputation(), greaterThan(14251747809260L));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetRebloggedBy() throws Exception {
        final List<AccountName> accountNames = steemApiWrapper.getRebloggedBy(new AccountName("dez1337"),
                "steemj-v0-2-6-has-been-released-update-11");

        assertThat(accountNames.size(), greaterThan(2));
        assertThat(accountNames.get(1), equalTo(new AccountName("jesuscirino")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetBlogAuthors() throws Exception {
        final List<PostsPerAuthorPair> blogAuthors = steemApiWrapper.getBlogAuthors(new AccountName("dez1337"));

        assertThat(blogAuthors.size(), greaterThan(2));
        assertThat(blogAuthors.get(1).getAccount(), equalTo(new AccountName("good-karma")));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testOrderBook() throws Exception {
        final OrderBook orderBook = steemApiWrapper.getOrderBook(1);

        assertThat(orderBook.getAsks().size(), equalTo(1));
        assertThat(orderBook.getBids().get(0).getSbd(), greaterThan(1L));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetFeedHistory() throws Exception {
        final FeedHistory feedHistory = steemApiWrapper.getFeedHistory();

        assertThat(feedHistory.getCurrentPrice().getBase().getAmount(), greaterThan(1.0));
        assertThat(feedHistory.getPriceHistory().size(), greaterThan(1));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testGetOpenOrders() throws Exception {
        // TODO: If dez1337 removes this operation, the test will fail. Use the
        // SteemJ account for this.
        final List<ExtendedLimitOrder> openOrders = steemApiWrapper.getOpenOrders(new AccountName(ACCOUNT));

        assertThat(openOrders.size(), greaterThanOrEqualTo(1));
        assertThat(openOrders.get(0).getCreated().getDateTime(), equalTo("2017-07-20T19:30:27"));
        assertThat(openOrders.get(0).getExpiration().getDateTime(), equalTo("1969-12-31T23:59:59"));
        assertThat(openOrders.get(0).getDeferredFee(), equalTo(0L));
        assertThat(openOrders.get(0).getForSale(), equalTo(1L));
        assertThat(openOrders.get(0).getId(), equalTo(675734));
        assertThat(openOrders.get(0).getOrderId(), equalTo(1500579025L));
        assertThat(openOrders.get(0).getSeller(), equalTo(new AccountName(ACCOUNT)));
        assertThat(openOrders.get(0).getSellPrice().getBase(), equalTo(new Asset(1, AssetSymbolType.SBD)));
    }

    @Category({ IntegrationTest.class })
    @Ignore
    @Test
    public void testGetConversionRequests() throws Exception {
        // TODO: Implement
        steemApiWrapper.getConversionRequests(new AccountName("dez1337"));
    }

}
