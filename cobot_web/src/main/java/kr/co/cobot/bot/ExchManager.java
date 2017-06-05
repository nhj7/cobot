package kr.co.cobot.bot;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbExchange;

public class ExchManager implements Runnable {

	@Override
	public void run() {
		
		
		while(true){
			try {
				
				
				Session session = HibernateCfg.getCurrentSession();
				// 트랜잭션 시작
				
				
				
				
				List exchList = session.createQuery("from TbExchange").list();
				
				for(int i = 0; i < exchList.size() ; i++){
					
					TbExchange te = (TbExchange) exchList.get(i);
					//Map map = new BeanMap(someBean);

				}
				
				
				
				Thread.sleep( 60000 );
				
				// DBIO_Cubrid_Test.main(null);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}finally{
				HibernateCfg.closeSession();
			}
		}
		
		
		
		
	}

}
