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
	    -ms-overflow-style: none;  // IE 10+
    	overflow: -moz-scrollbars-none;  // Firefox
	}	
	.searchBodyDiv::-webkit-scrollbar { 
	    display: none;  // Safari and Chrome
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
function addItem(idx, jObj ){
	var div = $(create("div"));
	div.attr("id", "item"+idx);
	div.attr("class", "itemDiv");
	
	
	
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
					<div class="searchBodyDivWrapper" id="searchBodyDivWrapper" style="width: 200%;">
						<span class="searchBodyItem" data-item-dvcd="seller" data-item-value="seller" >@seller</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jumma" >@jumma</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="jejujinfarm" >@jejujinfarm</span>
			            <span class="searchBodyItem" data-item-dvcd="seller" data-item-value="toktok" >@toktok</span>
					</div>
					</div>
				</div>
				<div class="searchRowDiv" id="searchRowDiv2" >
					<div class="searchTitleDiv" id="searchTitleDiv" >
						판매방식
					</div>
					<div class="searchBodyDiv" id="searchBodyDiv"  >
					<div id="searchBodyDivWrapper" class="searchBodyDivWrapper"  style="width: 200%;">
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
					<div class="searchBodyDivWrapper" id="searchBodyDivWrapper" style="width: 275%;">
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="1">의류</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="2">잡화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="3">화장품/미용</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="4">디지털/가전</span>						
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="5">식품</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="6">여행/문화</span>
						<span class="searchBodyItem" data-item-dvcd="category" data-item-value="7">금융</span>
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
				font-size:1.3em;
				font-weight:bold;
				margin-bottom:0.25em;
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
			  width:8em;height:8em;
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
			}
		</script>
		<section>
			<div class="itemListDiv">
			
				<!-- item start -->
				<div class="itemDiv">
					<div class="itemCell itemImgDiv itemSellerDiv" style="text-align:center;">
						<img class="prodImg" title="@seller"
							src="https://phinf.pstatic.net/shopping/main_1021908/10219087424.4.jpg?type=f133" />						
					</div>
					<div style="width:1em;">
					 
					</div>
					<div class="itemCell itemInfoDiv">
						<div class="itemInfoTitle" style="width:100%;">샤오미 보조배터리 10000mAh 3세대 프로 c타입 <span style="color:red">현재 11.6(SBD) </span></div>
						<div class="itemInfoTitle">남은 시간 : <span data-countdown="2017-08-04 21:00:00"></span></div>
						<div class="itemInfoRow">
						  <div class="itemInfoCell">28,000원</div>						  
						  <div class="itemInfoCell">$32.6 (12) <a href="javascript:;" onclick="viewReply('itemInfoReply1')">▼</a></div>
						</div>
						
						
						<div class="itemInfoReply" id="itemInfoReply1">
							<div class="itemInfoReplyItem">
								1. sigizzang 32SBD 입찰합니다.<br />
								2. gomyh16 19SBD 입찰합니다.<br />
								3. kwak 18SBD 입찰합니다.<br />
								4. kimsungmin 17SBD 입찰합니다.<br />
								5. nunojesus 16SBD 입찰합니다.<br />
								
							</div>
						</div>											
					</div>
					
				</div>
				<!-- item end -->
				
				
				
			</div>
		</section>
		<br />
		<section>
			<div id="paging" class="jquery-paging">
			
			</div>
		</section>
		<br /><br />
	</main>  
</body>
</html>
