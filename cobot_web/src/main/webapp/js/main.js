/*
	Prologue by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
*/

(function($) {

	skel.breakpoints({
		wide: '(min-width: 961px) and (max-width: 1880px)',
		normal: '(min-width: 961px) and (max-width: 1620px)',
		narrow: '(min-width: 961px) and (max-width: 1320px)',
		narrower: '(max-width: 960px)',
		mobile: '(max-width: 736px)'
	});

	$(function() {
		var	$window = $(window),
			$body = $('body');
		// Disable animations/transitions until the page has loaded.
			$body.addClass('is-loading');
			$window.on('load', function() {
				$body.removeClass('is-loading');
			});
		// CSS polyfills (IE<9).
			if (skel.vars.IEVersion < 9)
				$(':last-child').addClass('last-child');
		// Fix: Placeholder polyfill.
			$('form').placeholder();
		// Prioritize "important" elements on mobile.
			skel.on('+mobile -mobile', function() {
				$.prioritize(
					'.important\\28 mobile\\29',
					skel.breakpoint('mobile').active
				);
			});

		// Scrolly links.
			$('.scrolly').scrolly();
		// Nav.
			var $nav_a = $('#nav a');
			// Scrolly-fy links.
				$nav_a
					.scrolly()
					.on('click', function(e) {

						var t = $(this),
							href = t.attr('href');
						if (href[0] != '#')
							return;
						e.preventDefault();
						// Clear active and lock scrollzer until scrolling has stopped
							$nav_a
								.removeClass('active')
								.addClass('scrollzer-locked');
						// Set this link to active
							t.addClass('active');
					});

			// Initialize scrollzer.
				var ids = [];
				$nav_a.each(function() {
					var href = $(this).attr('href');
					if (href[0] != '#')
						return;
					ids.push(href.substring(1));
				});
				$.scrollzer(ids, { pad: 200, lastHack: true });
		// Header (narrower + mobile).

			// Toggle.
				$(
					'<div id="headerToggle">' +
						'<a href="#header" class="toggle"></a>' +
					'</div>'
				)
					.appendTo($body);

			// Header.
				$('#header')
					.panel({
						delay: 500,
						hideOnClick: true,
						hideOnSwipe: true,
						resetScroll: true,
						resetForms: true,
						side: 'left',
						target: $body,
						visibleClass: 'header-visible'
					});

			// Fix: Remove transitions on WP<10 (poor/buggy performance).
				if (skel.vars.os == 'wp' && skel.vars.osVersion < 10){
					$('#headerToggle, #header, #main')
					.css('transition', 'none');
					alert("");
				}
	});
	
	
	//setTimeout( '$("#main > section.one").css("background-image","url()");', 1000);
	
	/*
	setTimeout("setThemeColor('green');", 150);
	
	setTimeout("setThemeColor('#005000');", 600);
	setTimeout("setThemeColor('green');", 900);
	
	setTimeout("setThemeColor('white');", 1050); 
	*/
	
	
	if(navigator.userAgent.match(/Android/i)){
		// Set a timeout...
		setTimeout(function(){
			// Hide the address bar!
			window.scrollTo(0, 1);
		}, 0);
	}
	
	cfg_order = $.cookie("kr.co.cobot.cfg_order");
	
	if( cfg_order == undefined || cfg_order == "" ){
		cfg_order = [];
	}else{
		cfg_order = JSON.parse(cfg_order);
		var hObj = $("#header_"+cfg_order.colId);
		hObj.text( hObj.attr("title") + ( cfg_order.orderBy == "asc"? "↑":"↓" ) );
	}
	
	initCoins();
	setInterval("initCoins();", 3000);
	//initCoins();
	//alert( $("#main > section.one").attr("class") );
})(jQuery);

function initCoins(){
	
	if (webSocket !== undefined
			&& webSocket.readyState !== WebSocket.CLOSED) {
		send();
	}else{
		openSocket();
	}
	
}

function setThemeColor( color ){
	$('meta[name=theme-color]').remove();	
	$('head').append('<meta name="theme-color" content="'+color+'">');
}

function exactRound(num, decimals) {
	if (decimals<0)decimals=0;
    var sign = num >= 0 ? 1 : -1;
    return (Math.round((num * Math.pow(10, decimals)) + (sign * 0.001)) / Math.pow(10, decimals)).toFixed(decimals);
}

function reqJson( url, param, funcSuc, funcDone, funcFail ){
	$.post(
			url
			, param
			, funcSuc ? funcSuc : function(res){
				console.log( "res : " + JSON.stringify(res) );
			}, 'json'
	)
	.done( 
		funcDone ? funcDone : function (){
			console.log( "done" );
		}
	)
	.fail( 
		funcFail ? funcFail : function(jqxhr, textStatus, error ) { 
			var err = textStatus + ", " + error;
			alert( "서비스 실패 : " + err ); 
		}
	) ;
}