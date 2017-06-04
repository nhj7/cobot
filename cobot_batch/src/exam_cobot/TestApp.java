package exam_cobot;

import java.net.URI;
import java.net.URISyntaxException;

import nhj.util.socket.WebsocketClientEndpoint;

public class TestApp {
	public static void main(String[] args) {
		try {
			
			
			System.out.println("1");
			
			// open websocket
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
					new URI("wss://api2.poloniex.com/"));

			System.out.println("2");
			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					System.out.println(message);
				}
			});

			System.out.println("3");
			// send message to websocket
			clientEndPoint.sendMessage("{'event':'subscribe','channel':'1001'}");

			System.out.println("4");
			// wait 5 seconds for messages from websocket
			Thread.sleep(1115000);

		} catch (InterruptedException ex) {
			System.err.println("InterruptedException exception: " + ex.getMessage());
		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
	}

}
