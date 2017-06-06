package kr.co.cobot.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhj.api.PoloniexAPI;
import nhj.util.PrintUtil;

public class TickManager implements Runnable {

	//
	
	@Override
	public synchronized void run() {
		
		
		while(true){
			try {
				
				
				PoloniexAPI api = new PoloniexAPI("", "");
				
				List coinInfo = api.returnTicker();
				Map m = new HashMap();
				
				//PrintUtil.printList(coinInfo);
				
				m.put("eid" + 1, coinInfo);
				DATA.setCoinInfo(m);
				
				Thread.sleep( 60000 );
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		
	}
	
	
	public static void main(String[] args) throws Throwable {
		
		new Thread(new TickManager()).start();
		
		Thread.sleep(1000);
		
		System.out.println("main quit");
		
		//Thread.sleep(2000);
		
	}
	
	
}
