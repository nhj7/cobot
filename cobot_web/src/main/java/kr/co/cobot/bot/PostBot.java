package kr.co.cobot.bot;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbPostInfo;
import kr.co.cobot.entity.TbPostMarketInfo;
import kr.co.cobot.entity.TbWebPushM;
import nhj.api.WebPushAPI;
import nhj.util.JsonUtil;
import nhj.util.NetUtil;
import nhj.util.StringUtil;
import nhj.util.URLUtil;

public class PostBot implements Runnable {
	
	private static String STEEM_URL = "https://steemit.com";
	
	
	
	
	private static String STEEM_IMG_START_IDX_STR = ":url(";
	
	
	
	public static void main(String[] args) throws Throwable {
		PostBot.init();
	}
	
	public PostBot(){		
		
	}
	
	
	
	public static void init(){
		
		PostBot am = new PostBot();
		new Thread(am).start();		
	}
	
	
	private static int TERM_STEEM_POST_CHECK = 12000;	// 12초 주기로 새 포스팅 체크
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
				
				for(int i = 0; i < ARRAY_STEEM_DVCD.length ; i++){
					executeSavePost(ARRAY_STEEM_DVCD[i]);
					Thread.sleep(TERM_STEEM_POST_CHECK);
				}
				//Thread.sleep(TERM_STEEM_POST_CHECK);
				
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
	
	private static long STEEMIT_POST_SCRAP_TERM = 6000;
	public static void executeSavePost( String steemDvcd) throws Throwable{
		org.hibernate.Transaction tx = null;
		
		try{
			String steemUrl = STEEM_URL+"/"+steemDvcd+"/kr";			
			if( NetUtil.isMyLocal() ){
				steemUrl = STEEM_URL+"/@seller/";
			}
			
			
			// kr 태그 전체 조회
			String html = URLUtil.htmlToString(steemUrl, "UTF-8" );
			
			
			//System.out.println("html : " + html);
			
			Document dom = Jsoup.parse(html);
			Elements arrayElement = dom.select(".PostSummary");
			
			Session session = HibernateCfg.getCurrentSession();
			tx = session.getTransaction();
			
			System.out.println("STEEMIT_POST_SCRAP_TERM Wait " + STEEMIT_POST_SCRAP_TERM + "ms");
			Thread.sleep(STEEMIT_POST_SCRAP_TERM);
			
			// 전체 조회 수 만큼 반복!!
			for(int postIdx = 0; postIdx < arrayElement.size();postIdx++){
				
				tx.begin();
				
				Element article = arrayElement.get(postIdx);
				
				String author = article.select(".author").get(0).select("a").select("strong").text();
				
				Elements arrayTitle = article.select(".PostSummary__header.show-for-small-only");
				Element titleElement = arrayTitle.get(0);
				String postUrl = titleElement.select("a").attr("href");
				String postTitle = titleElement.text();			
				
				//System.out.println("arrayTitle : " + arrayTitle + ", size : " + arrayTitle.size() );
				
				Elements arrayBody = article.select(".PostSummary__body.entry-content");
				Element bodyElement = arrayBody.get(0);
				String postBodyUrl = bodyElement.select("a").attr("href");
				String strPostBody = bodyElement.text();
				
				
				Elements arrayImage = article.select(".PostSummary__image");
				
				//System.out.println("postUrl : " + postUrl );
				//System.out.println("postTitle : " + postTitle );
				
				String postImgUrl = "";
				if( arrayImage.size() > 0 ){
					String style = arrayImage.get(0).attr("style");
					postImgUrl = style.substring( style.indexOf(STEEM_IMG_START_IDX_STR)+STEEM_IMG_START_IDX_STR.length() , style.indexOf(");"));
					//System.out.println("image : " + postImgUrl );
				}
				
				TbPostInfo post = new TbPostInfo();
				post.setSiteDvcd("steemit");
				post.setCategoryDvcd("");
				post.setPostDvcd("");
				post.setPostAuthor(author);
				post.setPostUrl(postUrl);
				post.setPostTitle(postTitle);
				post.setPostBody(strPostBody);
				post.setPostImgUrl(postImgUrl);
				Date curDate = new Date();
				post.setRegDttm(curDate);
				post.setModDttm(curDate);
				
				List postList =  session.createQuery( "from TbPostInfo where POST_URL = :postUrl ").setParameter("postUrl", postUrl).list();
				TbPostInfo oldPost = postList.size() > 0 ? (TbPostInfo) postList.get(0) : null;
				if( oldPost != null ){
					System.out.println("oldPost exists!! continue.. [" + oldPost.getPostTitle()+ "]" );
					break;	// 겹치는 포스팅이 있다면 그 상태로 브레이끼.
				}
				// old not exists save and send alarm
				else{
					
					String targetPostUrl = STEEM_URL + postUrl;	// 포스팅 본문 주소!
					//System.out.println("targetPostUrl : " + targetPostUrl);
					
					// 본문 읽어들임.					
					html = URLUtil.htmlToString(targetPostUrl, "UTF-8" );
					//System.out.println("html : " + html);
					
					dom = Jsoup.parse(html);
					Elements arrElements = dom.select(".TagList__horizontal").select("a");
					
					
					//JsonArray tagArray = new JsonArray();
					String arrTagStr = "";
					for(int tagIdx = 0; tagIdx < arrElements.size();tagIdx++ ){
						String tagName = arrElements.get(tagIdx).text();
						if( tagIdx == 0 ){
							post.setCategoryDvcd(tagName);	// 첫번째 태그가 마스터 태그가 된다!!
							arrTagStr = tagName;
						}else{
							arrTagStr += ","+tagName;
						}
						//tagArray.add(tagName);
						//System.out.println("tagName : " + tagName);
					}
					
					post.setArrTagStr(arrTagStr);
					session.save(post);
					tx.commit();
					
					System.out.println("[New Post "+post.getPostId()+"] ["+steemDvcd+"] [" + post.getPostTitle() + "]" );
					
					// kr-market의 포스팅인 경우 마켓 인식 로직 수행!!
					if( arrTagStr.indexOf("kr-market") > -1 ){
						
						// 1. 마켓 글인지 판단한다.
						// 제목에 [경매], [판매], [이벤트]만 인식한다!
						String[] arrIsMarketWords = new String[]
								{
										"[경매]"
										,"[판매]"
										,"[이벤트]"
								};
						
						// 다음 글이 제목에 인식되면 마켓 글로 인식 파싱을 시작한다!!!
						if( StringUtil.hasStrings(post.getPostTitle(), arrIsMarketWords, false) ){
							String prodImgUrl = "";
							String prodName = "";
							
							Element postBody = dom.select(".PostFull__body").get(0);
							
							// 1. 상품 이미지 경로 읽어들이기 prodImgUrl
							try {
								Elements imgList = postBody.select("img");
								if( imgList.size() >= 2){
									// 두번째 이미지를 상품 이미지로 간주한다!!								
									prodImgUrl = imgList.get(1).attr("src");
									
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
							
							String bodyHtml = postBody.toString();
							
							// 2. 상품명 읽어들이기 prodName
							try {
								
								String[] arrStartStr = new String[]{
										"상품 :"
										,"상품:" 
										,"상품명:"
										,"상품명 :"
										,"상품 명:"
										,"상품 명 :"
								};
								for(int arrIdx = 0; arrIdx < arrStartStr.length && "".equals(prodName) ; arrIdx++){
									String prodStartStr = arrStartStr[arrIdx];
									if( bodyHtml.indexOf(prodStartStr) > -1 ){
										prodName = bodyHtml.substring( bodyHtml.indexOf(prodStartStr)+prodStartStr.length() , bodyHtml.indexOf("</", bodyHtml.indexOf(prodStartStr) )).trim();								
										
									}
								}
							} catch (Exception e) {
								System.out.println("상품명 읽어들이기 : "+e.toString());
							}
							
							// 3. 시작 가격 읽어들이기 actionStartAmt
							String actionStartAmt = "";
							try {
								String[] arrStartStr = new String[]{
										"시작 가격 :"
										,"시작 가격:" 
										,"시작가격:"
										,"시작가격 :"
										,"시작가:"
										,"시작가 :"
								};								
								String endStr = "SBD";
								for(int arrIdx = 0; arrIdx < arrStartStr.length && "".equals(actionStartAmt) ; arrIdx++){
									String prodStartStr = arrStartStr[arrIdx];
									if( bodyHtml.indexOf(prodStartStr) > -1 ){
										actionStartAmt = bodyHtml.substring( bodyHtml.indexOf(prodStartStr)+prodStartStr.length() , bodyHtml.indexOf(endStr, bodyHtml.indexOf(prodStartStr) )).trim();								
										
									}
								}
							} catch (Exception e) {
								System.out.println("시작 가격 읽어들이기 : "+e.toString());
							}
							
							
							// 로그 찍기
							System.out.println("prodImgUrl : " + prodImgUrl );
							System.out.println("prodName : " + prodName );
							System.out.println("actionStartAmt : " + actionStartAmt );
							
							// 마켓 디비에 저장하기!!!							
							TbPostMarketInfo postMarket = new TbPostMarketInfo();
							
							
						} // if( StringUtil.hasStrings(post.getPostTitle(), arrIsMarketWords, false) ){
					}
					
					//System.out.println("save post : " + StringUtil.getObjToStr(post) );
					
					
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
								// send steemit alarm
								
								
							}catch(Throwable e){
								System.out.println("sendAlarm.Throwable");
								e.printStackTrace();
								continue;
							}
						}
					}
					
				}
				
				
				System.out.println("STEEMIT_POST_SCRAP_TERM Wait " + STEEMIT_POST_SCRAP_TERM + "ms");
				Thread.sleep(STEEMIT_POST_SCRAP_TERM);
			}
			
		}catch(Throwable e){
			if( tx != null ) tx.rollback();
			e.printStackTrace();
			throw e;
		}finally {
			HibernateCfg.closeSession();
		}
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
							sendAlarm(pushTarget, post, "user");
							continue;
						}
					}else if( "tag".equals( JsonUtil.get(steemit_item,"dvnm")) ){
						if( post.getArrTagStr().indexOf( JsonUtil.get(steemit_item,"itemValue").toString()) > -1 ){
							sendAlarm(pushTarget, post, "tag");
							continue;
						}
					}
				}
			}
		}
		
	}
	
	private static void sendAlarm(TbWebPushM pushTarget, TbPostInfo post, String dvnm ) throws Throwable{
		System.out.println("[PUSH.sendAlarm]" + " : " + pushTarget.getAuth() + " : " + post.getPostTitle() );
		JsonObject jsonObject = new JsonObject();
		if( dvnm.equals("user")){
			jsonObject.addProperty("title", post.getSiteDvcd() + " - @" + post.getPostAuthor() );
		}else{
			jsonObject.addProperty("title", post.getSiteDvcd() + " - " + post.getCategoryDvcd() );
		}
		
        jsonObject.addProperty("body", post.getPostTitle() + "\n" + post.getPostBody() );
        jsonObject.addProperty("url", "https://steemit.com" + post.getPostUrl());
        jsonObject.addProperty("img", post.getPostImgUrl());
		WebPushAPI.PushChrome(pushTarget.getEndPoint(), pushTarget.getPublicKey(), pushTarget.getAuth(), jsonObject);
	}
}
