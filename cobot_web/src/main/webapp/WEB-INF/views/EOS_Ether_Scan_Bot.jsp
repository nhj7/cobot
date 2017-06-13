<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="nhj.api.*" %>
<%@ page import="nhj.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ICO 스캐너</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/css/main.css" />	

<style>

@media (min-width: 858px) {
    html {
        font-size: 12px;
    }
}

@media (min-width: 780px) {
    html {
        font-size: 11px;
    }
}

@media (min-width: 702px) {
    html {
        font-size: 10px;
    }
}

@media (min-width: 724px) {
    html {
        font-size: 9px;
    }
}

@media (max-width: 623px) {
    html {
        font-size: 8px;
    }
}
td{
	border:1px solid !important;
	align:center !important;
	text-align:center !important;
	color:white;
	font-weight:bold !important;
}
body{
	color:white !important;
	font-size:2.0em !important;
	font-weight:bold !important;
	background:url('/img/main.png') no-repeat !important ;
	background-size: 100% 100% !important;

}
</style>
<script>
	window.onload=function(){
		
		setTimeout("document.location.reload();", 300000);
		
	}
</script>	
</head>
<body>
■ ICO 스캐너(EOS)

<table>
	<tr>
		<td>최종 집계 일시</td>
		<td><%=HTMLParsingAPI.LAST_DTTM %></td>
	</tr>
	<tr>
		<td>페이지 수</td>
		<td><%=HTMLParsingAPI.TOTAL_PAGE %></td>
	</tr>
	<tr>
		<td>이더리움 합산 </td>
		<% 
			System.out.println(HTMLParsingAPI.TOTAL_ETHER);
		%>
		
		<td><%=StringUtil.addComma(String.valueOf(HTMLParsingAPI.TOTAL_ETHER)) %></td>
	</tr>
	<tr>
		<td>작업소요시간</td>
		<td><%=HTMLParsingAPI.LAP_TM / 1000  %>초</td>
	</tr>
</table>
- 집계에는 평균 3~5분 소요됩니다.
- 5분 후 자동재조회 됩니다.
</body>

</html>