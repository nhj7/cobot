package kr.co.cobot.bot;

import java.util.Stack;

public class MailBot implements Runnable{
	
	private static Stack WORK_REPO = new Stack();
	
	private MailBot(){
		
		
	}
	
	public static void addMail() {
		
	}
	
	public static void init() {		
		
		new Thread(new MailBot()).start();
	}	
	
	private long REFRESH_TERM = 150;	
	@Override
	public void run() {
		while(true){
			
			while (!WORK_REPO.empty()) {
				
			}
			
			
			try {
				Thread.sleep(REFRESH_TERM);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public static void main(String[] args) {
		
		MailBot.init();
		
		
		
	}
	
}
