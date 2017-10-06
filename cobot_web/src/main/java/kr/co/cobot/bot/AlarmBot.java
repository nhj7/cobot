package kr.co.cobot.bot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

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
import nhj.util.StringUtil;

public class AlarmBot implements Runnable {

	public static void main(String[] args) {		
		init();	
		
	}
	
	
	private static List<JsonObject> WEB_PUSH_LIST = new ArrayList();
	
	
	public static void init(){
		new Thread( new AlarmBot() ).start();
	}
	
	public static void newCheckAndSendAlarm( TbPostInfo post ) throws Throwable{
		
		for(int i =0; i < WEB_PUSH_LIST.size();i++){
			JsonObject alarmJson = WEB_PUSH_LIST.get(i);
			JsonObject setting = alarmJson.get("setting").getAsJsonObject();
			JsonElement steemit = setting.get("steemit");			
			if( steemit == null ) continue;
			
			
			if( steemit != null ){
				JsonObject steemitJson = steemit.getAsJsonObject();				
				if( "true".equals(JsonUtil.get(steemitJson, "alarmFlag")) ){					
					JsonArray steemit_item_list = steemitJson.get("item_list").getAsJsonArray();					
					for(int item_idx = 0 ; item_idx < steemit_item_list.size(); item_idx++ ){
						JsonObject steemit_item = steemit_item_list.get(item_idx).getAsJsonObject();
						if( !"true".equals( JsonUtil.get(steemit_item,"alamFlag") ) ){
							continue;
						}else{
							if( "user".equals( JsonUtil.get(steemit_item,"dvnm") )){
								if( post.getPostAuthor().equals( JsonUtil.get(steemit_item,"itemValue").toString().replaceAll("@", "")) ){
									sendNewSteemitAlarm(alarmJson, post, "user");
									continue;
								}
							}else if( "tag".equals( JsonUtil.get(steemit_item,"dvnm")) ){
								if( post.getArrTagStr().indexOf( JsonUtil.get(steemit_item,"itemValue").toString()) > -1 ){
									sendNewSteemitAlarm(alarmJson, post, "tag");
									continue;
								}
							}
						}
					}
				}
			}
			
			
			
		}
		
		
		
	}
	
	static long MIN_ONE = 60000;
	public static void auctionCheckAndSendAlarm( TbPostMarketInfo market ) throws Throwable{
		
		String[] strTerm = new String[]{
			"90", "60", "30", "20", "10", "5"	
		};
				
		long[] chkTermMin = new long[]{
			90 * MIN_ONE, 60 * MIN_ONE, 30 * MIN_ONE, 20 * MIN_ONE, 10 * MIN_ONE, 5	* MIN_ONE
		};
		
		
		if( StringUtil.isBlank(market.getAlarmInfo()) || market.getAlarmInfo().indexOf( "new" ) == -1 ){
			for(int pushIdx =0; pushIdx < WEB_PUSH_LIST.size();pushIdx++){
				
				JsonObject alarmJson = WEB_PUSH_LIST.get(pushIdx);
				JsonObject setting = alarmJson.get("setting").getAsJsonObject();
				JsonElement martJe = setting.get("mart");
				if( martJe == null ) continue;
				
				JsonObject mart = martJe.getAsJsonObject();
				String newChk = JsonUtil.get(mart,"newChk");
								
				// all이 아닌 경우 다음!
				if( !"true".equals(newChk) ){
					continue;
				}
				sendAuctionSteemitAlarm(alarmJson, market , "상품이 등록되었습니다. ");				
			}			
			
			if( StringUtil.isBlank(market.getAlarmInfo()) ){
				market.setAlarmInfo("new");
			}else{
				market.setAlarmInfo(market.getAlarmInfo() + ",new");
			}
		}
		
			
		
		Date curDt = new Date();
		
		// 경매종료시간 < 현재시간이면 리턴;
		if( market.getAuctionEndDttm().getTime() < curDt.getTime() ){
			return;
		}
		
		// 경매종료시간 - 현재시간 > 알람발송최대시간이면 return;
		long auctionTerm = market.getAuctionEndDttm().getTime() - curDt.getTime();
		if( auctionTerm > chkTermMin[0] * MIN_ONE  ){
			return;
		}
		// 종료된 경매건이면 리턴;
		if( market.getStatus() == 8 ){
			return;
		}
		
		boolean chkTerm = true;
		for(int i = 0; i < strTerm.length;i++){
			
			if( market.getAlarmInfo().indexOf(strTerm[i]) == -1 ){
				
				if( auctionTerm > chkTermMin[i]) continue;
				
				chkTerm = false;
				for(int pushIdx =0; pushIdx < WEB_PUSH_LIST.size();pushIdx++){
					
					JsonObject alarmJson = WEB_PUSH_LIST.get(pushIdx);
					JsonObject setting = alarmJson.get("setting").getAsJsonObject();
					JsonElement martJe = setting.get("mart");
					if( martJe == null ) continue;
					
					JsonObject mart = martJe.getAsJsonObject();
					
					
					
					String auctionChk = JsonUtil.get(mart,"auctionChk");
					String alarmTimer = JsonUtil.get(mart,"alarmTimer");
					
					// no 인 경우 다음!
					if( "no".equals(auctionChk) ){
						continue;
					}
					
					// 시간이 해당 안되는 경우 다음!
					if( !strTerm[i].equals(alarmTimer) ){
						continue;
					}
						
					if( StringUtil.isBlank(auctionChk) || StringUtil.isBlank(alarmTimer) ){
						continue;
					}
					
					String steemitId = JsonUtil.get(mart,"steemitId");
					
					if( !"all".equals(auctionChk)){
						if( StringUtil.isBlank(steemitId)){
							continue;
						}
					}else{
						sendAuctionSteemitAlarm(alarmJson, market , "경매가 "+( auctionTerm / 60 / 1000 )+"분 남았습니다. ");
						
						if( StringUtil.isBlank(market.getAlarmInfo()) ){
							market.setAlarmInfo(strTerm[i]);
						}else{
							market.setAlarmInfo(market.getAlarmInfo() + ","+strTerm[i]);
						}
						
					}
					
					
				}
				
			}
		}
		
		
		
		
	}
	
	
	private static void sendAuctionSteemitAlarm(JsonObject pushTarget, TbPostMarketInfo market, String body) throws Throwable{
		System.out.println("[PUSH.sendAuctionAlarm]" + " : " + JsonUtil.get(pushTarget, "auth") + " : " + market.getProdName() );
		
		Date curDt = new Date();		
		// 경매종료시간 < 현재시간이면 리턴;
		long auctionTerm = market.getAuctionEndDttm().getTime() - curDt.getTime();
		long minTerm = auctionTerm / MIN_ONE;
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("title", market.getProdName());
		
		
        jsonObject.addProperty("body", body);
        jsonObject.addProperty("url", "https://steemit.com/@" + market.getAuthor() + "/" + market.getPermLink());
        
        
        jsonObject.addProperty("img", market.getProdImgUrl() );
		WebPushAPI.PushChrome( 
				JsonUtil.get(pushTarget, "endPoint")
				, JsonUtil.get(pushTarget, "publicKey") 
				, JsonUtil.get(pushTarget, "auth")
				, jsonObject
		);
	}
	
	
	private static void sendNewSteemitAlarm(JsonObject pushTarget, TbPostInfo post, String dvnm ) throws Throwable{
		System.out.println("[PUSH.sendAlarm]" + " : " + JsonUtil.get(pushTarget, "auth") + " : " + post.getPostTitle() );
		JsonObject jsonObject = new JsonObject();
		if( dvnm.equals("user")){
			jsonObject.addProperty("title", post.getSiteDvcd() + " - @" + post.getPostAuthor() );
		}else{
			jsonObject.addProperty("title", post.getSiteDvcd() + " - " + post.getCategoryDvcd() );
		}
		
        jsonObject.addProperty("body", post.getPostTitle());
        jsonObject.addProperty("url", "https://steemit.com" + post.getPostUrl());
        jsonObject.addProperty("img", post.getPostImgUrl());
		WebPushAPI.PushChrome( 
				JsonUtil.get(pushTarget, "endPoint")
				, JsonUtil.get(pushTarget, "publicKey") 
				, JsonUtil.get(pushTarget, "auth")
				, jsonObject
		);
	}
	
	
	long PUSH_REFRESH_TERM = 15000;
	
	@Override
	public void run() {	
		Gson gson = new Gson();
		Session session = HibernateCfg.getCurrentSession();
		while(true){			
			try {
				List webPushList = session.createQuery("from TbWebPushM ").list();
				
				List<JsonObject> NEW_WEB_PUSH_LIST = new ArrayList();
				for(int i = 0; i < webPushList.size();i++){
					
					TbWebPushM pushTarget = (TbWebPushM) webPushList.get(i);
						
					try{
						
						//JsonObject alarmJson = gson.fromJson(gson.toJson(pushTarget), JsonObject.class);
						JsonObject alarmJson = JsonUtil.getJsonObject(pushTarget);
						
						alarmJson.remove("alarmSettingStr");
						
						JsonObject settingJson = JsonUtil.getJsonObject(pushTarget.getAlarmSettingStr());
						alarmJson.add("setting", settingJson);
						
						
						NEW_WEB_PUSH_LIST.add(alarmJson);						
					}catch(Throwable e){
						System.out.println("sendAlarm.Throwable");
						e.printStackTrace();
						continue;
					}					
				}
				
				WEB_PUSH_LIST = NEW_WEB_PUSH_LIST;
				
				System.out.println("[WEB_PUSH_LIST]" + WEB_PUSH_LIST.size() );
				Thread.sleep(PUSH_REFRESH_TERM);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(PUSH_REFRESH_TERM);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}
