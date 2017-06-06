package kr.co.cobot.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhj.api.BithumbAPI;
import nhj.api.PoloniexAPI;
import nhj.util.PrintUtil;

public class TickManager implements Runnable {

	//
	
	@Override
	public synchronized void run() {
		
		
		while(true){
			try {
				
				
				PoloniexAPI poloniex = new PoloniexAPI("", "");
				
				List poloList = poloniex.returnTicker();
				
				
				//PrintUtil.printList(poloList);
				
				
				Map m = new HashMap();
				m.put("eid_" + 1, poloList);
				
				
				BithumbAPI bithumb = new BithumbAPI(); 
				List bitList = bithumb.getTickData("");
				
				PrintUtil.printList(bitList);
				
				m.put("eid_" + 2, bitList);
				
				DATA.setCoinInfo(m);
				
				Thread.sleep( 7000 );
				
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
