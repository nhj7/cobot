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
		<meta name="viewport" content="width=device-width, initial-scale=1" />
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
		
	</head>
	<body>
	
		<!-- Header -->
		<div id="header">
			<div class="top">
				<!-- Logo -->
					<div id="logo">
						<span class="image avatar48"><img src="" alt="" /></span>
						<h1 id="title">Guest</h1>
						<p>SNS 로그인을 통한<br /> 잔고 정산 기능 추가 예정.<br /></p>
					</div>
	
					<!-- Nav -->
					<nav id="nav">
						<ul>
							<li><a href="#top" id="top-link" class="skel-layers-ignoreHref"><span class="icon fa-home">시세조회</span></a></li>
							<li><a href="#portfolio" id="portfolio-link" class="skel-layers-ignoreHref"><span class="icon fa-th">준비중</span></a></li>
							<li><a href="#about" id="about-link" class="skel-layers-ignoreHref"><span class="icon fa-user">준비중</span></a></li>
							<li><a href="#contact" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">준비중</span></a></li>
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
		</script>
		
		<!-- Main -->
			<div id="main">

				<!-- Intro -->
					<section id="top" class="one dark cover">
						<div id="btn_wrapp_search" style="text-align:center;width:95%;color:black;font-size:0.9em;font-weight:bold;">
							Cobot v 0.1.3
							<img id="serch_img" src="/img/btn/hide.png" width="16px" height="16px" onclick="doHideNShow(this, 'div_search');"/>
							
						</div>
						<div id="div_search" class="container">
							<div class="row" >
								<span style="width:100%;">
									<input type="text" placeholder="다음 코인 정렬 구현 예정" class="search_txt"  />
								</span>
							</div>
						</div>
					</section>
					
					<style>
					.search_txt{
						width:60%;
						font-size:14px;
						background-image:url('/img/search_icon.png');
						background-repeat:no-repeat;
						background-size:14px 80%;
						background-position:98% 50%;
					}
					.coinRank {
						display: table-block; 
						width: 100%; 
						font-size:14px;
						height:400px;
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

				<!-- coin_table -->
					<section id="coin_table" class="two">
						<div class="container">							
							<div class="row">
								<div class="4u 12u$(mobile)" style="width:100%;">
									<article class="item" style="width:100%;">
										
										<div id="btn_wrapp_coinRank" style="text-align:right;">
											<span style="float:left;font-weight:bold;color:black;font-size:0.8em;">USD/KRW 1,120, USDT $1.03</span>
											<img src="/img/btn/setting.png" width="16px" height="16px"/>
											&nbsp;&nbsp;&nbsp;
											<img src="/img/btn/close.png" width="16px" height="16px"/>
										</div>
										
										<div class="coinRank" id="coinRank" style="font-weight:bold;width:100%;" >
											<div class="rankRow" id="rankRow_header" style="font-weight:bold;width:100%;">
												<span class="rCell col_ex crheader" >Ex</span>
												<span class="rCell col_coin crheader" style="">Coin</span>
												<span class="rCell col_btc crheader" style="text-align:center;">BTC</span>
												<span class="rCell col_usd crheader" style="text-align:center;">USD(T)</span>
												<span class="rCell col_krw crheader" style="text-align:center;">KRW</span>
												<span class="rCell col_ch crheader" style="text-align:center;">CH</span>
											</div>
											
										</div>
										
									</article>
									
									<article  class="item">
										
										
										
									</article>
									
								</div>
								<div class="4u 12u$(mobile)">
								
								
									<div id="btn_wrapp_coinRank" style="text-align:right;">
											<span title="클릭하시면 필요한 코인을 더 추가하실 수 있습니다." style="float:left;" onclick="doHideNShow(document.getElementById('serch_img'), 'coinRank_bak');">* 코인 추가 및 삭제</span>
											<img id="serch_img" src="/img/btn/show.png" width="16px" height="16px" onclick="doHideNShow(this, 'coinRank_bak');"/>
											&nbsp;&nbsp;&nbsp;
											<img src="/img/btn/close.png" width="16px" height="16px"/>
										</div>
										
										<div class="coinRank none" id="coinRank_bak" style="font-weight:bold;width:100%;" >
											<div class="rankRow" id="rankRow_header" style="font-weight:bold;width:100%;">
												<span class="rCell col_ex crheader" >Ex</span>
												<span class="rCell col_coin crheader" style="">Coin</span>
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

				<!-- About Me -->
					<!-- section id="about" class="three">
						<div class="container">

							<header>
								<h2>About Me</h2>
							</header>

							<a href="#" class="image featured"><img src="img/pic08.jpg" alt="" /></a>

							<p>Tincidunt eu elit diam magnis pretium accumsan etiam id urna. Ridiculus
							ultricies curae quis et rhoncus velit. Lobortis elementum aliquet nec vitae
							laoreet eget cubilia quam non etiam odio tincidunt montes. Elementum sem
							parturient nulla quam placerat viverra mauris non cum elit tempus ullamcorper
							dolor. Libero rutrum ut lacinia donec curae mus vel quisque sociis nec
							ornare iaculis.</p>

						</div>
					</section-->

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
			<div id="footer">
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
	</body>
</html>