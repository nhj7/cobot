package kr.co.cobot.conf;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
	public String ip;
	public HttpSession httpSession;
	
	@Override
	public boolean checkOrigin(String originHeaderValue) {
		// TODO Auto-generated method stub
		ip = originHeaderValue;
		//config.getUserProperties().put("ip", ip);
		
		
		return super.checkOrigin(originHeaderValue);
	}


	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		
		//System.out.println("modifyHandshake execute!!! : " + request.getClass().getName());
		
		//super.modifyHandshake(config, request, response);
		
		
		
		httpSession = (HttpSession) request.getHttpSession();
		
		System.out.println("httpSession : " + httpSession);
		
		//System.out.println("config.getUserProperties() : "+config.getUserProperties() + ", session : " + httpSession);
		if( httpSession != null && config.getUserProperties() != null ){
			config.getUserProperties().put("ip", httpSession.getAttribute("ip"));
		}
		
		
		

		
	}
	
	
}
