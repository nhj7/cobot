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
	color:#424242;
	font-weight:bold !important;
	font-size:0.8em !important;
}
.td_col{
	text-align:center;
	background-color:#008299 !important;
	color:white;
	width:40vw;
}
.td_data{
	
}
body{
	color:black !important;
	font-size:2.0em !important;
	font-weight:bold !important;
	
	background-size: 100% 100% !important;

}
/* background:url('/img/main.png') no-repeat !important ; */
</style>
<script>
	window.onload=function(){
		
		setTimeout("document.location.reload();", 60000);
		
	}
</script>	
</head>
<body>


<table style="width:90%;margin-left:5%;">
	
	<tr>
		<td class="td_col">최종 작업시간</td>
		<td><%=HTMLParsingAPI.LAST_DTTM %></td>
	</tr>
	<tr>
		<td class="td_col">페이지 수</td>
		<td><%=HTMLParsingAPI.TOTAL_PAGE %></td>
	</tr>
	<tr>
		<td class="td_col">합계(Ether) </td>
		<% 
			System.out.println(HTMLParsingAPI.TOTAL_ETHER);
		%>
		
		<td><%=StringUtil.addComma(String.valueOf(HTMLParsingAPI.TOTAL_ETHER)) %></td>
	</tr>
	<%
		BigDecimal firstDistTrg = new BigDecimal("200000000");
		BigDecimal firstDist = BigDecimal.ZERO;
		if( firstDistTrg.compareTo(BigDecimal.ZERO) > 0 && HTMLParsingAPI.TOTAL_ETHER.compareTo(BigDecimal.ZERO) > 0 ){
			firstDist = firstDistTrg.divide(HTMLParsingAPI.TOTAL_ETHER, 8, BigDecimal.ROUND_DOWN);	
		}
		
		
	%>
	<tr>
		<td class="td_col">1 Ether 당 배분 수 </td>
		<td><%=StringUtil.addComma(String.valueOf(firstDist)) %></td>
	</tr>
	
	<%
		// 1 EOS 가격 
		List l = ( List )DATA.getCoinInfo().get("eid_3");
		System.out.println(l);
		
		BigDecimal ether_price = BigDecimal.ONE;
		for(int i = 0; i < l.size();i++){
			Map coin = (Map)l.get(i);
			if( "ETH".equals( coin.get("ccd")) ){
				System.out.println("coin price : " + coin.get("price"));
				ether_price = (BigDecimal) coin.get("price");
			}
		}
		BigDecimal eos_price = BigDecimal.ZERO;
		
		if( firstDist.compareTo(BigDecimal.ZERO) > 0  ){
			eos_price = ether_price.divide(firstDist, 0, BigDecimal.ROUND_DOWN);	
		}
		
	%>
	
	<tr>
		<td class="td_col"> EOS/Ether (KRW)</td>
		<td><%=StringUtil.addComma(String.valueOf(eos_price)) %> / <%=StringUtil.addComma(String.valueOf(ether_price.intValue())) %> </td>
	</tr>
	<%
		BigDecimal total_dist = new BigDecimal("1000000000");
		BigDecimal total_gap = eos_price.multiply(new BigDecimal(10) );
	%>
	<tr>
		<td class="td_col"> EOS 시총 </td>
		<td>약 <%=StringUtil.addComma(String.valueOf(total_gap.intValue())) %>억</td>
	</tr>
</table>

</body>

</html>