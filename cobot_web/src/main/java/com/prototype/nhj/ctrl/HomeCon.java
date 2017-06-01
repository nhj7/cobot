package com.prototype.nhj.ctrl;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.prototype.nhj.svc.HomeSvc;

/**  
 * Handles requests for the application home page.
 */
@Controller
public class HomeCon {
	 
	private static final Logger logger = LoggerFactory.getLogger(HomeCon.class);
	
	@Resource(name="HomeSvc")
	private HomeSvc homeService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/home"})
	public String home(Model model, @RequestParam Map ioMap, ServletRequest req) {
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		
		String formattedDate = dateFormat.format(date);
		
		homeService.printTmp(model);
		
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("ioMap", ioMap );
		   
		try {
			
			//DBIO_Cubrid_Test.main(null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "home";
	}
	
}
