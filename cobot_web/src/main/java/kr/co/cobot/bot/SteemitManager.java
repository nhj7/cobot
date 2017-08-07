package kr.co.cobot.bot;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.Discussion;
import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbPostInfo;
import kr.co.cobot.entity.TbPostMarketInfo;
import kr.co.cobot.entity.TbPostMarketReply;
import kr.co.cobot.entity.TbWebPushM;
import nhj.api.WebPushAPI;
import nhj.api.steemit.SteemApi;
import nhj.util.DateUtil;
import nhj.util.JsonUtil;

public class SteemitManager implements Runnable {
	private static Gson gson = new Gson();
	private static String STEEM_URL = "https://steemit.com";
	private static String STEEM_IMG_START_IDX_STR = ":url(";
	private static Session session = HibernateCfg.getCurrentSession();
	
	public static void main(String[] args) throws Throwable {
		CacheImgManager.init();
		SteemitManager.init();
	}
	public SteemitManager(){
	}
	
	public static void init(){
		SteemApi.init();
		SteemitManager sm = new SteemitManager();
		new Thread(sm).start();		
	}

	private static int TERM_STEEM_POST_CHECK = 10000;	// 10초 주기로 새 포스팅 체크
	private static String[] ARRAY_STEEM_DVCD = new String[]{
			"created"
			/*
			, "hot"
			, "trending"
			, "promoted"
			*/
	};
	
	public void run(){		
		while(true){
			try {
				
				long cur = System.currentTimeMillis();
				
				executeSave();
				
				System.out.println("[SteemitManager.executeSave()] : " + (System.currentTimeMillis()-cur) + "ms" );
				Thread.sleep(TERM_STEEM_POST_CHECK);
				
				cur = System.currentTimeMillis();
				marketRefresh();
				
				System.out.println("[SteemitManager.marketRefresh()] : " + (System.currentTimeMillis()-cur) + "ms" );
				
				Thread.sleep(TERM_STEEM_POST_CHECK);
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(TERM_STEEM_POST_CHECK);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private static void setPostFromDiscussion(TbPostInfo post, Discussion discussion){
		String author = discussion.getAuthor().getAccountName();	
		
    	JsonObject postMeta = gson.fromJson(discussion.getJsonMetadata(), JsonObject.class);
    	
    	String arrTagStr = postMeta.get("tags").toString().replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
    	
    	String postImgUrl = "";
    	String prodImgUrl = "";
    	if( postMeta.get("image") != null ){
    		JsonArray arrayImg = postMeta.get("image").getAsJsonArray();
    		if( arrayImg.size() > 0 ){
        		postImgUrl = arrayImg.get(0).toString().replaceAll("\"", "");
        		//System.out.println("대문 이미지 주소 : "+ postImgUrl);
        	}        	
        	if( arrayImg.size() > 1 ){
        		prodImgUrl = arrayImg.get(1).toString().replaceAll("\"", "");        		
        		//System.out.println("상품 이미지 주소 : "+ prodImgUrl );
        	}
    	}
    	
    	// System.out.println("url : "+discussion.getUrl());
		String postUrl = discussion.getUrl();
		String postTitle = discussion.getTitle();
		
		
		post.setSiteDvcd("steemit");
		post.setCategoryDvcd(discussion.getCategory());
		post.setPostDvcd("");
		post.setArrTagStr(arrTagStr);
		post.setPostAuthor(author);
		post.setPostUrl(postUrl);
		post.setPostTitle(postTitle);
		post.setPostImgUrl(postImgUrl);
		Date curDate = new Date();
		post.setRegDttm(curDate);
		post.setModDttm(curDate);
	}
	
	private static String getStringFromData(String targetStr, String[] arrStartStr, String endIdxStr ){
		String returnStr = "";
		try {
			for(int arrIdx = 0; arrIdx < arrStartStr.length && "".equals(returnStr) ; arrIdx++){
				String StartIdxStr = arrStartStr[arrIdx];
				if( targetStr.indexOf(StartIdxStr) > -1 ){
					returnStr = targetStr.substring( targetStr.indexOf(StartIdxStr)+StartIdxStr.length() , targetStr.indexOf(endIdxStr, targetStr.indexOf(StartIdxStr) )).trim();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return returnStr;
	}
	
	private static void chkMarketDataAndMerge( TbPostInfo post, Discussion discussion, TbPostMarketInfo marketInfo ) throws Throwable{
		
		
		
		String postBody = discussion.getBody();
		String newLine = "\n";
		
		// 2. 제목에 [경매], [판매], [이벤트]만 인식한다!
		/*
		String[] arrIsMarketWords = new String[]
				{
						"[경매]"
						,"[판매]"
						,"이벤트"
				};
		
		// 경매, 판매, 이벤트로만 인식한다!
		if( !StringUtil.hasStrings(post.getPostTitle(), arrIsMarketWords, false)){
			return;
		}
		
			
		if( post.getPostTitle().indexOf("[판매]") > -1 ){
			method = 1;
		}else if( post.getPostTitle().indexOf("이벤트") > -1 ){
			method = 3;
		}
		*/
		short method = 2;
		String prodMethod = "";
		try {
			
			String[] arrStartStr = new String[]{
					"판매방식 :"
					,"판매방식:" 					
			};			
			prodMethod = getStringFromData(postBody, arrStartStr, newLine);
			// 상품방식을 인식 못했을 시 리턴!
			if( "".equals(prodMethod)){
				return;
			}
			
			if( prodMethod.indexOf("판매") > -1 ){
				method = 1;
			}else if(prodMethod.indexOf("이벤트") > -1) {
				method = 3;
			}
		} catch (Exception e) {
			System.out.println("상품방식 읽어들이기 : "+e.toString());
		}
		
		/*
		 * 1. 의류
		 * 2. 잡화
		 * 3. 화장품/미용
		 * 4. 디지털/가전(전자)
		 * 5. 식품/음식
		 * 6. 여행/문화
		 * 7. 금융
		 * 0. 기타
		 */
		short category = 0;
		String strCategory = "기타";
		try {
			
			String[] arrStartStr = new String[]{
					"카테고리 :"
					,"카테고리:" 					
			};			
			strCategory = getStringFromData(postBody, arrStartStr, newLine);
			// 상품방식을 인식 못했을 시 리턴!
			if( !"".equals(strCategory)){
				if( strCategory.indexOf("의류") > -1 ){
					category = 1;
				}
				else if( strCategory.indexOf("잡화") > -1 || strCategory.indexOf("악세서리") > -1){
					category = 2;
				}
				else if( strCategory.indexOf("화장품") > -1 || strCategory.indexOf("미용") > -1 ){
					category = 3;
				}
				else if( strCategory.indexOf("디지털") > -1 || strCategory.indexOf("가전") > -1 || strCategory.indexOf("전자") > -1){
					category = 4;
				}
				else if( strCategory.indexOf("식품") > -1 || strCategory.indexOf("음식") > -1 ){
					category = 5;
				}
				else if( strCategory.indexOf("여행") > -1 || strCategory.indexOf("문화") > -1 || strCategory.indexOf("상품권") > -1 ){
					category = 6;
				}
				else if( strCategory.indexOf("생활") > -1 || strCategory.indexOf("건강") > -1 || strCategory.indexOf("용품") > -1 ){
					category = 7;
				}
				else if( strCategory.indexOf("금융") > -1 ){
					category = 99;
				}
				else if( strCategory.indexOf("도서") > -1 || strCategory.indexOf("책") > -1 ){
					category = 8;
				}
				else {
					category = 0;
				}
			}
			
			if( prodMethod.indexOf("판매") > -1 ){
				method = 1;
			}else if(prodMethod.indexOf("이벤트") > -1) {
				method = 3;
			}
		} catch (Exception e) {
			System.out.println("상품방식 읽어들이기 : "+e.toString());
		}
		
		
		String prodName = "";
		
		
		
		// 2. 상품명 읽어들이기 prodName
		try {
			
			String[] arrStartStr = new String[]{
					"상품명 :"
					, "상품 :"
					, "상품:" 
					, "상품명:"					
					, "상품 명:"
					, "상품 명 :"
			};			
			prodName = getStringFromData(postBody, arrStartStr, newLine);
			// 상품명을 인식 못했을 시 리턴!
			if( "".equals(prodName)){
				return;
			}
			if( prodName.indexOf("</") > -1 ){
				prodName = prodName.substring(0, prodName.indexOf("</"));
			}
		} catch (Exception e) {
			System.out.println("상품명 읽어들이기 : "+e.toString());
		}
		
		// 3. 판매가 혹은 시작 가격 읽어들이기 sellAmt
		double sellAmt = 0.0;
		String strSellAmt = "";
		try {
			String[] arrStartStr = new String[]{
					"경매시작가 :"
					,"경매시작가:" 
					,"판매가격 :"
					,"판매가격:"
					,"판매가 :"
					,"판매가:"
					,"경매시작가격 :"
					,"경매시작가격:"
					
			};		
			String endStr = "\n";
			strSellAmt = getStringFromData(postBody, arrStartStr, endStr).toLowerCase();
			
			if( strSellAmt.indexOf("sbd") > -1 ){
				endStr = "sbd";
			}else{
				endStr = "스팀달러";
			}
			
			strSellAmt = strSellAmt.substring(0,strSellAmt.indexOf(endStr) + endStr.length() );
			
			sellAmt = Double.parseDouble(strSellAmt.replaceAll("[^0-9.]", ""));
			// 시작 가격을 인식 못했을 시 리턴!
			
			if( "".equals(strSellAmt)){
				return;
			}
			
		} catch (Exception e) {
			System.out.println("시작 가격 읽어들이기 : "+e.toString());
		}
		
		// 4. 소비자 가격 읽어들이기 oriAmt
		String oriAmt = "";
		try {
			String[] arrStartStr = new String[]{
					"소비자 가격 :"
					,"소비자 가격:" 
					,"소비자가격 :"
					,"소비자가격:"					
			};								
			String endStr = "원";
			oriAmt = getStringFromData(postBody, arrStartStr, endStr).replaceAll(",", "");
			// 시작 가격을 인식 못했을 시 리턴!
			/*
			if( "".equals(oriAmt)){
				return;
			}
			*/
		} catch (Exception e) {
			System.out.println("소비자 가격 읽어들이기 : "+e.toString());
		}
		
		// 5. 적용률 읽어들이기 voteRatio
		String voteRatio = "0.375";
		/*
		try {
			String[] arrStartStr = new String[]{
					"글값 x "
					,"글값 x" 
					,"글값x "
					,"글값x"					
			};								
			String endStr = "(SBD)";
			voteRatio = getStringFromData(postBody, arrStartStr, endStr);
			// 시작 가격을 인식 못했을 시 리턴!
			
			if( "".equals(voteRatio)){
				return;
			}
			
		} catch (Exception e) {
			System.out.println("적용률 읽어들이기 : "+e.toString());
		}
		*/
		
		
		// 6. 호가단위 읽어들이기 actionUnitAmt 
		String auctionUnitAmt = "0.0";
		try {
			String[] arrStartStr = new String[]{
					"호가 단위 :"
					,"호가 단위:" 
					,"호가단위 :"
					,"호가단위:"					
			};								
			String endStr = "SBD";
			auctionUnitAmt = getStringFromData(postBody, arrStartStr, endStr);
			// 시작 가격을 인식 못했을 시 리턴!
			/*
			if( "".equals(actionUnitAmt)){
				return;
			}
			*/
		} catch (Exception e) {
			System.out.println("호가단위 읽어들이기 : "+e.toString());
		}		
		
		// 7. 경매종료일시 읽어들이기 actionUnitAmt 
		String actionEndDtStr = "";		
		Date auctionEndDttm = discussion.getCashoutTime().getDateTimeAsDate();
		try {
			String[] arrStartStr = new String[]{
					"경매종료날짜 :"
					, "경매 종료 날짜 :"
					, "경매종료 날짜 :"									
					, "경매종료일시 :"
					, "경매종료 일시 :"
					, "경매 종료 일시 :"
			};								
			String endStr = "\n";
			actionEndDtStr = getStringFromData(postBody, arrStartStr, endStr).replaceAll(" ", "");			
			
			// 시작 가격을 인식 못했을 시 리턴!
			
			if( !"".equals(actionEndDtStr) && actionEndDtStr.indexOf("페이아웃") == -1 ){
				
				String year = actionEndDtStr.substring(0, 4);				
				String month = actionEndDtStr.substring(actionEndDtStr.indexOf("년") + 1 , actionEndDtStr.indexOf("월") );
				if( month.length() == 1)
					month = "0"+month;
				
				String day = actionEndDtStr.substring(actionEndDtStr.indexOf("월") + 1 , actionEndDtStr.indexOf("일") );
				if( day.length() == 1)
					day = "0"+day;
				
				String hour = actionEndDtStr.substring(actionEndDtStr.indexOf("일") + 1 , actionEndDtStr.indexOf("시") );
				if( hour.length() == 1)
					hour = "0"+hour;
				
				// yyyy-MM-dd HH:mm:ss
				
				auctionEndDttm = DateUtil.getStringToDate(year+month+day+hour, "yyyyMMddhh");
				
				//System.out.println("convert Date : "+auctionEndDttm);
			}else{
				auctionEndDttm = discussion.getCashoutTime().getDateTimeAsDate();
			}
			
		} catch (Throwable e) {
			System.out.println("경매 종료 날짜 읽어들이기 : "+e.toString());
			auctionEndDttm = discussion.getCashoutTime().getDateTimeAsDate();
		}
		
		// 두번째 상품 이미지 가져오기!
		JsonObject postMeta = gson.fromJson(discussion.getJsonMetadata(), JsonObject.class);
					
		String postImgUrl = "";	 
		String prodImgUrl = "";
    	if( postMeta.get("image") != null ){
    		JsonArray arrayImg = postMeta.get("image").getAsJsonArray();
    		if( arrayImg.size() > 0 ){
        		postImgUrl = arrayImg.get(0).toString().replaceAll("\"", "");
        		//System.out.println("대문 이미지 주소 : "+ postImgUrl);
        	}
        	
        	if( arrayImg.size() > 1 ){
        		prodImgUrl = arrayImg.get(1).toString().replaceAll("\"", "");
        		
        	}
    	}
    	Asset pendingPayoutValue = discussion.getPendingPayoutValue();
    	BigDecimal tmpAmt = new BigDecimal(pendingPayoutValue.getAmount());
    	Date curDate = new Date();
    	//tmpAmt.setScale(pendingPayoutValue.getPrecision(), BigDecimal.ROUND_HALF_UP);
    	double voteAmt = tmpAmt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	short status = 1;	// 판매중 1, 종료 8 
    	if( discussion.getTitle().indexOf("종료") > -1 
    		|| discussion.getTitle().indexOf("중단") > -1	
    		|| curDate.getTime() - auctionEndDttm.getTime() > 60000 * 60 // 경매종료시간이 한시간 지나면 상태를 종료로 변경!
    	){
    		status = 8;
    	}
    	
    	long siteId = discussion.getId();
    	String permLink = discussion.getPermlink();
    	double realAmt = 0.0;
    	if( method == 2 ){
    		
    		realAmt = new BigDecimal(sellAmt - ( voteAmt * Double.parseDouble(voteRatio))).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    	}else{
    		realAmt = sellAmt;
    	}
    		
    	
    	// 로그 찍기    	
    	//if( NetUtil.isMyLocal() ){
    	if( false ){
    		System.out.println("siteId : " + siteId );
        	System.out.println("permLink : " + permLink );
        	System.out.println("prodName : " + prodName );
        	System.out.println("method : " + method );
        	System.out.println("category : " + category );
        	System.out.println("status : " + status );
        	
        	System.out.println("actionEndDttm : " + actionEndDtStr );
        	System.out.println("oriAmt : " + oriAmt );
        	System.out.println("actionStartAmt : " + sellAmt );
        	System.out.println("actionUnitAmt : " + auctionUnitAmt );
        	
    		System.out.println("prodImgUrl : " + prodImgUrl );
    		
    		System.out.println("sellAmt : " + sellAmt );
    		System.out.println("voteRatio : " + voteRatio );
    		System.out.println("voteAmt : " + voteAmt );
    		System.out.println("realAmt : " + realAmt );
    	}
    	
		
		
		// 마켓 디비에 저장하기!!!
		TbPostMarketInfo postMarket = new TbPostMarketInfo();
		
		if( post != null){
			postMarket.setPostId(post.getPostId());
		}
		
		postMarket.setSiteId(siteId);
		postMarket.setAuthor( discussion.getAuthor().getAccountName() );
		postMarket.setPermLink(permLink);
		postMarket.setProdName(prodName);
		postMarket.setMethod( method );
		postMarket.setCategory(category);
		postMarket.setStatus(status);
		postMarket.setAuctionEndDttm(auctionEndDttm);
		
		double dbOriAmt = 0.0; 
		
		try {
			dbOriAmt = Double.parseDouble(oriAmt);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}		
		postMarket.setOriAmt( dbOriAmt );
		postMarket.setSellAmt(sellAmt);
		if( !"".equals(auctionUnitAmt)){
			postMarket.setAuctionUnitAmt( Double.parseDouble(auctionUnitAmt) );
		}		
		postMarket.setVoteAmt(voteAmt);
		
		// 보팅금액 할인 비율.
		if( method == 2 ){
			postMarket.setVoteRatio( Double.parseDouble(voteRatio) );
		}
		
		postMarket.setRealAmt(realAmt);
		postMarket.setPostRegDttm( discussion.getCreated().getDateTimeAsDate());
		postMarket.setPostUpdateDttm( discussion.getLastUpdate().getDateTimeAsDate() );
		
		
		
		{
			postMarket.setProdImgUrl(prodImgUrl);
			String cachePostImgUrl = "";
			if( postImgUrl != null && !"".equals(postImgUrl)){
				cachePostImgUrl = CacheImgManager.getImgUrl(postImgUrl);
				postMarket.setCachePostImgUrl(cachePostImgUrl);
			}
		}
		{
			postMarket.setPostImgUrl(postImgUrl);
			String cacheProdImgUrl = "";
			if( prodImgUrl != null && !"".equals(prodImgUrl)){
				cacheProdImgUrl = CacheImgManager.getImgUrl(prodImgUrl);
				postMarket.setCacheProdImgUrl(cacheProdImgUrl);
			}
		}
		
		
		
		
		if( marketInfo != null){
			postMarket.setRegDttm(marketInfo.getRegDttm());
		}else{
			postMarket.setRegDttm(curDate);
		}
		postMarket.setModDttm(curDate);
		
		org.hibernate.Transaction tx = session.getTransaction();
		tx.begin();
		try {
			
			List<Discussion> replies = SteemApi.getContentReplies( postMarket.getAuthor() , permLink);
			//System.out.println("--- replie start");
			
			double lastAuctionAmt = 0.0;
			int replyCnt = 0;
			for( Discussion replie: replies){
				// 리플 단 사람이 판매자인 경우 패스
				if( replie.getAuthor().equals( postMarket.getAuthor() ) ){
					continue;
				}				
				if(
						replie.getAuthor().equals("krwhale") 
						|| replie.getAuthor().equals("randowhale")
				){
					continue;
				}
				String body = replie.getBody();
				
				if( body.length() > 512 ){
					continue;
				}
				
				/*
				if( body.indexOf("스달") == -1 
						&& body.indexOf("달러") == -1
						&& body.indexOf("SBD") == -1
						&& body.indexOf("sbd") == -1
				){
					continue;
				}
				*/
				if( body.indexOf("http") > -1
				){
					continue;
				}
					
				
				double auctionAmt = getAuctionAmt(body);
				if( auctionAmt == 0.0 ){
					continue;
				}
				
				lastAuctionAmt = lastAuctionAmt > auctionAmt ? lastAuctionAmt : auctionAmt;
				//System.out.println(replie.getBody());
				TbPostMarketReply reply = new TbPostMarketReply();
				reply.setReplyId(replie.getId());
				if( post != null){
					reply.setPostId(post.getPostId());
				}
								
				reply.setAuthor(replie.getAuthor().getAccountName());			
				reply.setAuctionAmt(auctionAmt);
				reply.setComment(body);
				reply.setAuctionAmt(auctionAmt);
				reply.setReplyRegDttm(replie.getCreated().getDateTimeAsDate());
				
				reply.setRegDttm(curDate);
				reply.setModDttm(curDate);
				session.merge(reply);
				replyCnt++;
			}
			
			if( lastAuctionAmt == 0.0 ){
				lastAuctionAmt = sellAmt;
			}
			
			postMarket.setLastSellAmt(lastAuctionAmt);
			if( method == 2 ){
	    		realAmt = new BigDecimal(lastAuctionAmt - ( voteAmt * Double.parseDouble(voteRatio))).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
	    	}else{
	    		realAmt = lastAuctionAmt;
	    	}
			postMarket.setReplyCnt(replyCnt);
			
			
			
			//postMarket.setCachePostImgUrl("");
			//System.out.println("--- replie end");
			
			Object result = session.merge(postMarket);
			
			tx.commit();
		} catch (Exception e) {
			if( tx != null ) tx.rollback();
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
		// 입찰가격 리플에서 정보 얻어오는 로직 만들 것 !!!
		//postMarket.setLastSellAmt(lastSellAmt);
	}
	
	private static double getAuctionAmt(String str){
		if( str == null || str.length() < 2 ){
			return 0.0;
		}
		str = str.replaceAll("[^0-9.]", "");
		while( str.length() > 1 && str.substring(0, 1).equals(".") ){
			str = str.substring(1, str.length());
		}
		while( str.length() > 1 && str.substring(str.length()-1, str.length()).equals(".")){
			str = str.substring(0, str.length()-1);
		}
		double rtnDouble = 0.0;
		try {
			rtnDouble = Double.parseDouble(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rtnDouble;
	}
	
	private static void marketRefresh() throws Throwable{
		List<TbPostMarketInfo> marketList =  session.createQuery( "from TbPostMarketInfo where status <> 8 ").list();
		
		for( TbPostMarketInfo market : marketList ){
			String author = market.getAuthor();
			String permLink = market.getPermLink();
			
			Discussion discussion = SteemApi.getContent(author, permLink);
			TbPostInfo post = new TbPostInfo();
			post.setPostId(market.getPostId());
			chkMarketDataAndMerge(post, discussion, market);
			
		}
		
	}
	
	public static void executeSave() throws Throwable{
		org.hibernate.Transaction tx = null;
		
		try{
			tx = session.getTransaction();
			
			List<Discussion> arrayPost = SteemApi.getDiscussionBy("kr", 50);
			//List<Discussion> arrayPost = SteemApi.getDiscussionBy("kr-seller", 20);
			
			// 전체 조회 수 만큼 반복!!
			for(int postIdx = 0; postIdx < arrayPost.size();postIdx++){
				
				Discussion discussion = arrayPost.get(postIdx);
				
				TbPostInfo post = new TbPostInfo();
				
				setPostFromDiscussion(post, discussion);
				
				List postList =  session.createQuery( "from TbPostInfo where POST_URL = :postUrl ").setParameter("postUrl", post.getPostUrl()).list();
				TbPostInfo oldPost = postList.size() > 0 ? (TbPostInfo) postList.get(0) : null;
				if( oldPost != null ){
					System.out.println("Database Post exists!! continue.. [" + oldPost.getPostTitle()+ "]" );
					// kr-market의 포스팅인 경우 마켓 인식 로직 수행!!
					post.setPostId(oldPost.getPostId());
					if( post.getArrTagStr().indexOf("kr-market") > -1 ){
						if( discussion.getBody().indexOf("판매방식 :") > -1 ){
							chkMarketDataAndMerge(post, discussion, null);
						}
					}
					continue;	// 겹치는 포스팅이 있다면 그 상태로 브레이끼.
				}
				// old not exists save and send alarm
				else{
					
					String targetPostUrl = STEEM_URL + post.getPostUrl();	// 포스팅 본문 주소!
					//System.out.println("targetPostUrl : " + targetPostUrl);
					
					tx.begin();
					session.save(post);
					tx.commit();
					
					System.out.println("[New Post "+post.getPostId()+"] [created] [" + post.getPostTitle() + "]" );
					
					// 알람 대상을 조회하여 알람을 발송한다!!
					{
						List webPushList = session.createQuery("from TbWebPushM ").list();
						
						System.out.println("[webPushList]" + webPushList.size() );
						
						Gson gson = new Gson();
						for(int pushIdx = 0; pushIdx < webPushList.size();pushIdx++){
							TbWebPushM pushTarget = (TbWebPushM) webPushList.get(pushIdx);
							
							try{
								JsonObject settingJson = gson.fromJson(pushTarget.getAlarmSettingStr(), JsonObject.class);
								if( settingJson == null ) continue;
								
								checkAndSendAlarm(settingJson, post, pushTarget );
								
							}catch(Throwable e){
								System.out.println("sendAlarm.Throwable");
								e.printStackTrace();
								continue;
							}
						}
					}
					
					// kr-market의 포스팅인 경우 마켓 인식 로직 수행!!
					if( post.getArrTagStr().indexOf("kr-market") > -1 ){
						if( discussion.getBody().indexOf("판매방식 :") > -1 ){
							chkMarketDataAndMerge(post, discussion, null);
						}
					}
					
					//System.out.println("save post : " + StringUtil.getObjToStr(post) );
					
					
					
				}
			}
			
		}catch(Throwable e){
			if( tx != null && tx.getStatus() != TransactionStatus.COMMITTED ){
				tx.rollback();
			}
			e.printStackTrace();
			throw e;
		}finally {
			
		}
	}
	
	
	
	@Override
	protected void finalize() throws Throwable {
		HibernateCfg.closeSession();
		super.finalize();
	}
	private static void checkAndSendAlarm(JsonObject settingJson, TbPostInfo post, TbWebPushM pushTarget) throws Throwable{
		
		JsonElement je = settingJson.get("steemit");
		if( je == null ) return;
		
		JsonObject steemitJson = je.getAsJsonObject();
		
		if( "true".equals(JsonUtil.get(steemitJson, "alarmFlag")) ){
			
			JsonArray steemit_item_list = steemitJson.get("item_list").getAsJsonArray();
			
			for(int item_idx = 0 ; item_idx < steemit_item_list.size(); item_idx++ ){
				JsonObject steemit_item = steemit_item_list.get(item_idx).getAsJsonObject();
				if( !"true".equals( JsonUtil.get(steemit_item,"alamFlag") ) ){
					continue;
				}else{
					if( "user".equals( JsonUtil.get(steemit_item,"dvnm") )){
						if( post.getPostAuthor().equals( JsonUtil.get(steemit_item,"itemValue").toString().replaceAll("@", "")) ){
							sendNewSteemitAlarm(pushTarget, post, "user");
							continue;
						}
					}else if( "tag".equals( JsonUtil.get(steemit_item,"dvnm")) ){
						if( post.getArrTagStr().indexOf( JsonUtil.get(steemit_item,"itemValue").toString()) > -1 ){
							sendNewSteemitAlarm(pushTarget, post, "tag");
							continue;
						}
					}
				}
			}
		}
		
	}
	
	private static void sendNewSteemitAlarm(TbWebPushM pushTarget, TbPostInfo post, String dvnm ) throws Throwable{
		System.out.println("[PUSH.sendAlarm]" + " : " + pushTarget.getAuth() + " : " + post.getPostTitle() );
		JsonObject jsonObject = new JsonObject();
		if( dvnm.equals("user")){
			jsonObject.addProperty("title", post.getSiteDvcd() + " - @" + post.getPostAuthor() );
		}else{
			jsonObject.addProperty("title", post.getSiteDvcd() + " - " + post.getCategoryDvcd() );
		}
		
        jsonObject.addProperty("body", post.getPostTitle());
        jsonObject.addProperty("url", "https://steemit.com" + post.getPostUrl());
        jsonObject.addProperty("img", post.getPostImgUrl());
		WebPushAPI.PushChrome(pushTarget.getEndPoint(), pushTarget.getPublicKey(), pushTarget.getAuth(), jsonObject);
	}
}
