package kr.co.cobot.conf;

import org.hibernate.Session;

import kr.co.cobot.entity.TbPostInfo;

public class HibernateTest {
	public static void main(String[] args) {
		org.hibernate.Transaction tx = null;
		try{
			Session session = HibernateCfg.getCurrentSession();
			tx = session.getTransaction();
			tx.begin();

			TbPostInfo post = new TbPostInfo();
			post.setSiteDvcd("11");
			post.setCategoryDvcd("112");
			post.setPostDvcd("1122");
			post.setPostTitle("fff𝌆𝌆f");
			post.setPostUrl("1222");
			session.save(post);
			tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
		}finally {
			HibernateCfg.closeSession();
			HibernateCfg.closeSessionFactory();
		}
		
		
		
		
		
		System.out.println("11233");
		
		
	}
}
