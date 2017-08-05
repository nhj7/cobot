var STEEM_URL = "https://steemit.com";

//콤마찍기
function comma(n) {
	var parts=n.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
}

$(function() {
	$(".searchBodyItem").click(
		function(obj){
			initPaging();
			var jObj = $(this);
			if( jObj.hasClass("searchBodyItemOn") ){
				jObj.removeClass("searchBodyItemOn");
			}else{
				$(this).addClass("searchBodyItemOn");	
			}
		}		
	);
	
	$(".sortItem").click(
			
			function(obj){
				initPaging();
				var jObj = $(this);
				$(".sortItem").removeClass("sortItemOn")
				jObj.addClass("sortItemOn");
			}		
		);
	
	$('[data-countdown]').each(function() {
		  var $this = $(this), finalDate = $(this).data('countdown');
		  $this.countdown(finalDate, function(event) {
		    $this.html(event.strftime('%D일 %H:%M:%S'));
		  });
		});
	
	
	$(".searchBodyItem,.sortItem").click(
		function(e){
			
			inqryPage();
			/*
			var jObj = $(this);
			var itemDvcd = jObj.attr("data-item-dvcd");
			var itemValue = jObj.attr("data-item-value");
			*/
			//alert( (jObj.hasClass("searchBodyItemOn")?"true":"false") + " : " + itemDvcd + " : " + itemValue);
		}		
	);
	
	// 첫 페이지 조회 call
	inqryPage();
	  
});

function create(tag){
	return document.createElement(tag);
}

function inqryPage(){
	
	var curPage = $(".paging-itemClass-current").text();
	
	var arrSeller = [];
	var arrMethod = "";
	var arrCategory = "";
	var arrStatus = [];
	var arrActionDate = "";
	$(".searchBodyItemOn[data-item-dvcd='seller']").each(
		function(e){
			arrSeller.push(this.getAttribute("data-item-value"));				
		}		
	);
	
	$(".searchBodyItemOn[data-item-dvcd='method']").each(
			function(e){
				arrMethod += this.getAttribute("data-item-value") + ",";				
			}		
		);
	$(".searchBodyItemOn[data-item-dvcd='category']").each(
			function(e){
				arrCategory += this.getAttribute("data-item-value") + ",";				
			}		
		);
	$(".searchBodyItemOn[data-item-dvcd='status']").each(
			function(e){
				arrStatus.push(this.getAttribute("data-item-value"));				
			}		
		);
	$(".searchBodyItemOn[data-item-dvcd='actionDate']").each(
			function(e){
				arrActionDate += this.getAttribute("data-item-value") + ",";				
			}		
		);
	
	var sortDvcd = $(".sortItemOn").attr("data-dvcd");
	
	console.log("arrSeller : "+arrSeller + "\narrMethod : " + arrMethod + "\narrCategory : " + arrCategory + "\narrStatus : " + arrStatus + "\narrActionDate : " + arrActionDate + "\nsortDvcd : "+ sortDvcd);
	
	var inqryParams = new Object();
	inqryParams.arrSeller = arrSeller.join();
	inqryParams.arrMethod = arrMethod;
	inqryParams.arrCategory = arrCategory;
	inqryParams.arrStatus = arrStatus.join();
	inqryParams.arrActionDate = arrActionDate;
	inqryParams.sortDvcd = sortDvcd;
	//alert(curPage);
	inqryParams.curPage = curPage==""?"1":curPage;
	GCurPage = curPage;
	inqryParams.pageSize = pageSize;
	inqryParams.postSize = postSize;
	//$("#itemListDiv").fadeOut(200);
	
	reqJson("/kr-market/Search", inqryParams , searchSuc );
}
function searchSuc(jObj){
	//alert(jObj.count);
	setPaging(jObj.pg);
	try{
		$("#sbd_txt").text( comma(jObj.KRW_SBD) );
	}catch(e){}
	$("#itemListDiv").empty();	
	for(var i = 0; i < jObj.itemList.length;i++){
		console.log( JSON.stringify(jObj.itemList[i]) );
		addItem(i, jObj.itemList[i]);	// call html javascript. 
	}
	$('[data-countdown]').each(function() {
		  var $this = $(this), finalDate = $(this).data('countdown');
		  $this.countdown(finalDate, function(event) {
		    $this.html(event.strftime('%D일 %H:%M:%S'));
		  });
		});
	
	
	//$("#itemListDiv").fadeIn(200);

}

function initPaging(){
	$('.paging-itemClass').each(
		function(e){
			$(this).removeClass("paging-itemClass-current");
		}
	);
}
function setPaging( pg ){
	//alert( count );
	$('#paging').paging({
		format:"{0}"
		, itemClass:"paging-itemClass"
		, itemCurrent:"paging-itemClass-current"
		, prev : ( pg.isPre == "true" ) ? "<이전" : ""
		, next : ( pg.isNext == "true" ) ? "다음>" : ""
		, first : "" 
		, last : ""
		, href : "javascript:$(window).scrollTop(0);inqryPage();"
		, length : pg.pageCount
		, max : pg.writing_Count 
		, current : GCurPage
	});		
}

var GCurPage;
function loadingView(flag){
	$("#loadSpinnerWrapper").css("top", $(window).height() / 2 - ( $("#loadSpinnerWrapper").height() ) );
	$("#loadSpinnerWrapper").css("left", $(window).width() / 2 - ( $("#loadSpinnerWrapper").width() / 2 ) );
	if( flag ){ 
		$("#loadSpinnerWrapper").show();
	}
	else{
		$("#loadSpinnerWrapper").hide();			
	}
	if( flag ){
		$("#loadSpinner").jmspinner();	
	}else{
		$("#loadSpinner").jmspinner(false);
	}
	
}

function reqJson( url, param, funcSuc, funcDone, funcFail ){
	loadingView(true);
	$.ajax({
			url : url
			, data : JSON.stringify(param) //param 
			, success : function(res){
				loadingView(false);
				funcSuc(res);
				//console.log( "res : " + JSON.stringify(res) );
			}
			, dataType : 'json'
			, method : 'post'
			, contentType : "application/json; charset=UTF-8"				
	})
	.done( 
		funcDone ? funcDone : function (){
			console.log( "done" );
		}
	)
	.fail( 
		funcFail ? funcFail : function(jqxhr, textStatus, error ) {
			loadingView(false);
			var err = textStatus + ", " + error;				
			alert( "서비스 실패 : " + err + "\n잠시 후에 다시 시도해보세요. 업데이트 중이거나 문제 해결 중입니다 ^^"); 
		}
	) ;
}
	
function exactRound(num, decimals) {
	if (decimals<0)decimals=0;
    var sign = num >= 0 ? 1 : -1;
    return (Math.round((num * Math.pow(10, decimals)) + (sign * 0.001)) / Math.pow(10, decimals)).toFixed(decimals);
}