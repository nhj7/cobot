package exam_cobot;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import ws.wamp.jawampa.WampError;
import ws.wamp.jawampa.connection.IWampClientConnectionConfig;
import ws.wamp.jawampa.connection.IWampConnectorProvider;
import ws.wamp.jawampa.transport.netty.NettyWampClientConnectorProvider;
import ws.wamp.jawampa.transport.netty.NettyWampConnectionConfig;

public class test {

	public static void polo1() throws Throwable {
		final WampClient client;
		final WampClient trollClient;
		try {

			NettyWampClientConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();

			// Create a builder and configure the client
			WampClientBuilder builder = new WampClientBuilder();
			builder.withConnectorProvider(connectorProvider).withUri("wss://api.poloniex.com").withRealm("realm1")
					.withInfiniteReconnects().withReconnectInterval(5, TimeUnit.SECONDS);
			// Create a client through the builder. This will not immediatly
			// start
			// a connection attempt
			client = builder.build();

			System.out.println("client : " + client);
			System.out.println("client.realm() : " + client.realm());

			client.statusChanged().subscribe(new Action1<WampClient.State>() {
				@Override
				public void call(WampClient.State t1) {
					System.out.println("Session status changed to " + t1);

					if (t1 instanceof WampClient.ConnectedState) {
						System.out.println("Client P ricevuto " + t1);

						client.makeSubscription("ticker").subscribe(new Action1<PubSubData>() {
							@Override
							public void call(PubSubData message) {
								// TODO Auto-generated method stub
								// System.out.println("ES ricevuto " +
								// eventSubscription);

								System.out.println("Received " + message.arguments().toString());
							}
						});
						// end makeSubscription("ticker")

						client.makeSubscription("trollbox").subscribe((s) -> {
							System.out.println(s.arguments());
						});

						// end client.call("ticker", "BTC_XRP")

					}
				} // call end

			});

			/*
			 * trollClient.statusChanged().subscribe(new
			 * Action1<WampClient.State>() {
			 * 
			 * @Override public void call(WampClient.State t1) {
			 * System.out.println("trollClient Session status changed to " +
			 * t1);
			 * 
			 * if (t1 instanceof WampClient.ConnectedState) {
			 * System.out.println("trollClient P ricevuto " + t1);
			 * trollClient.makeSubscription("trollbox") .subscribe((s) -> {
			 * System.out.println(s.arguments()); });
			 * 
			 * 
			 * 
			 * // end client.call("ticker", "BTC_XRP")
			 * 
			 * 
			 * } } // call end
			 * 
			 * } ); trollClient.open();
			 */

			client.open();

			Thread.sleep(120000);

		} catch (WampError e) {
			// Catch exceptions that will be thrown in case of invalid
			// configuration
			System.out.println(e);
			return;
		}
	}

	public static void polo2() throws Throwable{
		final WampClient client;
		
		
	    try {
	        WampClientBuilder builder = new WampClientBuilder();
	        IWampConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();
	        builder.withConnectorProvider(connectorProvider)
	                .withUri("wss://api2.poloniex.com")
	                .withRealm("realm1")
	                .withInfiniteReconnects()
	                .withReconnectInterval(5, TimeUnit.SECONDS);
	        client = builder.build();
	        client.statusChanged().subscribe(new Action1<WampClient.State>() {
	            @Override
	            public void call(WampClient.State t1) {
	            	
	            	System.out.println( "t1 : " + t1);
	                if (t1 instanceof WampClient.ConnectedState) {
	                    Subscription subscription = client.makeSubscription("1001")
	                            .subscribe((s) -> { System.out.println(s.arguments()); });
	                    
	                }
	            }
	        });
	        client.open();
	        //client.call("subscribe", args);
	        
	        Thread.sleep(100000000);
	    } catch (Exception e) {
	        return;
	    }
	    
	}

	public static void main(String[] args) throws Throwable {
		System.out.println("Hellow");
		// polo1();

		polo2();

	}
}
