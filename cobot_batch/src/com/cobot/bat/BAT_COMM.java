package com.cobot.bat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nhj.config.LogBackConfigurer;

public class BAT_COMM {
	
	final static Logger logger = LoggerFactory.getLogger(BAT_COMM.class);
	
	public static String polniexKey = "K3Z5ZFD6-5X36MSHK-5D23RLL1-PQ2HN46X";
	public static String polniexSecret = "85dc221d70f2f905087950717b808c61ab3b5e32ed07cb94b03484da9e2dc74dfd9ef3594ac6aff36a1a7f61b26e835570a03952f41e1ed7c713448ef0431d56";
	
	public static void init(String logPath){
		
		
		
		
		
		
		/*
		for(Iterator it = root.iteratorForAppenders();it.hasNext();){
			
			Appender ap = (Appender) it.next();
			
			System.out.println(ap);
		}
		*/
		
		
		
		//List loggerList = lc.getLoggerList();
		
		//PrintUtil.printList(loggerList);
		
		
		
		
		//org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
	}
	
	public static void printHelp(){
		
		LogBackConfigurer.configure("/log/cobot/cobot_batch/","printHelp");
		
		logger.info("Invalid args... 실행해고자 하는 배치 명을 넣어주세요. ");
		logger.info("1. 코인 시세 자료 등록 배치 : CO_SNAP , ex) java -jar *.jar CO_SNAP ");
		logger.info("2. 코인 시세 자료 통계 배치 : ANAL , ex) java -jar *.jar ANAL ");
		logger.info("3. 코인 시세 자료 통계 배치 : BUYER , ex) java -jar *.jar BUYER ");
	}
	
	public static void main(String[] args) {
		init("/log/cobot/cobot_batch/comm.log");
		
		if( args == null || args.length == 0 ){
			printHelp();
			return;
		}
		if( "CO_SNAP".equals(args[0].toUpperCase()) ){
			BAT_CO_SNAP.main(null);
		}else if( "ANAL".equals(args[0].toUpperCase()) ){
			BAT_ANALYSIS.main(null);
		}else if( "BUYER".equals(args[0].toUpperCase()) ){
			BAT_BUYER.main(null);
		}
		
	}
}


