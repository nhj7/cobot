package kr.co.cobot.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbExchange;

public class ExchManager implements Runnable {

	@Override 
	public void run() {
		
		Session session = HibernateCfg.getCurrentSession();
		
		boolean flag = true;
		
		while( flag ){
			try {
				
				// 트랜잭션 시작
				/*
				List exchList = session.createQuery("from TbExchange").list();
				
				Map<String, Map> NEW_EXCH_INFO = new HashMap();
				for(int i = 0; i < exchList.size() ; i++){
					
					TbExchange te = (TbExchange) exchList.get(i);
					ObjectMapper oMapper = new ObjectMapper();
					
					// object -> Map
			        Map map = oMapper.convertValue(te, Map.class);
			        
			        //System.out.println(map);
			        
			        NEW_EXCH_INFO.put( map.get("eid").toString(),  map );
					
				}
				
				
				
				DATA.setExchs(NEW_EXCH_INFO);
				*/
				Thread.sleep( 60000 );
				
				// DBIO_Cubrid_Test.main(null);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}
		
		HibernateCfg.closeSession();
		
		
	}

}
