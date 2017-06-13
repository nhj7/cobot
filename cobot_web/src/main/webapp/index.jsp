<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="nhj.util.*" %>
<%
	System.out.println( "["+DateUtil.getCurDate("yyyy-MM-dd kk:mm:ss") + "] index.jsp call : " + request.getRemoteAddr());
	
	if(true){
		//throw new Exception("123123");
	}
		
%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>코봇-거래소 정보 모아보자</title>
		<meta name=”description=” Content=”코봇에서는 모든 거래소의 시세정보를 모아서 볼수 있게 하려는 모토로 개발이 진행 되고 있습니다. 추가적인 기능으로 잔고정산, 채팅, 암호화화폐 정보 검색 등의 기능이 추가될 예정이오니 많은 관심 바랍니다.”>
		<meta name="google-site-verification" content="oh_2BqNhU-HCxyw9pyAYq-R8quUISyrJiuuTvu3L2Y0" />
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="mobile-web-app-capable" content="yes">
		<meta name=apple-mobile-web-app-capable content=yes>
		<meta name=apple-mobile-web-app-status-bar-style content=black>
		<link rel="stylesheet" href="/css/main.css" />		
		<!--[if lte IE 8]><script src="/js/ie/html5shiv.js"></script><![endif]-->
		 
		<!--[if lte IE 8]><link rel="stylesheet" href="/css/ie8.css" /><![endif]-->
		<!--[if lte IE 9]><link rel="stylesheet" href="/css/ie9.css" /><![endif]-->
		
		<!-- To support Android -->
		
		<link rel=”icon” sizes=”196×196″ href=”/img/ico/favicon_196_196.png”>
		<link rel=”icon” sizes=”192×192″ href=”/img/ico/favicon_192_192.png”>
		<link rel=”icon” sizes=”180×180″ href=”/img/ico/favicon_180_180.png”>
		<link rel=”icon” sizes=”152×152″ href=”/img/ico/favicon_152_152.png”>
		<link rel=”icon” sizes=”144×144″ href=”/img/ico/favicon_144_144.png”>
		<link rel=”icon” sizes=”128×128″ href=”/img/ico/favicon_128_128.png”>
		<link rel=”icon” sizes=”114×114″ href=”/img/ico/favicon_114_114.png”>
		<link rel=”icon” sizes=”96×96″ href=”/img/ico/favicon_96_96.png”>
		<link rel=”icon” sizes=”76×76″ href=”/img/ico/favicon_76_76.png”>
		<link rel=”icon” sizes=”72×72″ href=”/img/ico/favicon_72_72.png”>
		<link rel=”icon” sizes=”60×60″ href=”/img/ico/favicon_60_60.png”>
		<link rel=”icon” sizes=”57×57″ href=”/img/ico/favicon_57_57.png”>
		<link rel=”icon” sizes=”32×32″ href=”/img/ico/favicon_32_32.png”>
		<link rel=”icon” sizes=”16×16″ href=”/img/ico/favicon_16_16.png”>
		
		<link rel="shortcut icon" href="/img/ico/favicon_128_128.ico" />
		
		<meta name="msapplication-TileColor" content="#194386">
		<meta name="theme-color" content="black">
		<style>
			body{
				 ;
			}
			
		</style>
	</head>
	<body >	
		<!-- Header -->
		<div id="header">
			<div class="top">
				<!-- Logo -->
					<div id="logo">
						<span class="image avatar48"><img src="" alt="" /></span>
						<h1 id="title">Guest</h1>
						<p>SNS 로그인 구현 예정</p>
					</div>
					
					<script>
					/*
					<div id="chat_import" style="display:none;">
											<iframe id="chat_iframe" src="" style="width:100%;height:350px;display:block"></iframe>
										</div>
					*/
						var coinoneUrl = "https://coinone.co.kr/chat/";
						var steemUrl = "https://steemit.chat/channel/korea";
						function chatOnOff( target ){
							
							var chat_iframe = $("#chat_iframe");
							if( target == chat_iframe.attr("data-target") ){
								chat_iframe.attr("src", "");
								chat_iframe.attr("data-flag", "off");
								chat_iframe.attr("data-target", "");
								$("#chat_import").hide();
							}else{
								chat_iframe.attr("data-target", target)
								chat_iframe.attr("src", ( target=="coinone" ? coinoneUrl : steemUrl ));
								$("#chat_import").show();
							}	
						}
						
						var etherScanUrl = "/EOS_SCAN";
						function utilPageImport( target ){
							
							var util_iframe = $("#util_iframe");
							if( target == util_iframe.attr("data-target") ){
								util_iframe.attr("src", "");
								util_iframe.attr("data-flag", "off");
								util_iframe.attr("data-target", "");
								$("#util_import").hide();
							}else{
								util_iframe.attr("data-target", target)
								util_iframe.attr("src", ( target=="etherScan" ? etherScanUrl : "" ));
								$("#util_import").show();
							}	
						}
					</script>
					<style>
						.nav_detail{
							font-size:0.7em;
							font-weight:bold;
						}
					</style>
					<!-- Nav -->
					<nav id="nav">
						<ul>
							<li>
								<a href="javascript:;" id="top-link" class="skel-layers-ignoreHref nav_detail" >
									Ticker &nbsp;&nbsp;<img width="16px" height="16px" src="/img/nav/stock.png" />
								</a>
							</li>
							<li>
								<a href="javascript:;" onclick="chatOnOff('coinone');" id="coinone-link" class="skel-layers-ignoreHref nav_detail">
									Coinone Chat &nbsp;&nbsp;<img width="16px" height="16px" src="/img/nav/chat.png" />
								</a>
							</li>
							<li>
								<a href="javascript:;" onclick="chatOnOff('steem');"  id="about-link" class="skel-layers-ignoreHref nav_detail">
									Steem Chat &nbsp;&nbsp;<img width="16px" height="16px" src="/img/nav/chat.png" />
								</a>
							</li>
							<li>
								<a href="javascript:;" onclick="utilPageImport('etherScan');" id="contact-link" class="skel-layers-ignoreHref nav_detail">
									IOC Scanner(EOS) &nbsp;&nbsp;<img width="16px" height="16px" src="/img/nav/search.png" />
								</a>
							</li>
							
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
							<li><a href="javascript:;" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
						</ul>
					</nav>
			</div>
			<!-- End Top -->
			
			
			
			<div class="bottom">

				<!-- Social Icons -->
					<ul class="icons">
						<li><a href="#" class="icon fa-twitter"><span class="label">Twitter</span></a></li>
						<li><a href="#" class="icon fa-facebook"><span class="label">Facebook</span></a></li>
						<li><a href="#" class="icon fa-github"><span class="label">Github</span></a></li>
						<li><a href="#" class="icon fa-dribbble"><span class="label">Dribbble</span></a></li>
						<li><a href="#" class="icon fa-envelope"><span class="label">Email</span></a></li>
					</ul>
			</div>
		</div>
		<!-- End Header -->
		
		
		
		<!-- Main -->
			<div id="main">

				<!-- Intro -->
					<section id="top" class="one dark cover" style="margin:0 0 0 0;padding:10 0 0 0 ;">
						<div id="btn_wrapp_search" style="text-align:center;width:100%;color:black;font-size:0.9em;font-weight:bold;">
							<span style="position:relative;float: right;left:-0%;width:100%"><input type="text" placeholder="Cobot v1.0.0   " class="search_txt"  /></span> 
							<!-- span style="position:relative;float:right;font-size:0.7em;">cobot v1.0.1</span-->
						</div>
						
					</section>
					
					<style>
					.search_txt{
						width:50%;
						font-size:14px;
						background-image:url('/img/search_icon.png');
						background-repeat:no-repeat;
						background-size:14px 80%;
						background-position:98% 50%;
						border : 1px solid green !important;
						align:center; text-align:center;
					}
					.coinRank {
						display: table-block; 
						width: 100%; 
						font-size:14px;
						max-height:50vh;
						overflow:scroll;
						
					}
					.coinRank::-webkit-scrollbar {
					    width: 5px ;
					}
					
					
					::-webkit-scrollbar-thumb {
					    border-radius: 10px;
					    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); 
					}
					.coinRank .rankRow {
						display: table-row;
						height:1em;
						width:100%;
						
					}
					.coinRank .rankRow .header { color:red; }
					.coinRank .rCell {
						display: table-cell; 
						padding-top: 3px; 
						padding-bottom: 3px;
						border-bottom: 1px solid #DDD;
						background-color:white;
						vartical-align:middle;
						line-height: 20px;
					}
					.coinRank .col_ex { width: 3em;}
					.coinRank .col_coin {width: 15%;}
					.coinRank .col_btc {width: 20%;}
					.coinRank .col_usd {width: 20%;}
					.coinRank .col_krw {width: 20%;}
					.coinRank .col_ch {width: 15%;}
					.crheader{ text-align:center; background-color:#008299 !important; color:white;}
					.rIcon {width: 20px;height:16px; }
					.up{ color:red }
					.down{ color:blue }
					.rr_even{ background-color:#F8FFFF !important;}
					/*
					.up_bg{ background-color:#FFFCFC !important;}
					.down_bg{ background-color:#F8FFFF !important;}
					*/
					.up_bg{ background-color:#FFFFFF !important;}
					.down_bg{ background-color:#FFFFFF !important;}
					.up_flash{ background-color:#FFEAEA !important;}
					.down_flash{ background-color:#E6FFFF !important;}
					.none{display:none;}
					</style>
					
					<script>
		
						function doHideNShow(imgObj, id ){
							if( $(imgObj).attr("src").indexOf("hide") > -1 ){
								$("#" + id).css("display","none");
								$(imgObj).attr("src", "/img/btn/show.png");
							}else{
								$("#" + id).css("display","block");
								$(imgObj).attr("src", "/img/btn/hide.png");
							}
						}
			
						//콤마찍기
						function comma(n) {
							var parts=n.toString().split(".");
						    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
						}
						
						
						var cfg_order = [];
						
						function setOrder( obj, colId ){
							var headerObj = $( obj );
							var orderBy = "";
							$("#rankRow_header").children().each(
								function(){
									$(this).text($(this).attr("title"));	
								}		
							);
							if( cfg_order.colId == undefined ){
								orderBy = "asc";
							}else{
								if( cfg_order.colId != colId){
									orderBy = "asc";
								}else{
									orderBy = cfg_order.orderBy=="asc"?"desc":"asc";
								}
							}
							
							headerObj.text(headerObj.attr("title") + (orderBy=="asc"?"↑":"↓"));
							cfg_order = {"colId":colId , "orderBy" : orderBy};
							$.cookie("kr.co.cobot.cfg_order", JSON.stringify(cfg_order), { expires: 365 } );
							
							exeOrder(colId, orderBy, $("#coinRank div[data-cd=data]"));
						}
						
						function exeOrder( colId, orderBy, dataArray ){
							
							dataArray.sort(
								function(a, b){
									//alert(22);
									var aVal = a.getAttribute("data-" + colId).replace(/,/gi,""); 
									var bVal = b.getAttribute("data-" + colId).replace(/,/gi,"");
									if( aVal == "-"){
										aVal = "1";
									}
									if( bVal == "-"){
										bVal = "1";
									}
									if( !isNaN(aVal) ){
										aVal = parseFloat(aVal);
										bVal = parseFloat(bVal);
										
										return orderBy == "asc" ? aVal - bVal : bVal - aVal;
									}
									if( aVal > bVal ) return orderBy == "asc" ? 1 : -1 ;
									if( bVal > aVal ) return orderBy == "asc" ? -1 : 1 ;
									return 0;
								}		
							);
							$("#coinRank div[data-cd=data]").remove();
							$("#coinRank").append( dataArray );
						}
					</script>

				<!-- coin_table -->
					<section id="coin_table" class="two">
						<div class="container" style="width:100%;padding:0 0 0 0 !important;">							
							<div class="row" style="width:100%;margin:0 0 0 0 !important;">
								<div class="4u 12u$(mobile)" style="width:100%;;margin:0 0 0 0 !important;;padding:0 0 0 0 !important;">
									<article class="item" style="width:100%;">
										
										<div id="btn_wrapp_coinRank" style="text-align:right;">
											<span style="float:left;font-weight:bold;color:black;font-size:0.8em;">&nbsp;&nbsp;&nbsp;USD/KRW <data id="per_krw">1,120</data>&nbsp;&nbsp; USDT $<data id="per_usd">1.01</data></span>
											<!-- img src="/img/btn/setting.png" width="16px" height="16px"/-->
											&nbsp;&nbsp;&nbsp;
											<!-- img src="/img/btn/close.png" width="16px" height="16px"/-->&nbsp;&nbsp;&nbsp;
										</div>
										
										
										
										<div class="coinRank" id="coinRank" style="font-weight:bold;width:100%;" >
											<div class="rankRow" id="rankRow_header" style="font-weight:bold;width:100%;">
												<span class="rCell col_ex crheader" onclick="setOrder(this,'eid')" id="header_eid" title="Ex">Ex</span>
												<span class="rCell col_coin crheader" onclick="setOrder(this,'ccd')" id="header_ccd" title="Coin">Coin</span>
												<span class="rCell col_btc crheader" onclick="setOrder(this,'btc')" id="header_btc" title="BTC" style="text-align:center;" >BTC</span>
												<span class="rCell col_usd crheader" onclick="setOrder(this,'usdt')" id="header_usdt" title="USDT" style="text-align:center;">USDT</span>
												<span class="rCell col_krw crheader" onclick="setOrder(this,'krw')" id="header_krw" title="KRW" style="text-align:center;">KRW</span>
												<span class="rCell col_ch crheader" onclick="setOrder(this,'ch')" id="header_ch" title="CH" style="text-align:center;">CH</span>
											</div>
										</div>
									</article>
									<article  class="item">
										<div id="chat_import" style="display:none;">
											<iframe id="chat_iframe" src="" style="width:100%;height:350px;display:block"></iframe>
										</div>
									</article>
								</div>
								<div class="4u 12u$(mobile)" style="width:100%;;margin:0 0 0 0 !important;;padding:0 0 0 0 !important;">
									<a href="javascript:;" onclick="doHideNShow(document.getElementById('c_img'), 'coinRank_bak');">
									<div id="btn_wrapp_coinRank" style="text-align:right;" >
										<span title="클릭하시면 필요한 코인을 더 추가하실 수 있습니다." style="float:left;color:black;font-size:0.8em;font-weight:bold;" >코인 전체 내역(Click Me)</span>
										<img id="c_img" src="/img/btn/show.png" width="16px" height="16px" />
										&nbsp;&nbsp;&nbsp;
										<img src="/img/btn/close.png" width="16px" height="16px"/>
									</div>
									</a>
									<div class="coinRank none" id="coinRank_bak" style="font-weight:bold;width:100%;" >
										<div class="rankRow" id="rankRow_header" style="font-weight:bold;width:100%;">
											<span class="rCell col_ex crheader" >Ex</span>
											<span class="rCell col_coin crheader" >Coin</span>
											<span class="rCell col_btc crheader" style="text-align:center;">BTC</span>
											<span class="rCell col_usd crheader" style="text-align:center;">USD</span>
											<span class="rCell col_krw crheader" style="text-align:center;">KRW</span>
											<span class="rCell col_ch crheader" style="text-align:center;">CH</span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</section>

				<!-- Util Section -->
				<section id="secUtil" class="three">
					<div class="container">
						<div id="util_import" style="display:none;">
							<iframe id="util_iframe" src="" style="width:100%;height:350px;display:block"></iframe>
						</div>
					</div>
				</section>

				<!-- Contact -->
					<!-- section id="contact" class="four">
						<div class="container">

							<header>
								<h2>Contact</h2>
							</header>

							<p>Elementum sem parturient nulla quam placerat viverra
							mauris non cum elit tempus ullamcorper dolor. Libero rutrum ut lacinia
							donec curae mus. Eleifend id porttitor ac ultricies lobortis sem nunc
							orci ridiculus faucibus a consectetur. Porttitor curae mauris urna mi dolor.</p>

							<form method="post" action="#">
								<div class="row">
									<div class="6u 12u$(mobile)"><input type="text" name="name" placeholder="Name" /></div>
									<div class="6u$ 12u$(mobile)"><input type="text" name="email" placeholder="Email" /></div>
									<div class="12u$">
										<textarea name="message" placeholder="Message"></textarea>
									</div>
									<div class="12u$">
										<input type="submit" value="Send Message" />
									</div>
								</div>
							</form>

						</div>
					</section-->

			</div>
		<!-- div>
			<button type="button" onclick="openSocket();">Open</button>
			<button type="button" onclick="send();">Send</button>
			<button type="button" onclick="closeSocket();">Close</button>
		</div-->
		<div style="display:none;">
			입력 : <input type=text id="input_txt" style="width:100%;" /> <br />
			상태 : <input type=text id="prg_txt" style="width:100%;" />
		</div>
		<!-- Server responses get written here -->
		<div id="messages"></div>
	
		<!-- End Main -->
		<!-- Footer -->
		<!-- style>
			#footer{
				color:white !important;				
				font-weight:bold !important;
				background:url('/img/main.png') no-repeat !important ;
				background-size: 100% 100% !important;
			
			}
		</style-->
		<div id="footer" style="">
				<!-- Copyright -->
				<!-- ul class="copyright">
					<li><a href="/lib/cobot_chrome.crx">Chrome에서 사용하기</a></li>
				</ul-->
				
				<ul class="copyright">
					<li>현재 사이트 개발 중에 있으며 모바일 버전으로 먼저 개발하고 있습니다.</li>
					<li>필요한 기능이나 문제점은 메일로 보내주시면 다음 개발 시에 참고하여 진행하도록 하겠습니다.^^</li>
					<li> <a href="mailto:admin@cobot.co.kr">admin@cobot.co.kr</a><br /></li>
					
				</ul>
				<ul class="copyright">
					<li>BTC : 1Hi3zc7sSxd9ovG2kwZ62yVbaPM4dXrJK<br /></li>
					<li>ETH : 0xf3c8f49f41e2a9a4e8a7106918c327f1bc9d8a25<br /></li>
					<li></li>
					
				</ul>
				<ul class="copyright">
					<li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
					<li>&copy; Cobot. All rights reserved.</li>
				</ul>
		</div>

		<!-- Scripts -->
		<script src="/js/include.jsp"></script>
		<div id="coinRankTmp" style="display:none"></div>
		<%@ include file="/footer.jsp"  %>
	<script async="async">(function(a,b,c){if(c in b&&b[c]){var d,e=a.location,f=/^(a|html)$/i;a.addEventListener("click",function(a){d=a.target;while(!f.test(d.nodeName))d    =d.parentNode;"href"in d&&(d.href.indexOf("http")||~d.href.indexOf(e.host))&&(a.preventDefault(),e.href=d.href)},!1)}})(document,window.navigator,"standalone")</script>
	</body>
	<script async="async">
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-100941339-1', 'auto');
	  ga('send', 'pageview');
	
	</script>
</html>