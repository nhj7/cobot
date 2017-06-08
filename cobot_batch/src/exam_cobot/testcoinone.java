package exam_cobot;

import io.socket.client.IO;
import io.socket.client.IO.Options;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class testcoinone {

	
	public static void main(String[] args) throws Throwable {
		System.out.println("Hellow");
		
		
		Options opts = new Options();
		opts.transports = new String[]{ "websocket", "polling"};
		Socket socket = IO.socket("https://push.coinone.co.kr/ticker", opts);
		System.out.println("socket : "+ socket);
		// {transports:['websocket','polling']}
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
			    //socket.emit("foo", "hi");
			    //socket.disconnect();
				System.out.println("EVENT_CONNECT : "+args);
			  }

			}).on("update", new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
				  
				  System.out.println("update : "+args[0]);
			  }

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
				  System.out.println("EVENT_DISCONNECT : "+args[0]);
			  }

			});
			socket.connect();

	}
}
