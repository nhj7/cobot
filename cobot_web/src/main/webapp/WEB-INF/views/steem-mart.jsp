<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Cobot - Steem Market </title>  
  <script src="/js/jquery.min.js"></script>
  <script src="/js/jquery.cookie.1.4.1.js"></script>
  <script src="/js/jquery.countdown.min.js"></script>
  <script src="/js/jquery.paging.min.js"></script>
  <script src="/js/loading/jm.spinner.js"></script>
  <link href="/css/loading/jm.spinner.css" rel="stylesheet">
  
</head>
<style>
	:root{
		--main-width:980px;
		--main-color:#4078c0
	}
	
	main{
		width:var(--main-width);
	}
	.searchDiv{
		display:table;
		width:var(--main-width);
		border:1px solid silver;
	}
	.searchRowDiv{
		display:table-row;
		border-bottom:1px solid silver;
	}
	.searchTitleDiv{
		display:table-cell;
		width:15%;
		background-color:var(--main-color);
		color:white;
		padding-left:10px;
		padding-right:10px;
		padding-top:5px;
		padding-bottom:5px;
		font-weight:bold;
	}
	.searchBodyDiv{
		display:table-cell;
		padding-left:20px;
		padding-right:10px;
		padding-top:10px;
		padding-bottom:10px;
		border-bottom:1px solid silver;
		
	}
	.searchBodyItem{
		color:var(--main-color);
		cursor:pointer;
		padding-left:5px;
		padding-right:5px;
		margin-right:15px;
	}
	.searchBodyItem:active{
		color:red;
		
	}
	.searchBodyItem:hover{
		color:black;	
	}
	.searchBodyItemOn{
		background-color:var(--main-color);
		border-radius:5px;
		color:white;	
	} 
	.searchBodyItemOn:hover{
		color:white;	
	} 
	.jquery-paging{		
		width:var(--main-width);
		text-align:center;
		text-decoration: none;
	}
	.paging-itemClass{
		color:var(--main-color);
		margin-right:10px;	
		margin-left:10px;
		padding-left:5px;
		padding-right:5px;
		text-decoration: none;
		font-weight:bold;
	}
	.paging-itemClass-current{
		background-color:var(--main-color);
		border-radius:5px;
		color:white;
		text-decoration: none;
	}
	.paging-side{
		color:var(--main-color);
		margin-right:10px;	
		margin-left:10px;
		padding-left:5px;
		padding-right:5px;
		text-decoration: none;
		font-weight:bold;
	}
	.searchDivWrapper{
		z-index:0;
		font-size:0.8em;
		text-align:right;
		position:relative;
		margin-top:-1.3em;
	}
</style>
<script>
var agent = navigator.userAgent.toLowerCase();
var browser = "";
if (agent.indexOf("chrome") != -1) {
	browser = "chrome";
}
else if (agent.indexOf("safari") != -1) {
	browser = "safari";
}
else if (agent.indexOf("firefox") != -1) {
	browser = "firefox";
}
else if (agent.indexOf("msie") != -1) {
	browser = "ie";
}
var pageSize = 10; 
var postSize = 10;
</script>
<script src="/js/steem/biz.js"></script>
<script>
var cachePath = "/extanal/img/";

function addItem(idx, jObj ){
	//alert();
	var newItem = $("#itemDummy").clone();
	newItem.attr("id","item" + idx);
	newItem.css("display","");
	newItem.attr("data-link", jObj.postUrl );
	newItem.attr("onclick", "window.open('"+STEEM_URL + jObj.postUrl+"', '_blank')" );
	var prodImg = newItem.find("#prodImg");	
	
	if( jObj.cacheProdImgUrl && jObj.cacheProdImgUrl.length > 0 ){
		prodImg.attr("src", cachePath + jObj.cacheProdImgUrl );
	}else{
		prodImg.attr("src", jObj.prodImgUrl );
	}
	
	
	
	var prodName = newItem.find("#prodName");	
	prodName.text( jObj.prodName );
	
	var strAuctionEndDttm = newItem.find("#strAuctionEndDttm");	
	strAuctionEndDttm.attr("data-countdown", jObj.strAuctionEndDttm );
	
	var realAmt = newItem.find("#realAmt");	
	realAmt.text( jObj.realAmt + "" );
	try{
		var sbdKrw = $("#sbd_txt").text().replace(",", "");
		var realKrwAmt = newItem.find("#realKrwAmt");	
		realKrwAmt.text( comma( exactRound(jObj.realAmt * sbdKrw, 0) ) + "원"  );
	}catch(e){
		
	}
	
	
	var oriAmt = newItem.find("#oriAmt");	
	oriAmt.text( comma(jObj.oriAmt) + "원" );
	
	var sellAmt = newItem.find("#sellAmt");	
	sellAmt.text( jObj.sellAmt + "" );
	
	var lastSellAmt = newItem.find("#lastSellAmt");	
	lastSellAmt.text( jObj.lastSellAmt + "" );
	
	var auctionEndDttm = newItem.find("#auctionEndDttm");	
	auctionEndDttm.text( jObj.strAuctionEndDttm );
	
	var voteAmt = newItem.find("#voteAmt");	
	voteAmt.text( '$'+jObj.voteAmt);
	
	var itemInfoReplyPre = newItem.find("#itemInfoReplyPre");
	itemInfoReplyPre.click(
		function(e){
			e.stopPropagation();
			viewReply("itemInfoReply" + idx);
			return false;
		}		
	);
	
	itemInfoReplyPre.text("총 " + jObj.reply.length + "건의 입찰이 있습니다. ");
	
	
	var itemInfoReply = newItem.find("#itemInfoReply");	
	itemInfoReply.attr("id", "itemInfoReply" + idx);
	
	var itemInfoReplyItem = newItem.find("#itemInfoReplyItem");	
	itemInfoReplyItem.empty();
	for(var rIdx = 0; rIdx < jObj.reply.length ; rIdx++){
		itemInfoReplyItem.append("<div class='replyItemOne'>"+(rIdx+1)+". @"+jObj.reply[rIdx].author+ " : " +jObj.reply[rIdx].comment+"</div>");
	}
	
	var postImgUrl = newItem.find("#postImgUrl");
	
	if( jObj.cachePostImgUrl.length > 0 ){
		postImgUrl.attr("src", cachePath + jObj.cachePostImgUrl );
	}else{
		postImgUrl.attr("src", jObj.postImgUrl );
	}	
	
	
	var seller = newItem.find("#seller");	
	seller.text( "@"+jObj.author );
	
	try{
		if( jObj.method == "2" ){
			
			var dcRatio = newItem.find("#dcRatio");
			var sellRatio = exactRound( (1 - jObj.realAmt / jObj.sellAmt) * 100 , 1);
			var oriRatio = exactRound( (1 - jObj.realAmt / jObj.oriAmt) * 100 , 1);
			
			var dcRtVal = 0.0;
			if( sellRatio > 0 && oriRatio > 0 ){
				dcRtVal = sellRatio > oriRatio ? sellRatio : oriRatio;
			}else if( sellRatio < 0 && oriRatio > 0){
				dcRtVal  = sellRatio;
			}else if( sellRatio > 0 && oriRatio < 0 ){
				dcRtVal  = oriRatio;
			}else if( sellRatio < 0 && oriRatio < 0 ){
				dcRtVal = sellRatio > oriRatio ? oriRatio : sellRatio;
			}else{
				dcRtVal = sellRatio;
			}
			dcRatio.text( dcRtVal +"%"  );
			
			//var dcRatio = newItem.find("#dcRatio");	
			//dcRatio.text( exactRound( (1 - jObj.realAmt / jObj.sellAmt) * 100 , 1)+"%"  );
		}
	}catch(e){		
	}
	
	
	$("#itemListDiv").append(newItem);
	
}



</script>
<body>	
	<main style="margin: 0 auto;" >
	  <center><img src="/img/steem/steemmart.png" style="position:relative;z-index:1;"/></center>
		<div id="loadSpinnerWrapper" style="position:fixed;text-align:center;display:none;">
			<img width="140px" height="140px" src="/img/loading/steemit.png" /><br />
			<div id="loadSpinner" ></div>
		</div>
		<section>
		  <div id="searchDivWrapper" class="searchDivWrapper" style="" title="이 데이터는 Cobot 시세조회 서비스를 사용합니다.">(폴로닉스 기준 1SBD : 약<span id="sbd_txt"></span>원)</div>
			<div class="searchDiv" id="searchDiv">
				<div class="searchRowDiv" id="searchRowDiv1" >
					<div class="searchTitleDiv" id="searchTitleDiv" style="">
						판매자
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" style="font-weight:bold;">
						<span class="searchBodyItem" data-item-dvcd="seller" data-item-value="seller" >@seller</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jumma" >@jumma</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="greenjuice" >@greenjuice</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jejujinfarm" >@jejujinfarm</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="toktok" >@toktok</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="iieeiieeii" >@iieeiieeii</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="minari" >@minari</span>
					</div>
				</div>
				<div class="searchRowDiv" id="searchRowDiv2" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						판매방식
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" >
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="1">판매</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="2">경매</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="3">이벤트</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="4">SBD대출</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="0">기타</span>
					</div>
				</div>
				
				<div class="searchRowDiv" id="searchRowDiv3" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						카테고리
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" >
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="1">의류</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="2">잡화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="3">화장품/미용</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="4">디지털/가전</span>						
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="5">식품</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="6">여행/문화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="7">생활/건강</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="99">금융</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="8">도서</span>
						<br />
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="0">기타</span>
					</div>
				</div>
				
				<div class="searchRowDiv" id="searchRowDiv4" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						상태
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" >						
						<span class="searchBodyItem searchBodyItemOn" data-item-dvcd="status" data-item-value="1">판매중</span>
						<span class="searchBodyItem" data-item-dvcd="status" data-item-value="8">종료</span>
						
						<div style="display:none;">
						<hr />
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="1">1일전</span>
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="2">2일전</span>
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="3">3일전</span>
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="4">4일전</span>
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="5">5일전</span>
						<span class="searchBodyItem " data-item-dvcd="actionDate" data-item-value="6">6일전</span>
						</div>
						
					</div>
				</div>
				
				
			</div>
		</section>		
		<style>
		.sortDiv{
			display:table;
			width:var(--main-width);
			border-bottom:1px solid silver;
			margin-top:15px;
			font-size:12px;
			padding-bottom:10px;
		}
		.sortItem{
			color:#4078c0;
			cursor:pointer;
			padding-left:5px;
			padding-right:5px;
			margin-right:15px;
		}
		.sortItemOn{
			font-weight:bold;
			font-size:1.2em;
		}
		</style>
		<section>
			<div class="sortDiv">
				
				<span class="sortItem sortItemOn" data-dvcd="descAuctionDate">낙찰일순(낙찰임박)</span>
				<span class="sortItem" data-dvcd="descRegDate">등록일순(최근)</span>
				<span class="sortItem" data-dvcd="ascRealAmt">낮은 가격순(실구매가)</span>
				<span class="sortItem" data-dvcd="descRealAmt">높은 가격순(실구매가)</span>
				<span class="sortItem" data-dvcd="descReply" >입찰(댓글) 많은 순</span>
			</div>
		</section>
		<style>
			.itemListDiv{
				display:table;
				width:var(--main-width);
				border-bottom:1px solid silver;
				margin-top:15px;
				font-size:12px;
				padding-bottom:10px;
			}
			.itemDiv{
				display:table-row;				
				// color:#4078c0;
				color:black;				
				padding-left:5px;
				padding-right:5px;
				margin-right:15px;
				border-bottom:1px solid silver;
			}
			.itemCell{
				border-bottom:1px solid silver;
				padding-bottom:15px;
				padding-top:15px;
			}
			.itemImgDiv{
				display:-webkit-inline-box;
				border:1px solid silver;
				height:160px;
			}
			.itemInfoRow{
				display:table-Row;
				width:90%;
				text-valign:top;
				vertical-align: middle;
				margin-left:15px;
			}
			.itemInfoCell{
				display:table-Cell;
				text-valign:middle;
				vertical-align: middle;
				margin-left:15px;
				padding:5px;
				text-align:center;
				font-size:1.2em;				
				border:1px solid black;

			}
			.itemInfoDiv{
				display:table-cell;				
				text-valign:top;
				vertical-align: top;
				padding-left:15px;
			}
			.itemSellerDiv{
				display:table-cell;
				text-align:center;
				vertical-align: top;
				font-size:1.3em;
				font-weight:bold;
				color:#4078c0;
			}
			.itemInfoTitle{
				font-size:1.2em;
				font-weight:bold;
				margin-bottom:7px;
			}
			.itemInfoAmt{
			  display:table-cell;
				font-size:1.2em;
				font-weight:bold;
				margin-bottom:5px;
			}
			
			.itemInfoReply{
				display:none;
				margin-top:1em;
			}
			.itemInfoReply:{
				display:none;
			}
			
			.itemInfoReplyOn{
				display:inline;
			}
			
			.itemInfoReplyItem{
				padding-left:20px;
				width:80%;
			}
			.itemInfoReplyPre{
				width:90%;border:0.5px solid black;font-weight:bold;margin-top:10px;text-align:center;cursor:pointer;
			}
			.replyItemOne{
				width:100%;
			}
		</style>
		
		<script>
			function viewReply(id){
				var jObj = $("#"+id);
				var onClassTxt = "itemInfoReplyOn";
				if( jObj.hasClass( onClassTxt ) ){
					jObj.removeClass( onClassTxt );
				}else{
					jObj.addClass( onClassTxt );
				}
				return false;
			}
			
			
		</script>
		<section>
			<div id="itemListDiv" class="itemListDiv">
			</div>
		</section>
		<br />
		<section>
			<div id="paging" class="jquery-paging">
			
			</div>
		</section>
		<br /><br />
	</main>
	
	<!-- Dummy item start -->
	<div id="itemDummy" class="itemDiv" style="display:none;cursor:pointer;" >
		<div onclick="openLink(this);" class="itemCell itemImgDiv" style="overflow: hidden;width:200px;">
			<img id="prodImg" name="prodImg" style="max-width:100%;height:auto;vertical-align: middle;"
				src="" />
		</div>
		<div style="width:10px;">
		 
		</div>
		<div class="itemCell itemInfoDiv">
			<div id="prodName" class="itemInfoTitle">상품명</div>
			<div class="itemInfoTitle" ><span id="realAmt" style="color:red;">0.0</span>SBD (한화 약 : <data id="realKrwAmt">0원</data> )</div>
			<div class="itemInfoTitle">남은 시간 : <span id="strAuctionEndDttm" data-countdown="2017-08-04 21:00:00"></span></div>
			<div class="itemInfoRow">
			  <div class="itemInfoCell">할인율</div>
			  <div class="itemInfoCell">매출원가(원)</div>
			  <div class="itemInfoCell">경매시작</div>
			  <div class="itemInfoCell">최종입찰가</div>
			  <div class="itemInfoCell">경매종료일시</div>
			  <div class="itemInfoCell">보팅금액</div>
			</div>
			<div class="itemInfoRow">
			  <div id="dcRatio" class="itemInfoCell" style="color:red;"></div>
			  <div id="oriAmt" class="itemInfoCell"></div>
			  <div id="sellAmt" class="itemInfoCell"></div>
			  <div id="lastSellAmt" class="itemInfoCell"></div>
			  <div id="auctionEndDttm" class="itemInfoCell">
			    17년 8월 2일 21시
			  </div>
			  <div id="voteAmt" class="itemInfoCell">$0.0</div>
			</div>			
			<div id="itemInfoReplyPre" onclick="viewReply('itemInfoReply'); return false;" class="itemInfoReplyPre">
			 	총 0건의 입찰이 있습니다. 
			</div>
			
			<div class="itemInfoReply" id="itemInfoReply">
				<div class="itemInfoReplyItem" id="itemInfoReplyItem">
				</div>
			</div>											
		</div>
		<div onclick="" class="itemCell itemSellerDiv">
			<img id="postImgUrl" width="160px" height="120px" 
				src="" />
			<br />
			<span id="seller"></span>
		</div>
	</div>
	<!-- item end -->
	<%@ include file="/footer.jsp"  %>	
</body>
</html>
