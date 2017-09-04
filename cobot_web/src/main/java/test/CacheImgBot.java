package test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbCacheImg;
import net.coobird.thumbnailator.Thumbnails;
import nhj.util.SecurityUtil;

public class CacheImgBot implements Runnable {
	private CacheImgBot(){
		
	}
	public static void init() {		
		File pathChk = new File(CACHE_DIR_PATH);
		if( !pathChk.exists()){
			pathChk.mkdirs();
		}
		new Thread(new CacheImgBot()).start();
	}

	private static Map<String, String> CACHE_IMG_REPO = new HashMap();
	private static Stack WORK_REPO = new Stack();

	public static void addCacheImg(String imgUrl) {
		/*
		 * if( cacheImgMap.containsKey(imgUrl) ){ return; }
		 */
		if( "".equals(imgUrl)){
			return;
		}
		else if (WORK_REPO.contains(imgUrl)) {
			System.out.println("CacheImgManager wait For Work... [" + imgUrl + "]");
			return;
		} else {

			try {
				Session session = HibernateCfg.getCurrentSession();
				List list = session.createQuery("from TbCacheImg where imgUrl = :imgUrl ").setParameter("imgUrl", imgUrl)
						.getResultList();
				if (list.size() == 0) {
					WORK_REPO.push(imgUrl);
				} else {
					TbCacheImg cacheImg = (TbCacheImg) list.get(0);				
					// File이 존재하면 캐시에 넣음. 없으면 디비에서 삭제
					chkFileAndCache( cacheImg,  session);				
				}
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				HibernateCfg.closeSession();
			}
		}
	}
	
	private static boolean chkFileAndCache( TbCacheImg cacheImg,  Session session ){
		if (new File(CACHE_DIR_PATH + cacheImg.getImgPath()).exists()) {
			CACHE_IMG_REPO.put(cacheImg.getImgUrl(), cacheImg.getImgPath());
			return true;
		}
		// 파일이 없으면 DB에서 삭제하고 다시 addCacheImg(); 호출!!!
		else {
			
			org.hibernate.Transaction tx = session.beginTransaction();
			
			Query q = session.createQuery("delete from TbCacheImg where imgUrl = :imgUrl ").setParameter("imgUrl",
					cacheImg.getImgUrl());
			int i = q.executeUpdate();
			tx.commit();
			addCacheImg(cacheImg.getImgUrl());
			return false;
		}
	}

	private static String CACHE_DIR_PATH = "/data/cobot/img/";

	
	
	public static String getImgUrl(String url){
		if( url == null || "".equals(url)){
			return "";
		}
		Object obj = CACHE_IMG_REPO.get(url);
		if( obj == null || "".equals(obj.toString()) ) addCacheImg(url);
		return obj == null ? "": obj.toString();
	}

	private static int REFRESH_CACHE_IMG_WORK_TERM = 300;

	@Override
	public void run() {
		try {
			Session session = HibernateCfg.getCurrentSession();
			
			
			List<TbCacheImg> list = session.createQuery("from TbCacheImg ").getResultList();
			for(TbCacheImg img : list ){
				chkFileAndCache(img, session);			
			}
			while (true) {
				try {

					while (!WORK_REPO.empty()) {
						
						String imgUrl = WORK_REPO.pop().toString();
						
						//System.out.println("CacheImgManager cache image work start! + " + imgUrl);
						
						String imgName = SecurityUtil.getRandomID128() + ".png";
						
						org.hibernate.Transaction tx = null;
						try {
							URL url = new URL(imgUrl);
							Thumbnails.of(Thumbnails.of(url).width(200).asBufferedImage()).scale(1)
									.sourceRegion(0, 0, 200, 150)
									.toFile( CACHE_DIR_PATH + imgName );
							TbCacheImg cacheImg = new TbCacheImg();
							cacheImg.setImgUrl(imgUrl);
							cacheImg.setImgPath(imgName);
							tx = session.beginTransaction();						
							session.save(cacheImg);
							tx.commit();
							
							CACHE_IMG_REPO.put(imgUrl, imgName);
							
						} catch (Exception e) {
							if( tx != null && tx.getStatus() != TransactionStatus.COMMITTED ){
								tx.rollback();
							}							
						}
					}
					Thread.sleep(REFRESH_CACHE_IMG_WORK_TERM);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
								
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			System.out.println("CacheImgManager.finally");
			
			HibernateCfg.closeSession();		
			HibernateCfg.closeSessionFactory();
		}
	}
	
	public static void main(String[] args) throws Throwable {

		CacheImgBot.init();
		
		System.out.println("add img");
		String imgUrl1 = "https://steemitimages.com/0x0/https://steemitimages.com/0x0/https://steemitimages.com/DQmQZ2eXkxh2zr8YsrEXEUo8rPhmsxqQcG9V1cVFxAyiXKm/bag7301.JPG";
		//CacheImgManager.addCacheImg(imgUrl1);
		System.out.println("add img end");
		
		String cacheImgUrl1 = getImgUrl(imgUrl1);
		System.out.println("cacheImgUrl1 : " + cacheImgUrl1 );
		
		System.out.println("wait 6sec.");
		Thread.sleep(6000);
		
		cacheImgUrl1 = getImgUrl(imgUrl1);
		System.out.println("cacheImgUrl1 : " + cacheImgUrl1 );
		
		System.out.println("wait 3sec.");
		Thread.sleep(3000);
		cacheImgUrl1 = getImgUrl(imgUrl1);
		System.out.println("cacheImgUrl1 : " + cacheImgUrl1 );
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();

		HibernateCfg.closeSession();
		HibernateCfg.closeSessionFactory();
	}
	
}
