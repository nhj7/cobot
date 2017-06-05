package kr.co.cobot.bot;

public class TickManager implements Runnable {

	//
	
	@Override
	public synchronized void run() {
		
		
		try {
			
			
			
			
			Thread.sleep(2000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws Throwable {
		
		new Thread(new TickManager()).start();
		
		Thread.sleep(1000);
		
		System.out.println("main quit");
		
		//Thread.sleep(2000);
		
	}
	
	
}
