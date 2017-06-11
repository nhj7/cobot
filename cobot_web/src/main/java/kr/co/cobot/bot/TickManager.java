package kr.co.cobot.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhj.api.BithumbAPI;
import nhj.api.CoinoneAPI;
import nhj.api.KorbitAPI;
import nhj.api.PoloniexAPI;
import nhj.util.PrintUtil;

public class TickManager implements Runnable {

	//
	static{
		CoinoneAPI.init();	// WS Socket init
		KorbitAPI.init();	// Web scrapping init
	}
	
	@Override
	public synchronized void run() {
		
		
		while(true){
			try {
				
				Map m = new HashMap();
				
				// PoloniexAPI
				{
					PoloniexAPI poloniex = new PoloniexAPI("", "");
					List poloList = poloniex.returnTicker();
					m.put("eid_" + 1, poloList);
				}
				
				// BithumbAPI
				{
					BithumbAPI bithumb = new BithumbAPI(); 
					List bitList = bithumb.returnTicker();
					
					//PrintUtil.printList(bitList);
					
					m.put("eid_" + 2, bitList);
				}
				
				// CoinoneAPI
				{
					List coinList = CoinoneAPI.returnTicker();
					m.put("eid_" + 3, coinList);
				}
				
				// KorbitAPI
				{
					List korbitList = KorbitAPI.returnTicker();
					m.put("eid_" + 4, korbitList);
				}
				
				{
					String per_krw = CoinoneAPI.getPerKrw();
					m.put("per_krw" , per_krw);
				}
				
				DATA.setCoinInfo(m);				
				Thread.sleep( 3000 );
				
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
