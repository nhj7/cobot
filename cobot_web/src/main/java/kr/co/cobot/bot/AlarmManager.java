package kr.co.cobot.bot;

import java.util.Date;
import java.util.List;


import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbWebPushM;
import kr.co.cobot.entity.TbWebPushPost;
import kr.co.cobot.entity.TbWebPushPostId;
import nhj.api.WebPushAPI;
import nhj.util.URLUtil;

public class AlarmManager implements Runnable {
	
	private static String STEEM_URL = "https://steemit.com/:steemDvcd/coinkorea";
	private static String[] ARRAY_STEEM_DVCD = new String[]{
		"created", "hot", "trending", "promoted"
	};
	
	private static String STEEM_IMG_START_IDX_STR = ":url(";
	
	public static void main(String[] args) throws Throwable {
		AlarmManager.init();
	}
	
	private String steemDvcd = "";
	
	public AlarmManager( String steemDvcd ){
		this.steemDvcd = steemDvcd;
		
	}
	
	
	
	public static void init(){
		
		for(int i = 0; i < ARRAY_STEEM_DVCD.length;i++){
			AlarmManager am = new AlarmManager(ARRAY_STEEM_DVCD[i]);
			new Thread(am).start();
		}
	}
	
	
	private static int TERM_STEEM_POST_CHECK = 60000 * 5;	// 5분 주기로 새 포스팅 체크
	public void run(){
		while(true){
			try {
				sendAlarm(steemDvcd);
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
	
	public static void sendAlarm( String steemDvcd) throws Throwable{
		String steemUrl = STEEM_URL.replaceAll(":steemDvcd", steemDvcd);
		
		String html = URLUtil.htmlToString(steemUrl, "UTF-8" );
		//System.out.println("html : " + html);
		
		Document dom = Jsoup.parse(html);
		Elements arrayElement = dom.select(".PostSummary");
		
		Session session = HibernateCfg.getCurrentSession();
		org.hibernate.Transaction tx = session.getTransaction();
		tx.begin();
		for(int i = 0; i < arrayElement.size();i++){
			
			Element article = arrayElement.get(i);
			
			Elements arrayTitle = article.select(".PostSummary__header.show-for-small-only");
			//System.out.println("arrayTitle : " + arrayTitle + ", size : " + arrayTitle.size() );
			
			Elements arrayImage = article.select(".PostSummary__image");
			
			
			Element titleElement = arrayTitle.get(0);
			String postUrl = titleElement.select("a").attr("href");
			String postTitle = titleElement.text();			
			//System.out.println("postUrl : " + postUrl );
			//System.out.println("postTitle : " + postTitle );
			
			String postImgUrl = "";
			if( arrayImage.size() > 0 ){
				String style = arrayImage.get(0).attr("style");
				postImgUrl = style.substring( style.indexOf(STEEM_IMG_START_IDX_STR)+STEEM_IMG_START_IDX_STR.length() , style.indexOf(");"));
				//System.out.println("image : " + postImgUrl );
			}
			
			TbWebPushPostId id = new TbWebPushPostId();
			id.setSiteDvcd("steemit");
			id.setCategoryDvcd("coinkorea");
			id.setPostDvcd(steemDvcd);
			id.setTagDvcd("");
			id.setPostUrl(postUrl);
			
			TbWebPushPost post = new TbWebPushPost();
			post.setId(id);
			post.setPostTitle(postTitle);
			post.setPostImgUrl(postImgUrl);
			Date curDate = new Date();
			post.setRegDttm(curDate);
			post.setModDttm(curDate);
			
			TbWebPushPost oldPost = session.get(TbWebPushPost.class, id);
			
			if( oldPost != null ){
				//System.out.println("oldPost exists!! continue.." + oldPost );
				continue;
			}
			// old not exists save and send alarm
			else{
				
				session.save(post);
				
				System.out.println("[New Post] Continue Push Chrome ["+steemDvcd+"] [" + post.getPostTitle() + "]" );
				
				List webPushList = session.createQuery("from TbWebPushM where steem_dvcd = :steemDvcd ").setParameter("steemDvcd", steemDvcd).list();
				
				System.out.println("[webPushList]" + webPushList.size() );
				
				for(int pushIdx = 0; pushIdx < webPushList.size();pushIdx++){
					TbWebPushM pushM = (TbWebPushM) webPushList.get(pushIdx);
					JsonObject jsonObject = new JsonObject();
			        jsonObject.addProperty("body", "Coinkorea -" + post.getPostTitle() );
			        jsonObject.addProperty("url", "https://steemit.com" + post.getId().getPostUrl());
			        jsonObject.addProperty("img", post.getPostImgUrl());
					WebPushAPI.PushChrome(pushM.getEndPoint(), pushM.getPublicKey(), pushM.getAuth(), jsonObject);
				}
			}
		}
		
		tx.commit();
		HibernateCfg.closeSession();
	}
}
