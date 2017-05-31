<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="util.*" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인기검색어</title>
</head>
<body>
<%
	try{
		HTMLParser.isHTML = true;
		HTMLParser.setPrintWriter(out);
		HTMLParser.main(null);
		out.flush();
	}catch(Throwable e){}
	
%>
</body>
</html>