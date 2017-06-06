package kr.co.cobot.ctrl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
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

import com.google.gson.JsonObject;

import kr.co.cobot.bot.DATA;
import kr.co.cobot.conf.GetHttpSessionConfigurator;
import nhj.util.JsonUtil;

@Controller("TestWebSocketController")
@RequestMapping(value = "Test")
@ServerEndpoint(value = "/echo"
//, configurator=GetHttpSessionConfigurator.class
)

public class WebSocketCon {
	private static final java.util.Set<Session> sessions = java.util.Collections
			.synchronizedSet(new java.util.HashSet<Session>());

	/**
	 * 웹 소켓 테스트 화면
	 * 
	 * @return
	 */
	@RequestMapping("webSocket.do")
	public String testView() {
		return "common/testWebSocket";
	}
	
	
	

	/**
	 * @OnOpen allows us to intercept the creation of a new session. The session
	 *         class allows us to send data to the user. In the method onOpen,
	 *         we'll let the user know that the handshake was successful.
	 */
	@OnOpen
	public void onOpen(Session session , EndpointConfig config) {
		System.out.println("[WebSocket] Open session id : " + session.getId());

		try {
			final Basic basic = session.getBasicRemote();
			
			JsonObject jo = new JsonObject();
			jo.addProperty("cmd", "status");
			jo.addProperty("value", "Connection onOpen!!!");
			
			//System.out.println("onOpen : jo.getAsString() : " + jo.toString());
			
			basic.sendText( jo.toString() );
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		sessions.add(session);
	}

	/**
	 * 모든 사용자에게 메시지를 전달한다.
	 * 
	 * @param self
	 * @param message
	 */
	private void sendAllSessionToMessage(Session self, String message) {
		try {
			for (Session session : WebSocketCon.sessions) {
				if (!self.getId().equals(session.getId()))
					session.getBasicRemote().sendText("All : " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

	
	/**
	 * When a user sends a message to the server, this method will intercept the
	 * message and allow us to react to it. For now the message is read as a
	 * String.
	 * @throws Throwable 
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("[Websocket] Message from [" + session.getId() + "] : " + message + " : ");
		try {
			final Basic basic = session.getBasicRemote();
			JsonObject jo = new JsonObject();
			jo.addProperty("cmd", "coin_info");
			
			Map m = DATA.getCoinInfo();
			
			jo.add("value", JsonUtil.getJsonFromMap(DATA.getCoinInfo()) );
			
			
			//System.out.println("jo.getAsString() : " + jo.toString());
			
			basic.sendText( jo.toString() );
			
			
			//basic.sendText("to : " + message);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		sendAllSessionToMessage(session, message);
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
