package com.cobot.bat;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nhj.db.mybatis.MyBatisFactory;
import nhj.util.DateUtil;
import nhj.util.PrintUtil;

public class BAT_ANALYSIS {
	final static Logger logger = LoggerFactory.getLogger(BAT_ANALYSIS.class);
	static BAT_DAO dao;
	static{
		BAT_COMM.init( "/log/cobot_batch/cobot_analysis.log" );
		dao = (BAT_DAO) MyBatisFactory.getDAO(BAT_DAO.class);
		logger.info("Entering BAT_ANALYSIS application.");
	}
	
	private static long TICK_TIME = 20000;
	
	public static void main(String[] args) {
		
		try{
			
		
		while(true){
			
			long cur = System.currentTimeMillis();
			
		
			List<Map> coSnapAnalList = dao.selectCoSnapAnal();
			
			PrintUtil.printList(coSnapAnalList);
			
			for(Map anMap : coSnapAnalList ){
				try {
					
					float score = 0;
					
					// 1. RISE_VOL
					{
						final float RISE_VOL = Float.parseFloat(anMap.get("RISE_VOL").toString());
						if( RISE_VOL > 100 ){
							score += 100;
						}else{
							score += (RISE_VOL / 10 * 10);
						}
					}
					
					// 2. RISE_PER
					{
						final float RISE_PER = Float.parseFloat(anMap.get("RISE_PER").toString());
						
						score += ( RISE_PER * 10 );
					}
					
					// 3. RISE_PER_1
					{
						final float RISE_PER_1 = Float.parseFloat(anMap.get("RISE_PER_1").toString());					
						score += ( RISE_PER_1 * 50 );
					}
									
					// 4. CAT_RISE_VOL SCORE				
					{
						String CAT_RISE_VOL = anMap.get("CAT_RISE_VOL").toString();
						String[] ARR_RISE_VOL = CAT_RISE_VOL.split(",");
						if( 
							Integer.parseInt(ARR_RISE_VOL[3]) > Integer.parseInt(ARR_RISE_VOL[2])
							&& Integer.parseInt(ARR_RISE_VOL[2]) > Integer.parseInt(ARR_RISE_VOL[1])
							&& Integer.parseInt(ARR_RISE_VOL[1]) > Integer.parseInt(ARR_RISE_VOL[0])
						){
							score += ( 200 );
						}else if(
							Integer.parseInt(ARR_RISE_VOL[3]) > Integer.parseInt(ARR_RISE_VOL[2])
							&& Integer.parseInt(ARR_RISE_VOL[2]) > Integer.parseInt(ARR_RISE_VOL[1])
							//&& Integer.parseInt(ARR_RISE_VOL[1]) > Integer.parseInt(ARR_RISE_VOL[0])	
						){
							score += ( 150 );
						}else if(
							Integer.parseInt(ARR_RISE_VOL[3]) > Integer.parseInt(ARR_RISE_VOL[2])
							//&& Integer.parseInt(ARR_RISE_VOL[2]) > Integer.parseInt(ARR_RISE_VOL[1])
							&& Integer.parseInt(ARR_RISE_VOL[1]) > Integer.parseInt(ARR_RISE_VOL[0])	
						){
							score += ( 125 );
						}else if(
							//Integer.parseInt(ARR_RISE_VOL[3]) > Integer.parseInt(ARR_RISE_VOL[2])
							Integer.parseInt(ARR_RISE_VOL[2]) > Integer.parseInt(ARR_RISE_VOL[1])
							&& Integer.parseInt(ARR_RISE_VOL[1]) > Integer.parseInt(ARR_RISE_VOL[0])	
						){
							score += ( 125 );
						}else {
							score += ( 100 );
						}
					}
					
					// 5. CAT_RISE_PER SCORE
					{
						String CAT_RISE_PER = anMap.get("CAT_RISE_PER").toString();
						String[] ARR_RISE_PER = CAT_RISE_PER.split(",");
						if( 	
							Integer.parseInt(ARR_RISE_PER[3]) > Integer.parseInt(ARR_RISE_PER[2])
							&& Integer.parseInt(ARR_RISE_PER[2]) > Integer.parseInt(ARR_RISE_PER[1])
							&& Integer.parseInt(ARR_RISE_PER[1]) > Integer.parseInt(ARR_RISE_PER[0])
						){
							score += ( 200 );
						}else if(
							Integer.parseInt(ARR_RISE_PER[3]) > Integer.parseInt(ARR_RISE_PER[2])
							&& Integer.parseInt(ARR_RISE_PER[2]) > Integer.parseInt(ARR_RISE_PER[1])
							//&& Integer.parseInt(ARR_RISE_VOL[1]) > Integer.parseInt(ARR_RISE_VOL[0])	
						){
							score += ( 150 );
						}else if(
							Integer.parseInt(ARR_RISE_PER[3]) > Integer.parseInt(ARR_RISE_PER[2])
							//&& Integer.parseInt(ARR_RISE_VOL[2]) > Integer.parseInt(ARR_RISE_VOL[1])
							&& Integer.parseInt(ARR_RISE_PER[1]) > Integer.parseInt(ARR_RISE_PER[0])	
						){
							score += ( 150 );
						}else if(
							//Integer.parseInt(ARR_RISE_VOL[3]) > Integer.parseInt(ARR_RISE_VOL[2])
							Integer.parseInt(ARR_RISE_PER[2]) > Integer.parseInt(ARR_RISE_PER[1])
							&& Integer.parseInt(ARR_RISE_PER[1]) > Integer.parseInt(ARR_RISE_PER[0])	
						){
							score += ( 125 );
						}else {
							score += ( 100 );
						}
					}
					
					{
						String RISE24PER = anMap.get("RISE24PER").toString();
						
						score = score * ( 100f - Float.parseFloat(RISE24PER))  / 100; 
					}
					
					anMap.put("SCORE", score);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
			
			if( coSnapAnalList.size() > 0 ){
				dao.insertAn1( coSnapAnalList );
			}
			
			String newMM = "";
			
			while( 
					System.currentTimeMillis() - cur < TICK_TIME
			){
				Thread.sleep(500);
			}
			
		//insertAn1
		
		}
		
		}catch(Throwable e){
			e.printStackTrace();
			
			logger.error("dd",e);		
		}
	}
}
