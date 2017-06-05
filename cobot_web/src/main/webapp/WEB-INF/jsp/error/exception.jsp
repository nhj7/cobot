<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%
	String mode = System.getProperty("mode");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cobot - Error</title>
</head>
<body>
Cobot - Error <br />
<%
	if( "local".equals(mode) ||  "dev".equals(mode) ){
%>
	<%exception.printStackTrace(new java.io.PrintWriter(out)); %>
<%
	}
%>
</body>
</html>