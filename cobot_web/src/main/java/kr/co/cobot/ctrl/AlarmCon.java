package kr.co.cobot.ctrl;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbExchange;
import kr.co.cobot.entity.TbWebPushM;
import nhj.util.PrintUtil;
import nhj.util.SecurityUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AlarmCon {

	private static final Logger logger = LoggerFactory.getLogger(AlarmCon.class);
	
	/*
	 *  reg alarm
	 */
	@RequestMapping(value = { "/Alarm/Reg" })
	public @ResponseBody Object alarmReg(Model model, @RequestParam Map ioMap, ServletRequest req) {
		
		PrintUtil.printMap(ioMap);
		
		String alarmID = SecurityUtil.getRandomID();
		
		String endpoint = ioMap.get("endpoint").toString();
		
		Transaction tx = null;
		try {
			
			
			Session session = HibernateCfg.getCurrentSession();
			// 트랜잭션 시작
			tx = session.getTransaction();
			
			TbWebPushM webPushM = new TbWebPushM();
			webPushM.setAlarmId(alarmID);
			webPushM.setWebDvcd((short) 1);
			webPushM.setEndPoint(endpoint.replaceAll("https://fcm.googleapis.com/fcm/send/", ""));
			webPushM.setPublicKey(ioMap.get("keys[p256dh]").toString());
			webPushM.setAuth( ioMap.get("keys[auth]").toString());
			webPushM.setSteemDvcd( ioMap.get("steemDvcd").toString() );
			//te.setRegDttm(date);
			//te.setModDttm(date);
			
			session.beginTransaction();
			session.save(webPushM);
			tx.commit(); // 커밋
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}
		
		Map out = new HashMap();
		out.put("alarmID", alarmID);

		return out;
	}
	
	
	
	/*
	 *  Del alarm
	 */
	@RequestMapping(value = { "/Alarm/Del" })
	public @ResponseBody Object alarmDel(Model model, @RequestParam Map ioMap, ServletRequest req) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("ioMap", ioMap);
		PrintUtil.printMap(ioMap);
		String alarmID = ioMap.get("alarmID").toString();
		Transaction tx = null;
		try {
			
			
			Session session = HibernateCfg.getCurrentSession();
			// 트랜잭션 시작
			tx = session.getTransaction();
			
			TbWebPushM webPushM = new TbWebPushM();
			webPushM.setAlarmId(alarmID);
			session.beginTransaction();
			session.remove(webPushM);
			tx.commit(); // 커밋
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			if( tx != null ){
				tx.rollback();
			}
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}
		
		Map out = new HashMap();
		out.put("alarmID", alarmID);
		return out;
	}
	
	
	/*
	 *  update alarm
	 */
	@RequestMapping(value = { "/Alarm/Update" })
	public @ResponseBody Object alarmUpdate(Model model, @RequestParam Map ioMap, ServletRequest req) {
		
		
		PrintUtil.printMap(ioMap);
		
		String alarmID = ioMap.get("alarmID").toString();
		
		Transaction tx = null;
		try {
			
			
			Session session = HibernateCfg.getCurrentSession();
			// 트랜잭션 시작
			tx = session.getTransaction();
			
			TbWebPushM webPushM = session.get(TbWebPushM.class, alarmID);
			
			webPushM.setSteemDvcd(ioMap.get("steemDvcd").toString());
			session.beginTransaction();
			session.merge(webPushM);
			tx.commit(); // 커밋
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}
		
		Map out = new HashMap();
		out.put("alarmID", alarmID);
		return out;
	}
	
	
	
	@RequestMapping(value = { "/Alarm" })
	public String EOS_SCAN(Model model, @RequestParam Map ioMap, ServletRequest req) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		String formattedDate = dateFormat.format(date);

		

		return "alarm";
	}

}
