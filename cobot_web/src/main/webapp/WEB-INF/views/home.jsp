<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<html lang="ko">
<meta content="text/html;charset=UTF-8">
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<form:form modelAttribute="ioMap" name="frm" action="/home" method="post">
<input type="text" name="한글"  value="<c:out value="${ioMap['한글']}" />" />
<form:input type="submit" path="" value="send" />
</form:form>



</body>
</html>
