package kr.co.cobot.conf;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
	private HttpSession httpSession;

	
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		
		System.out.println("modifyHandshake execute!!! : " + request.getClass().getName());
		
		//super.modifyHandshake(config, request, response);
		
		
		
		httpSession = (HttpSession) request.getHttpSession();
		
		System.out.println("config.getUserProperties() : "+config.getUserProperties() + ", session : " + httpSession);
		
		config.getUserProperties().put(HttpSession.class.getName(), httpSession);
		
		

		
	}
	
	
}
