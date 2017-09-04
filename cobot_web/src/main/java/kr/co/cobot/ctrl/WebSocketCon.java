package kr.co.cobot.ctrl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import kr.co.cobot.bot.ChartBot;
import kr.co.cobot.bot.DATA;
import kr.co.cobot.conf.GetHttpSessionConfigurator;
import nhj.util.JsonUtil;

@Controller("WebSocketController")
@ServerEndpoint(value = "/echo"
, configurator=GetHttpSessionConfigurator.class
)

public class WebSocketCon {
	private static final java.util.Set<Session> sessions = java.util.Collections
			.synchronizedSet(new java.util.HashSet<Session>());

	/**
	 * 웹 소켓 테스트 화면
	 * 
	 * @return
	 */
	@RequestMapping("/refresh")
	public String refresh() {
		String msg = "[{\"cmd\":\"refresh\"}]";
		sendAllSessionToMessage(msg);
		return "common/testWebSocket";
	}
	
	
	

	/**
	 * @OnOpen allows us to intercept the creation of a new session. The session
	 *         class allows us to send data to the user. In the method onOpen,
	 *         we'll let the user know that the handshake was successful.
	 */
	@OnOpen
	public void onOpen(Session session , EndpointConfig config) {
		String ip = "";
		if( config != null && config.getUserProperties() != null ){
			ip = (String )config.getUserProperties().get("ip");
		}		
		System.out.println("[WebSocket] Open session id : " + session.getId() + " : ip : " + ip );
		try {
			final Basic basic = session.getBasicRemote();
			JsonObject jo = new JsonObject();
			jo.addProperty("cmd", "status");
			jo.addProperty("value", "Connection onOpen!!!");
			basic.sendText( jo.toString() );
		} catch (IOException e) {
			
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		sessions.add(session);
	}

	/**
	 * 모든 사용자에게 메시지를 전달한다.
	 * 
	 * @param self
	 * @param message
	 */
	public static void sendAllSessionToMessage(String message) {
		try {
			for (Session session : WebSocketCon.sessions) {
				session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private String get( JsonObject jo, String key ){
		return jo.get(key).toString().replaceAll("\"", "");
	}

	
	/**
	 * When a user sends a message to the server, this method will intercept the
	 * message and allow us to react to it. For now the message is read as a
	 * String.
	 * @throws Throwable 
	 */
	@OnMessage
	public void onMessage(String message, Session session, EndpointConfig config) throws Throwable {
		//System.out.println("session.getUserProperties() : " + session.getUserProperties());
		
		
		
		System.out.println("[MSG]["+sessions.size()+"] Message from [" + session.getId() + "] : " + message + " : " + session.getUserProperties().get("ip"));
		
		Gson gson = new Gson();
		JsonArray ja = gson.fromJson(message, JsonArray.class);
		
		try {
			
			
				
			
			
			StringBuilder rtnJsonStr = new StringBuilder("[");
			if( ja == null ){
				rtnJsonStr.append("{ \"cmd\" : \"tick\" , \"value\" : "+DATA.COIN_INFO_STR+" }" );
			}else{
				
			int idx = 0;
			for(Iterator it = ja.iterator(); it.hasNext();){
				
				JsonObject jo = (JsonObject)it.next();
				String cmd = get(jo, "cmd");
				
				
				
				if( cmd.equals("tick") && "true".equals( JsonUtil.get(jo, "initFlag")) ){
					if( idx > 0 ){
						rtnJsonStr.append(",");
					}
					rtnJsonStr.append("{ \"cmd\" : \"tick\" , \"value\" : "+DATA.COIN_INFO_STR+" }" );
					
					
				}else if( cmd.equals("chart")){
					
					String eid = get(jo, "eid");
					String unit_ccd = get(jo, "unit_cid");
					String ccd = get(jo, "ccd");
					String chartData = ChartBot.getChartData(eid, unit_ccd, ccd);
					
					try {
						if( idx > 0 ){
							rtnJsonStr.append(",");
						}
						rtnJsonStr.append( "  { \"cmd\" : \"chart\", \"title\":\""+ccd+"/BTC\", \"tick\":"+ChartBot.getTickData(eid, unit_ccd, ccd)+" , \"value\" : "+ chartData +" }" );
						
						
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					
				}
				idx++;
			} // for end
			
			
			} // else end
			rtnJsonStr.append( "]" );
			final Basic basic = session.getBasicRemote();
			basic.sendText( rtnJsonStr.toString() );
			
			//basic.sendText("to : " + message);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//sendAllSessionToMessage(session, message);
	}

	@OnError
	public void onError(Throwable e, Session session) {
		e.printStackTrace();
	}

	/**
	 * The user closes the connection.
	 * 
	 * Note: you can't send messages to the client from this method
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("[Websocket] Session " + session.getId() + " has ended");
		sessions.remove(session);
	}




	
}
