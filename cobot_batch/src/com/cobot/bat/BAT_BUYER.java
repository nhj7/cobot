package com.cobot.bat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nhj.api.poloniex.PoloniexAPI;
import nhj.db.mybatis.MyBatisFactory;
import nhj.util.DateUtil;
import nhj.util.PrintUtil;

public class BAT_BUYER {
	final static Logger logger = LoggerFactory.getLogger(BAT_BUYER.class);
	static BAT_DAO dao;
	static{
		BAT_COMM.init( "/log/cobot_batch/cobot_buyer.log" );
		dao = (BAT_DAO) MyBatisFactory.getDAO(BAT_DAO.class);
		logger.info("Entering BAT_ANALYSIS application.");
	}
	
	public static Map convertListToMap(List<Map> l){
		Map m = new HashMap();
		for(int i = 0; i < l.size();i++){
			m.put( l.get(i).get("CCD"), l.get(i));
		}
		return m;
	}
	
	public static void l( Object log ){
		if( log != null ){
			logger.info(log.toString());
		}
	}
	
	public static BigDecimal setPerCh(Map VALUE_MAP, Object _CUR_PRICE){
		
		BigDecimal CUR_PRICE = new BigDecimal( _CUR_PRICE.toString() );
		if( VALUE_MAP.get("BUY_PRICE") == null){
			VALUE_MAP.put("BUY_PRICE", CUR_PRICE);
		}
		BigDecimal PER_CH = ( (BigDecimal ) CUR_PRICE ).divide( (BigDecimal) VALUE_MAP.get("BUY_PRICE"), 2);
		PER_CH = PER_CH.subtract(BigDecimal.ONE).multiply(new BigDecimal(100));

		VALUE_MAP.put("PER_CH", PER_CH);
		
		return PER_CH;
	}
	
	public static int LIMIT_NUMBER_COIN = 21;
	
	public static void main(String[] args) {
		
		try{
			
			PoloniexAPI api = new PoloniexAPI(BAT_COMM.polniexKey, BAT_COMM.polniexSecret);
			
			Map<String, Map> MY_COINS = api.returnBalances();
						
			
			BigDecimal btc_bal = new BigDecimal( MY_COINS.get("BTC").get("balance").toString() ); 
			
			//MY_COINS.clear();
			
		while(true){
			
			String mm = DateUtil.getDttm("mm");			
			
			// 코인당 구매 가격
			BigDecimal UNIT_BTC = btc_bal.divide( new BigDecimal( LIMIT_NUMBER_COIN ), 8, BigDecimal.ROUND_CEILING );
			
			l("btc_bal : "+btc_bal + ", UNIT_BTC : " + UNIT_BTC);
			
			
			
			
			// 구입 추천 코인 조회 리스트 
			List RCM_LIST = dao.selectRcmList(mm);
			PrintUtil.printList(RCM_LIST);
			for(int i = 0 ; i < RCM_LIST.size();i++){
				
				
				Map RCM_Map = (Map)RCM_LIST.get(i);
				
				String ccd = RCM_Map.get("CCD").toString();
				Map COIN_INFO = (Map) MY_COINS.get(ccd);
				if( ccd.equals("BTC") ){
					continue;
				}
				if(MY_COINS.containsKey(ccd)){
					
					try {
						
						BigDecimal PER_CH = setPerCh(COIN_INFO, RCM_Map.get("CUR_PRICE"));
						
						
						l("이미 보유한 코인입니다. " + ccd + ", PER_CH : " + PER_CH + ", BUY_PRICE : " + COIN_INFO.get("BUY_PRICE") + ", CUR_PRICE : " + RCM_Map.get("CUR_PRICE") );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw e;
					}
				}else{
					
					if( MY_COINS.size() < LIMIT_NUMBER_COIN ){
						// TODO ... 구매하기    
						
						
						
						Map NEW_VAL_MAP = new HashMap();
						
						// 구매가격 결정하기
						BigDecimal BUY_PRICE = ((BigDecimal)RCM_Map.get("CUR_PRICE")).divide( new BigDecimal(1.0025), 2);
						
						BigDecimal AMOUNT = UNIT_BTC.divide( BUY_PRICE , 8, BigDecimal.ROUND_FLOOR);
						
						api.buy(RCM_Map.get("CCD").toString(), BUY_PRICE, AMOUNT);
						
						NEW_VAL_MAP.put("BUY_PRICE", BUY_PRICE);
						NEW_VAL_MAP.put("CUR_PRICE", RCM_Map.get("CUR_PRICE"));
						NEW_VAL_MAP.put("PER_CH", "0");
						NEW_VAL_MAP.put("BUY_DTTM", DateUtil.getDttm("yyyy-MM-dd kk:mm:ss"));
						MY_COINS.put(ccd  , NEW_VAL_MAP);
						
						l("[" + ccd + "] : 코인을 [" + RCM_Map.get("CUR_PRICE") + "]에 구매요청하였습니다.");
						
						break;
					}else{
						l("보유 코인이 초과하여 구매하실 수 없습니다. [" + ccd + "]" );
					}
				}
			}
			
			l("정산 : ======================================================");
			
			BigDecimal AVG_PER_CH = BigDecimal.ZERO;
			
			// 현재 가격 조회 및 세팅
			{
				String[] arr_ccd = new String[MY_COINS.size()];
				java.util.Iterator it = MY_COINS.keySet().iterator();
				for(int i = 0; it.hasNext();i++){
					arr_ccd[i] = it.next().toString();
				}
				
				List cur_bal_list = dao.selectCoSnapList(arr_ccd, mm);
				for(int i = 0; i < cur_bal_list.size();i++){
					
					Map cb_map = (Map)cur_bal_list.get(i); 
					BigDecimal PER_CH = setPerCh( (Map) MY_COINS.get(cb_map.get("CCD")) , new BigDecimal( cb_map.get("PRICE").toString()) );
				}
			}
			
			
			
			for(java.util.Iterator it = MY_COINS.keySet().iterator(); it.hasNext();){
				String ccd = it.next().toString();
				if( "BTC".equals(ccd)) {
					continue;
				}
				Map VAL_MAP = (Map)MY_COINS.get(ccd);
				
				if( VAL_MAP.get("PER_CH") == null){
					VAL_MAP.put("PER_CH", "0");
					continue;
				}
				if( VAL_MAP.get("BUY_DTTM") == null){
					VAL_MAP.put("BUY_DTTM", DateUtil.getDttm("yyyy-MM-dd kk:mm:ss"));
				}
				
				
				BigDecimal PER_CH = new BigDecimal( VAL_MAP.get("PER_CH").toString());
				
				AVG_PER_CH = AVG_PER_CH.add(PER_CH);
				l( ccd + " : 증감율 : " + VAL_MAP.get("PER_CH")  + ", BUY_PRICE : " + VAL_MAP.get("BUY_PRICE") + ", BUY_DTTM : " + VAL_MAP.get("BUY_DTTM"));
			}
			
			AVG_PER_CH = AVG_PER_CH.divide( new BigDecimal(MY_COINS.size() - 1), 2 );
			l("정산 : 평균 : " + AVG_PER_CH);
			
			String newMM = "";
			
			while( mm.equals( ( newMM = DateUtil.getDttm("mm")) ) ){
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
