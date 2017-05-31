package exam_cobot;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import ws.wamp.jawampa.WampError;
import ws.wamp.jawampa.connection.IWampConnectorProvider;
import ws.wamp.jawampa.transport.netty.NettyWampClientConnectorProvider;

public class test {
	public static void main(String[] args) throws Throwable {
		System.out.println("Hellow");
		
		
		final WampClient client;
		final WampClient trollClient;
		try {
			
			IWampConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();

		    // Create a builder and configure the client
		    WampClientBuilder builder = new WampClientBuilder();
		    builder.withConnectorProvider(connectorProvider)
		           .withUri("wss://api.poloniex.com")
		           .withRealm("realm1")
		           .withInfiniteReconnects()
		           .withReconnectInterval(5, TimeUnit.SECONDS);
		    // Create a client through the builder. This will not immediatly start
		    // a connection attempt
		    client = builder.build();
		    
		    
		    
		    System.out.println( "client : " + client);
		    System.out.println( "client.realm() : " + client.realm());
		    
		    client.statusChanged().subscribe(new Action1<WampClient.State>() {
	            @Override
	            public void call(WampClient.State t1) {
	                System.out.println("Session status changed to " + t1);
	                
	                if (t1 instanceof WampClient.ConnectedState) {
	                    System.out.println("Client P ricevuto " + t1);
	                    
	                    client.makeSubscription("ticker")
	                    .subscribe(
	                    		new Action1<PubSubData>() {
									@Override
									public void call(PubSubData message) {
										// TODO Auto-generated method stub
										//System.out.println("ES ricevuto " + eventSubscription);
				                        
				                        
				                        System.out.println("Received " + message.arguments().toString());
									}
								}
	                    );
	                    // end makeSubscription("ticker")
	                    
	                    client.makeSubscription("trollbox")
                        .subscribe((s) -> { System.out.println(s.arguments()); });

	                    
	                    
	                    // end client.call("ticker", "BTC_XRP")
	                    
	                    
	                }
	            } // call end
	            
		    }
	        );
		    
		    /*
		    trollClient.statusChanged().subscribe(new Action1<WampClient.State>() {
	            @Override
	            public void call(WampClient.State t1) {
	                System.out.println("trollClient Session status changed to " + t1);
	                
	                if (t1 instanceof WampClient.ConnectedState) {
	                    System.out.println("trollClient P ricevuto " + t1);
	                    trollClient.makeSubscription("trollbox")
                        .subscribe((s) -> { System.out.println(s.arguments()); });

	                    
	                    
	                    // end client.call("ticker", "BTC_XRP")
	                    
	                    
	                }
	            } // call end
	            
		    }
	        );
	        trollClient.open();
		    */
		    
		    
		    
		    
		    client.open();
		    
		    
		    
		    
		    
		     

		    
		    
		    
		} catch (WampError e) {
		    // Catch exceptions that will be thrown in case of invalid configuration
		    System.out.println(e);
		    return;
		}
	}
}
