package kr.co.cobot.ctrl;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.svc.HomeSvc;
import nhj.util.PrintUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeCon {

	private static final Logger logger = LoggerFactory.getLogger(HomeCon.class);

	@Resource(name = "HomeSvc")
	private HomeSvc homeService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = { "/exam" } )
	public @ResponseBody Object exam(Model model, @RequestParam Map ioMap, ServletRequest req) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		String formattedDate = dateFormat.format(date);		

		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("ioMap", ioMap);
		
		System.out.println("ioMap : "+ioMap);
		
		PrintUtil.printMap(ioMap);
		 
		Transaction tx = null;
		try {
			
			/*
			Session session = HibernateCfg.getCurrentSession();
			// 트랜잭션 시작
			tx = session.getTransaction();
			
			TbExchange te = new TbExchange();
			
			te.setEid(1);
			te.setEcd("PLNX");
			te.setEnm("Poloniex");
			te.setEnmKo("폴로닉스..");
			//te.setRegDttm(date);
			//te.setModDttm(date);
			
			session.beginTransaction();
			session.merge(te);
			//tx.
			
			tx.commit(); // 커밋
			
			*/
			
			
			// DBIO_Cubrid_Test.main(null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}
		Gson gson = new Gson();
		
		return (ioMap);
	}
	
	@RequestMapping(value = { "/EOS_SCAN" })
	public String EOS_SCAN(Model model, @RequestParam Map ioMap, ServletRequest req) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		String formattedDate = dateFormat.format(date);

		

		return "EOS_Ether_Scan_Bot";
	}

}
