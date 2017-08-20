package kr.co.cobot.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.cobot.ctrl.WebSocketCon;
import nhj.api.BithumbAPI;
import nhj.api.BittrexAPI;
import nhj.api.CoinoneAPI;
import nhj.api.HTMLParsingAPI;
import nhj.api.KorbitAPI;
import nhj.api.PoloniexAPI;
import nhj.util.JsonUtil;

public class TickManager implements Runnable {

	//
	static{
		CoinoneAPI.init();	// WS Socket init
		KorbitAPI.init();	// Web scrapping init
		BithumbAPI.init();
		HTMLParsingAPI.init();	
	}
	
	@Override
	public synchronized void run() {
		
		
		while(true){
			try {
				
				Map newCoinMap = new HashMap();
				
				// PoloniexAPI
				{
					PoloniexAPI poloniex = new PoloniexAPI("", "");
					List poloList = poloniex.returnTicker();
					newCoinMap.put("eid_" + 1, poloList);
				}
				
				// BithumbAPI
				{
					try{
						newCoinMap.put("eid_" + 2, DATA.getBitthumb_LIST());
					}catch(Throwable e){
						e.printStackTrace();
						newCoinMap.put("eid_" + 2, new ArrayList());
						
					}
					
				}
				
				// CoinoneAPI
				{
					List coinList = CoinoneAPI.returnTicker();
					newCoinMap.put("eid_" + 3, coinList);
				}
				
				// KorbitAPI
				{
					List korbitList = KorbitAPI.returnTicker();
					newCoinMap.put("eid_" + 4, korbitList);
				}
				
				// KorbitAPI
				{
					List list = BittrexAPI.returnTicker();
					newCoinMap.put("eid_" + 5, list);
				}
				
				String per_krw = "1145";
				{
					per_krw = CoinoneAPI.getPerKrw();
					newCoinMap.put("per_krw" , per_krw);
					
					DATA.USD_KRW = per_krw;
				}
				
				Map oldCoinMap = DATA.getCoinInfo();
				
				DATA.setCoinInfo(newCoinMap);
				
				Map chCoinMap = new HashMap();
				
				for(Iterator it = oldCoinMap.keySet().iterator();it.hasNext(); ){
					String key = it.next().toString();
					if( "per_krw".equals(key) ) {
						chCoinMap.put("per_krw" , per_krw);
						continue;
					}
					
					List<Map> oldCoinList = (List)oldCoinMap.get(key);
					List<Map> newCoinList = (List)newCoinMap.get(key);					
					List<Map> chCoinList = new ArrayList();					
					chCoinMap.put(key, chCoinList);
					for(int i = 0; i < oldCoinList.size();i++){
						Map oldCoin = oldCoinList.get(i);
						Map newCoin = newCoinList.get(i);
						if( !oldCoin.get("per_ch").equals(newCoin.get("per_ch")) ){
							chCoinList.add(newCoin);
						}
					}
				}
				StringBuilder rtnJsonStr = new StringBuilder("[");
				rtnJsonStr.append("{ \"cmd\" : \"tick_ch\" , \"value\" : "+JsonUtil.getJsonFromMap(chCoinMap).toString()+" }" );
				rtnJsonStr.append("]");					
				WebSocketCon.sendAllSessionToMessage( rtnJsonStr.toString() );
				
				DATA.COIN_INFO_STR = JsonUtil.getJsonFromMap(DATA.getCoinInfo()).toString();
				
				Thread.sleep( 2700 );
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		
	}
	
	
	public static void main(String[] args) throws Throwable {
		
		new Thread(new TickManager()).start();
		
		Thread.sleep(1000000);
		
		System.out.println("main quit");
		
		//Thread.sleep(2000);
		
	}
	
	
}
