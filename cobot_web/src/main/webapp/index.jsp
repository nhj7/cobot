<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="util.*" %>
<%
	System.out.println("index.jsp call");
%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>코봇-필요한 정보를 손쉽게</title>
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
		<meta name="theme-color" content="white">
		
	</head>
	<body>
	
		<!-- Header -->
		<div id="header">
			<div class="top">
				<!-- Logo -->
					<div id="logo">
						<span class="image avatar48"><img src="" alt="" /></span>
						<h1 id="title">Guest</h1>
						<p>회원가입을 해주세요.</p>
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
		
			function doHideNShow(){
				if( $("#serch_img").attr("src").indexOf("hide") > -1 ){
					$("#div_search").css("display","none");
					$("#serch_img").attr("src", "/img/btn/show.png");
				}else{
					$("#div_search").css("display","block");
					$("#serch_img").attr("src", "/img/btn/hide.png");
				}
			}
		</script>
		
		<!-- Main -->
			<div id="main">

				<!-- Intro -->
					<section id="top" class="one dark cover">
						<div id="btn_wrapp_search" style="text-align:right;width:95%">
							<img id="serch_img" src="/img/btn/hide.png" width="16px" height="16px" onclick="doHideNShow();"/>
							
						</div>
						<div id="div_search" class="container">
							<div class="row" >
								<span style="width:100%;">
									<input type="text" placeholder="검색기능 구현 중" class="search_txt"  />
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
					#coinRank {display: table; width: 100%; font-size:14px;}
					#coinRank .rankRow {display: table-row;}
					#coinRank .rCell {
						display: table-cell; 
						padding: 3px; 
						border-bottom: 1px solid #DDD;
						background-color:white;
						vartical-align:middle;
						line-height: 20px;
					}
					#coinRank .col_ex { width: 5%;}
					#coinRank .col_coin {width: 10%;}
					#coinRank .col_btc {width: 15%;}
					#coinRank .col_usd {width: 20%;}
					#coinRank .col_krw {width: 20%;text-align:right;}
					#coinRank .col_ch {width: 20%;}
					.rIcon {width: 20px;height:16px; }
					.up{ color:red }
					.down{ color:blue }
					</style>

				<!-- coin_table -->
					<section id="coin_table" class="two">
						<div class="container">							
							<div class="row">
								<div class="4u 12u$(mobile)">
									<article class="item">
										
										<div id="btn_wrapp_coinRank" style="text-align:right;">
											<img src="/img/btn/setting.png" width="16px" height="16px"/>
											&nbsp;&nbsp;&nbsp;
											<img src="/img/btn/close.png" width="16px" height="16px"/>
										</div>
										
										
										<div id="coinRank">
											<div class="rankRow" style="font-weight:bold;">
												<span class="rCell col_ex" style="text-align:center;">Ex</span>
												<span class="rCell col_coin" style="text-align:center;">Coin</span>
												<span class="rCell col_btc" style="text-align:center;">BTC</span>
												<span class="rCell col_usd" style="text-align:center;">$</span>
												<span class="rCell col_krw" style="text-align:center;">KRW</span>
												<span class="rCell col_ch" style="text-align:center;">CH</span>
											</div>
											<div class="rankRow">
												<span class="rCell col_ex"><img class="rIcon" src="/img/exchange/1.png" title="폴로닉스" /></span>
												<span class="rCell col_coin">BTC</span>
												<span class="rCell col_btc">1</span>
												<span class="rCell col_usd">2,324</span>
												<span class="rCell col_krw">2,733,333</span>
												<span class="rCell col_ch up">+6.29</span>
											</div>
											
											<div class="rankRow">
												<span class="rCell col_ex"><img class="rIcon" src="/img/exchange/1.png" title="폴로닉스" /></span>
												<span class="rCell col_coin">ETH</span>
												<span class="rCell col_btc">0.87691000</span>
												<span class="rCell col_usd">212</span>
												<span class="rCell col_krw">247,294</span>
												<span class="rCell col_ch down">-2.45</span>
											</div>
											
											<div class="rankRow">
												<span class="rCell col_ex"><img class="rIcon" src="/img/exchange/2.png" title="빗썸" /></span>
												<span class="rCell col_coin">BTC</span>
												<span class="rCell col_btc">1</span>
												<span class="rCell col_usd">-</span>
												<span class="rCell col_krw">3,095,000</span>
												<span class="rCell col_ch up">+0.29</span>
											</div>
										</div>
									</article>
									
									<article  class="item">
										
										
										
									</article>
									
								</div>
								<div class="4u 12u$(mobile)">
									<article id="coinone_chat" class="item" >
										<div id="btn_wrapp_coinRank" style="text-align:right;">
											<img src="/img/btn/세팅.png" width="16px" height="16px"/>
											&nbsp;&nbsp;&nbsp;
											<img src="/img/btn/닫기.png" width="16px" height="16px" onclick="$('#coinone_chat').remove();"/>
										</div>
										<iframe src="https://coinone.co.kr/chat/" width="90%" height=300px></iframe>
									</article>
									<!-- article class="item" style="background-color:red">
										폴로닉스 채팅 닫기
										<iframe src="https://poloniex.com/trollbox" width="90%" height=300px></iframe>
										
									</article-->
								</div>
								<div class="4u$ 12u$(mobile)">
									<article class="item">
										<a href="#" class="image fit"><img src="img/pic06.jpg" alt="" /></a>
										<header>
											<h3>Dolor Penatibus</h3>
										</header>
									</article>
									<article class="item">
										<a href="#" class="image fit"><img src="img/pic07.jpg" alt="" /></a>
										<header>
											<h3>Orci Convallis</h3>
										</header>
									</article>
								</div>
							</div>

						</div>
					</section>

				<!-- About Me -->
					<section id="about" class="three">
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
					</section>

				<!-- Contact -->
					<section id="contact" class="four">
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
					</section>

			</div>
		<!-- End Main -->
		<!-- Footer -->
			<div id="footer">

				<!-- Copyright -->
					<ul class="copyright">
						<li>&copy; Cobot. All rights reserved.</li><li></a></li>
					</ul>

			</div>

		<!-- Scripts -->
			<script src="/js/jquery.min.js"></script>
			<script src="/js/jquery.scrolly.min.js"></script>
			<script src="/js/jquery.scrollzer.min.js"></script>
			<script src="/js/skel.min.js"></script>
			<script src="/js/util.js"></script>
			<!--[if lte IE 8]><script src="/js/ie/respond.min.js"></script><![endif]-->
			<script src="/js/main.js"></script>
			
	</body>
</html>