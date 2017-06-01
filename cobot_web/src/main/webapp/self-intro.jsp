<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="util.*" %>
<!DOCTYPE HTML>
<html>
	<head>
		<title>My Portfolio</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="/css/ie8.css" /><![endif]-->
		<!--[if lte IE 9]><link rel="stylesheet" href="/css/ie9.css" /><![endif]-->
	</head>
	<body>

		<!-- Header -->
			<div id="header">

				<div class="top">

					<!-- Logo -->
						<div id="logo">
							<span class="image avatar48"><img src="images/myPhoto.jpg" alt="" /></span>
							<h1 id="title">Hyungju Na</h1>
							<p>Java Developer</p>
						</div>

					<!-- Nav -->
						<nav id="nav">
							<!--

								Prologue's nav expects links in one of two formats:

								1. Hash link (scrolls to a different section within the page)

								   <li><a href="#foobar" id="foobar-link" class="icon fa-whatever-icon-you-want skel-layers-ignoreHref"><span class="label">Foobar</span></a></li>

								2. Standard link (sends the user to another page/site)

								   <li><a href="http://foobar.tld" id="foobar-link" class="icon fa-whatever-icon-you-want"><span class="label">Foobar</span></a></li>

							-->
							<ul>
								<li><a href="#top" id="top-link" class="skel-layers-ignoreHref"><span class="icon fa-home">Profile</span></a></li>
								<li><a href="#portfolio" id="portfolio-link" class="skel-layers-ignoreHref"><span class="icon fa-th">Portfolio</span></a></li>
								<li><a href="#about" id="about-link" class="skel-layers-ignoreHref"><span class="icon fa-user">About Me</span></a></li>
								<li><a href="#contact" id="contact-link" class="skel-layers-ignoreHref"><span class="icon fa-envelope">Contact</span></a></li>
							</ul>
						</nav>

				</div>

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

		<!-- Main -->
			<div id="main">

				<!-- Intro -->
					<section id="top" class="one dark cover">
						<div class="container">

							<header>
								<header>
									<h2>Profile</h2>
								</header>
								<h2 class="alt" style="text-align:left;font-size:16px">
									<li >경력사항
										<dd>- 아프로시스템 2010년 11월 ~ 재직중(<%=DateUtil.getCurDate("YYYY-MM") %>)</dd>
										<dd>- 원캐싱 전산팀 2007년 10월 ~ 2010년 11월(3년 2개월)</dd>
									</li>
									<li >학력 1 : 남도대학 컴퓨터정보통신(2년제 졸업, 3.9/4.5)</li>
									<li >학력 2 : 서울디지털대학교 소프트웨어(4년제 졸업, 3.5/4.5)</li>
									<li >전문교육 : 삼성멀티캠퍼스 웹 전문가과정(2007년 3월 ~ 8월,5개월간)</li>
									<li >주요스킬
										<dd>- Framework : Spring, 전자정부, Anyframe</dd>
										<dd>- UI : MiFlatform, JSP, HTML, CSS</dd>
										<dd>- DB : Oracle, MS-SQL, MySQL, Tibero, Cubrid</dd>
									</li>
									<li >생년월일 : 1983년 8월 5일</li>
									<li >혈액형 : A형</li>
									<li >주소 : 서울시 노원구</li>
									<li >취미/특기 : 영화감상 및 개인 프로그램 개발(Java, C++, C#)</li>
								</h2>
								
							</header>

							<footer>
								<a href="#portfolio" class="button scrolly">Go Portfolio</a>
							</footer>

						</div>
					</section>

				<!-- Portfolio -->
					<section id="portfolio" class="two">
						<div class="container">

							<header>
								<h2>Portfolio</h2>
							</header>

							<p style="text-align:left;font-size:16px">
								&nbsp;&nbsp;그룹 내 OK저축은행 여신개발을 주업무로 담당하며 과거 그룹 내 계열사의 다양한 전산개발을 담당.
								계열사는 OK저축은행, OK캐피탈, 러시앤캐시, 미즈사랑, 원캐싱, 아프로신용정보, 예스자산대부, 아프로시스템이 있음.
								
							</p>

							<div class="row">
							
								
								<div class="4u 12u$(mobile)">
									<article class="item">
										<a class="image fit"><img src="images/cf_oc_1.jpg" alt="" /></a>
										<header>
											<h3>
												<b>OK캐피탈 인수합병에 따른 데이터 마이그레이션 담당(2015년 10월~2016년 8월)</b>
											</h3>
											<ul>
												<li>OK캐피탈 채권 2만 5천건 → OK저축은행 매각 시 데이터 마이그레이션 담당('16년 2월)</li>
												<li>OK캐피탈 PL채권 전체 → OK캐피탈 신규 대출시스템으로 데이터 마이그레이션 담당('16년 6월)</li>
											</ul>
										</header>
									</article>
									
									<article class="item">
										<a class="image fit"><img src="images/cf_ok_1.jpg" alt="" /></a>
										<header>
											<h3>
												<b>OK저축은행 여신개발 주업무 담당(2014년 7월~2015년 9월)</b>
											</h3>
											<ul>
												<li>OK저축은행 여신시스템 개발운영</li>
												<li>개발팀 내 선임급으로 업무분석/설계가 주 업무</li>
												<li>대표개발상품  : 마이너스OK론, 햇살론, 오토론</li>
												<li>총 여신규모 1,605억 → 약2조 증가</li>
												<li>현재 저축은행 업계 2위</li>
											</ul>
										</header>
									</article>
									<article class="item">
										<a class="image fit"><img src="images/cf_rush_1.jpg" alt="" /><img src="images/cf_miz_1.jpg" alt="" /></a>
										
										<header>
											<h3>그룹 내 계정계 개발 및 운영(2012년~2014년 7월)</h3>
											<ul >
												<li>주요개발 : 여신소액 신상품 개발, 포인트, 신규 대외 서비스 개발</li>
												<li>러시앤캐시, 미즈사랑, 원캐싱, 아프로캐피탈, 예스자산대부, 아프로신용정보</li>
												<li>총 여신규모 1,605억 → 11,007억으로 증가</li>
											</ul>
										</header>
									</article>
								</div>
								<div class="4u 12u$(mobile)">
									<article class="item">
										<a href="#" class="image fit"><img src="images/pic04.jpg" alt="" /></a>
										<header>
											<h3>Magna Nullam</h3>
										</header>
									</article>
									<article class="item">
										<a href="#" class="image fit"><img src="images/pic05.jpg" alt="" /></a>
										<header>
											<h3>Natoque Vitae</h3>
										</header>
									</article>
								</div>
								<div class="4u$ 12u$(mobile)">
									<article class="item">
										<a href="#" class="image fit"><img src="images/pic06.jpg" alt="" /></a>
										<header>
											<h3>Dolor Penatibus</h3>
										</header>
									</article>
									<article class="item">
										<a href="#" class="image fit"><img src="images/pic07.jpg" alt="" /></a>
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

							<a href="#" class="image featured"><img src="images/pic08.jpg" alt="" /></a>

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

		<!-- Footer -->
			<div id="footer">

				<!-- Copyright -->
					<ul class="copyright">
						<li>&copy; Untitled. All rights reserved.</li><li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
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