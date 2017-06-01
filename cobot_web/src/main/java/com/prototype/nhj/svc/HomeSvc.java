package com.prototype.nhj.svc;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service("HomeSvc")
public class HomeSvc {
	public void printTmp(Model model){
		  
		System.out.println("printTmp " + model);
		
	} 
}
