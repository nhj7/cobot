<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="nhj.util.*" %>

<%
	System.out.println("variable.jsp...");
	String domain = "cobot.co.kr";
	String websocket_adr = null;
	
	if( request.isSecure() ){
		websocket_adr = "wss://cobot.co.kr/echo";
	}else{
		websocket_adr = "ws://cobot.co.kr/echo";
	}
	
	String ip = ""; 
	try{
		ip = NetUtil.getLocalIp();
		if( ip.startsWith("172.30")){
			domain = ip;
			websocket_adr = "ws://"+ip+"/echo";
		}
	}catch(Throwable e){
		
	}
	


%>

var cobot_domain = "<%=domain %>";
var WebSocket_adr = "<%=websocket_adr %>";
//alert(WebSocket_adr);