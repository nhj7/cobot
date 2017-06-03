package kr.co.cobot.ctrl;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("TestWebSocketController")
@RequestMapping(value = "Test")
@ServerEndpoint(value = "/echo")

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
	public void onOpen(Session session) {
		System.out.println("Open session id : " + session.getId());

		try {
			final Basic basic = session.getBasicRemote();
			basic.sendText("Connection Established");
		} catch (IOException e) {
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
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message from " + session.getId() + ": " + message);
		try {
			final Basic basic = session.getBasicRemote();
			basic.sendText("to : " + message);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		sendAllSessionToMessage(session, message);
	}

	@OnError
	public void onError(Throwable e, Session session) {

	}

	/**
	 * The user closes the connection.
	 * 
	 * Note: you can't send messages to the client from this method
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("Session " + session.getId() + " has ended");
		sessions.remove(session);
	}
}