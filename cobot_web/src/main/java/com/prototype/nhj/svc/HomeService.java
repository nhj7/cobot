package com.prototype.nhj.svc;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service("HomeService")
public class HomeService {
	public void printTmp(Model model){
		  
		System.out.println("printTmp " + model);
		
	} 
}
