<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="nhj.api.*" %>
<%@ page import="nhj.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="kr.co.cobot.bot.*" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Cobot Thread Mornitor</title>
</head>
<body>
	<%
		Map chart_data = ChartManager.CHART_DATA;
		for(Iterator it = chart_data.keySet().iterator();it.hasNext();){
			out.println( "cm is : " + it.next() + "<br />");
		}
	%>
</body>
</html>