<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

  <meta charset="UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta name="mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="theme-color" content="#4078c0">
  <title>Cobot - Steem Mart Mobile</title>  
  <script src="/js/jquery.min.js"></script>
  <script src="/js/jquery.cookie.1.4.1.js"></script>
  <script src="/js/jquery.countdown.min.js"></script>
  <script src="/js/jquery.paging.min.js"></script>
  <script src="/js/loading/jm.spinner.js"></script>
  <link href="/css/loading/jm.spinner.css" rel="stylesheet">
  
</head>
<style>
	:root{
		--main-width:100%;
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
		width:30%;
		background-color:#4078c0;		color:white;
		padding-left:1em;
		padding-right:1em;
		padding-top:0.25em;
		padding-bottom:0.25em;
		font-weight:bold;
		vertical-align:middle;
	}
	.searchBodyDiv{
		display:table-cell;
		padding-left:0.5em;
		padding-right:0.5em;
		padding-top:0.5em;
		padding-bottom:0.5em;
		border-bottom:1px solid silver;		
	    overflow-x: scroll;
	    overflow-y: hidden;
	    -webkit-overflow-scrolling: touch;
		-webkit-transform: translateZ(0px);
	    -webkit-transform: translate3d(0,0,0);
	    -webkit-perspective: 1000;
	    -ms-overflow-style: none;
    	overflow: -moz-scrollbars-none;
    	vertical-align:middle;
	}	
	.searchBodyDiv::-webkit-scrollbar { 
	    display: none;
	}
	.searchBodyDivWrapper{		
		-webkit-overflow-scrolling: touch;
		-webkit-transform: translateZ(0px);
	    -webkit-transform: translate3d(0,0,0);
	    -webkit-perspective: 1000;
	}
	.searchBodyItem{
		color:#4078c0;
		cursor:pointer;
		padding-left:0.25em;
		padding-right:0.25em;
		margin-right:0.25em;
		float: left;
	}
	.searchBodyItem:active{
		color:red;	
	}
	.searchBodyItem:hover{
		color:black;	
	}
	.searchBodyItemOn{
		background-color:#4078c0;
		border-radius:0.25em;
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
		color:#4078c0;
		margin-right:0.25em;	
		margin-left:0.25em;
		padding-left:0.25em;
		padding-right:0.25em;
		text-decoration: none;
		font-weight:bold;
	}
	.paging-itemClass-current{
		background-color:#4078c0;
		border-radius:0.25em;
		color:white;
		text-decoration: none;
	}
	.paging-side{
		color:#4078c0;
		margin-right:0.25em;	
		margin-left:0.25em;
		padding-left:0.25em;
		padding-right:0.25em;
		text-decoration: none;
		font-weight:bold;
	}
</style>

<script src="/js/steem/biz.js"></script>
<script>
var pageSize = 5; 
var postSize = 10;
var cachePath = "/extanal/img/";
function addItem(idx, jObj ){
	//alert();
	var newItem = $("#itemDummy").clone();
	newItem.attr("id","item" + idx);
	newItem.css("display","");
	newItem.attr("data-link", STEEM_URL + jObj.postUrl );
	
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
	realAmt.text( jObj.realAmt + " SBD" );
	try{
		var sbdKrw = $("#sbd_txt").text().replace(",", "");
		var realKrwAmt = newItem.find("#realKrwAmt");	
		realKrwAmt.text( "한화 약 : ("+comma( exactRound(jObj.realAmt * sbdKrw, 0) ) + "원)"  );
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
	
	/*
	var itemInfoReplyPre = newItem.find("#itemInfoReplyPre");	
	itemInfoReplyPre.attr("onclick", "viewReply('itemInfoReply"+idx+"')");
	itemInfoReplyPre.text("총 " + jObj.reply.length + "건의 입찰이 있습니다. ");
	*/
	
	var voteInfo = newItem.find("#voteInfo");
	voteInfo.css("color","gray");
	voteInfo.attr("onclick", "viewReply(\'itemInfoReply"+idx+"\')");
	voteInfo.html("▼ "+jObj.reply.length+"");
	
	var itemInfoReply = newItem.find("#itemInfoReply");	
	itemInfoReply.attr("id", "itemInfoReply" + idx);
	
	var itemInfoReplyItem = newItem.find("#itemInfoReplyItem");	
	itemInfoReplyItem.empty();
	for(var rIdx = 0; rIdx < jObj.reply.length ; rIdx++){
		var comment = jObj.reply[rIdx].comment;
		var replyCmt = (rIdx+1)+". "+jObj.reply[rIdx].author+ " : " +comment
		if( replyCmt.length > 29 ){
			replyCmt = replyCmt.substring(0, 26 ) + "...";
		}
		itemInfoReplyItem.append("<div class='replyItemOne'>"+replyCmt+"</div>");
	}
	/*
	var postImgUrl = newItem.find("#postImgUrl");
	
	if( jObj.cachePostImgUrl.length > 0 ){
		postImgUrl.attr("src", cachePath + jObj.cachePostImgUrl );
	}else{
		postImgUrl.attr("src", jObj.postImgUrl );
	}	
	
	
	var seller = newItem.find("#seller");	
	seller.text( "@"+jObj.author );
	*/
	
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
		}
	}catch(e){
		
	}
	
	
	$("#itemListDiv").append(newItem);
	
}

</script>
<body>
	<!-- style="width:15em;height:7em;" -->
	<main style="margin-left:0.5;margin-top:0.5em;" >
	  <center><img 
	    
	    src="/img/steem/steemmart.png" /></center>
		<div id="loadSpinnerWrapper" style="position:fixed;text-align:center;display:none;">
			<img width="140px" height="140px" src="/img/loading/steemit.png" /><br />
			<div id="loadSpinner" ></div>
		</div>
		<section>
		  <div style="font-size:0.7em;margin-top:-1.5em;text-align:right;color:gray;" title="이 데이터는 Cobot 시세조회 서비스를 사용합니다.">(폴로닉스 기준 1SBD : 약 <span id="sbd_txt"></span>원)</div>
			<div class="searchDiv" id="searchDiv">
				<div class="searchRowDiv" id="searchRowDiv1" >
					<div class="searchTitleDiv" id="searchTitleDiv" style="">
						판매자
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" style="font-weight:bold;">
					<div class="searchBodyDivWrapper" id="searchBodyDivWrapper" style="width: 250%;">
						<span class="searchBodyItem" data-item-dvcd="seller" data-item-value="seller" >@seller</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jumma" >@jumma</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="greenjuice" >@greenjuice</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jejujinfarm" >@jejujinfarm</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="toktok" >@toktok</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="iieeiieeii" >@iieeiieeii</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="minari" >@minari</span>
					</div>
					</div>
				</div>
				<div class="searchRowDiv" id="searchRowDiv2" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						판매방식
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv"  >
					<div id="searchBodyDivWrapper" class="searchBodyDivWrapper"  style="width: 170%;">
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="1">판매</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="2">경매</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="3">이벤트</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="4">SBD대출</span>
						<span class="searchBodyItem" data-item-dvcd="method" data-item-value="0">기타</span>
					</div>
					</div>
				</div>
				
				<div class="searchRowDiv" id="searchRowDiv3" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						카테고리
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" >
					<div class="searchBodyDivWrapper" id="searchBodyDivWrapper" style="width: 265%;">
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="1">의류</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="2">잡화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="3">화장품/미용</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="4">디지털/가전</span>						
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="5">식품</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="6">여행/문화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="7">생활/건강</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="99">금융</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="8">도서</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="0">기타</span>
					</div>
					</div>
				</div>
				
				<div class="searchRowDiv" id="searchRowDiv4" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						상태
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv" >						
						<span class="searchBodyItem searchBodyItemOn" data-item-dvcd="status" data-item-value="1">판매중</span>
						<span class="searchBodyItem" data-item-dvcd="status" data-item-value="8">종료</span>
					</div>
				</div>
				
				
			</div>
		</section>
		<style>
		.sortDiv{
			display:table;
			width:var(--main-width);
			border-bottom:1px solid silver;
			margin-top:1.25em;
			font-size:12px;
			padding-bottom:1em;
		}
		.sortItem{
			color:#4078c0;
			cursor:pointer;
			padding-left:0.25em;
			padding-right:0.25em;
			margin-right:1.25em;
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
				margin-top:1.25em;
				font-size:12px;
				padding-bottom:1em;
			}
			.itemDiv{
				display:table-row;
				// color:#4078c0;
				color:black;
				cursor:pointer;
				padding-left:0.25em;
				padding-right:0.25em;
				margin-right:0.25em;
				border-bottom:1px solid silver;
			}
			.itemCell{
				border-bottom:1px solid silver;
				padding-bottom:0.25em;
				padding-top:0.25em;
			}
			.itemImgDiv{
				display:table-cell;
				border:1px solid silver;
				
			}
			.itemInfoRow{
				display:table-Row;
				text-valign:top;
				vertical-align: top;
				margin-left:0.25em;
			}
			.itemInfoCell{
				display:table-Cell;
				text-valign:middle;
				vertical-align: middle;
				margin-left:0.25em;
				padding:0.25em;
				text-align:center;
				font-size:1.2em;
				font-weight:bold;
				//border:1px solid black;

			}
			.itemInfoDiv{
				display:table-cell;
				text-valign:top;
				vertical-align: top;
				margin-left:0.25em;
				padding-top:0.5em;
			}
			.itemSellerDiv{
				display:table-cell;
				text-align:center;
				vertical-align: middle;
				font-size:1.3em;
				font-weight:bold;
				color:#4078c0;
			}
			.itemInfoTitle{
				font-size:1em;
				font-weight:bold;
				margin-bottom:0.25em;
				padding-top:0.25em;
			}
			.itemInfoAmt{
			  display:table-cell;
				font-size:1.2em;
				font-weight:bold;
				margin-bottom:0.25em;
			}
			
			.itemInfoReply{
				display:none;
				margin-top:1em;
			}
			.itemInfoReply:{
				display:none;
			}
			
			.itemInfoReplyOn{
				display:block;
			}
			
			.itemInfoReplyItem{
				padding-left:0.5em;
			}
			.prodImg{
			  width:8em;height:6em;
			}
		</style>
		
		<script>
			function viewReply(id){
				event.stopPropagation();
				
				//alert(1231);
				var jObj = $("#"+id);
				var onClassTxt = "itemInfoReplyOn";
				if( jObj.hasClass( onClassTxt ) ){
					jObj.removeClass( onClassTxt );
				}else{
					jObj.addClass( onClassTxt );
				}
			}
			
			function openLink(obj){
				//alert(1122);				
				var jObj = $(obj);
				var itemDiv = jObj.closest(".itemDiv");
				//alert(itemDiv.attr("data-link"));
				window.open(itemDiv.attr("data-link"), "_blank");
				
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
	<!-- item start -->
	<div id="itemDummy" class="itemDiv" style="display:none;">
		<div onclick="openLink(this);" class="itemCell itemImgDiv itemSellerDiv" style="text-align:center;">
			<img id="prodImg" class="prodImg" title="판매상품 이미지"
				src="" />						
		</div>
		<div ontouch="openLink(this);" style="width:1em;">
		 
		</div>
		<div class="itemCell itemInfoDiv">
			<div  onclick="openLink(this);" class="itemInfoTitle" style="width:100%;">
				<div id="prodName" style="font-size:1.2em;margin-bottom:0.35em;">샤오미 보조배터리 10000mAh 3세대 프로 c타입 </div>
				<div style="font-size:1.1em;"><span id="realAmt" style="color:red;"></span> <span style="margin-left:1em;" id="realKrwAmt"></span> </div>
				<div style="font-size:1.1em;">할인율 : <span id="dcRatio" style="color:red;"></span></div>
			</div>
			<div onclick="openLink(this);" class="itemInfoTitle" style="font-size:0.9em;">남은 시간 : <span id="strAuctionEndDttm" data-countdown=""></span></div>
			<div  class="itemInfoRow">
			  <div onclick="openLink(this);" id="oriAmt" class="itemInfoCell" style="font-size:0.9em;">28,000원</div>						  
			  <div class="itemInfoCell" style="font-size:0.9em;"><span id="voteAmt">0</span>&nbsp;<span id="voteInfo">(12)<a href="javascript:;" onclick="viewReply('itemInfoReply')">▼</a></span></div>
			</div>
			
			
			<div class="itemInfoReply" id="itemInfoReply">
				<div id="itemInfoReplyItem" class="itemInfoReplyItem">
				</div>
			</div>											
		</div>
		
	</div>
	<!-- item end -->
	<%@ include file="/footer.jsp"  %>
</body>

</html>
