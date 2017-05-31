package com.cobot.bat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nhj.api.poloniex.PoloniexAPI;
import nhj.config.LogBackConfigurer;
import nhj.db.mybatis.MyBatisFactory;
import nhj.util.DateUtil;
import nhj.util.PrintUtil;

public class BAT_CO_SNAP {
	
	final static Logger logger = LoggerFactory.getLogger(BAT_CO_SNAP.class);
	static BAT_DAO dao;
	static PoloniexAPI api;
	static{
		
		LogBackConfigurer.configure("/log/cobot/cobot_batch/","co_snap");
		dao = (BAT_DAO) MyBatisFactory.getDAO(BAT_DAO.class);
		logger.info("Entering BAT_CO_SNAP application.");
	}
	
	private static Map EXCH_CO_INFO = new HashMap();
	private static long TICK_TIME = 60000;
	public static void main(String[] args) {
		
		try {			
			List<Map> listAll = dao.selectAll();
			
			for( Map m : listAll ){
				EXCH_CO_INFO.put( m.get("EID") + "|" + m.get("CCD"), m);
			}
			
			//logger.debug(EXCH_CO_INFO.toString());
			api = new PoloniexAPI(BAT_COMM.polniexKey, BAT_COMM.polniexSecret);
			 
			
			while(true){
				List<Map> tickList = api.returnTicker();
				long cur = System.currentTimeMillis();
				
				for(Map tickMap : tickList ){
					try {
						
						String cid = ((Map)EXCH_CO_INFO.get( "1|" + tickMap.get("ccd"))).get("CID").toString();
						tickMap.put("cid", cid);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("ccd : " + tickMap.get("ccd"));
						throw e;
					}
				}
				
				//PrintUtil.printList(tickList);
				
				dao.insertCoSnapBatch( tickList );
				
				while( 
					System.currentTimeMillis() - cur < TICK_TIME
				){							
					Thread.sleep(500);
				}
				
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.error("dd",e);
		}
		
		
		
		
	}
}
