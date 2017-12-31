var webSocket;
var messages = document.getElementById("messages");
 
function openSocket() {
	// Ensures only one connection is open at a time
	if (webSocket !== undefined
			&& webSocket.readyState !== WebSocket.CLOSED) {
		writeStatus("WebSocket is already opened.");
		return;
	}

	// Create a new instance of the websocket
	webSocket = new WebSocket(WebSocket_adr);

	/**
	 * Binds functions to the listeners for the websocket.
	 */
	webSocket.onopen = function(event) {
		// For reasons I can't determine, onopen gets called twice
		// and the first time event.data is undefined.
		// Leave a comment if you know the answer.
		send();
		
		if (event.data === undefined)
			return;
		
		var jo = JSON.parse(event.data);
		if( jo.cmd == "status" ){
			writeStatus( jo.value );
			
		}else{
			//writeResponse(event.data);
		}
		
		
		
	};

	webSocket.onmessage = function(event) {
		var jo = JSON.parse(event.data);
		
		for(var i = 0 ; i < jo.length;i++){
			
			if( jo[i].cmd  == "tick" ){
				setTick(jo[i]);
			}
			if( jo[i].cmd  == "tick_ch" ){
				setTick(jo[i]);
			}
			else if( jo[i].cmd  == "chart" ){
				setChart(jo[i]);
			} else if( jo[i].cmd  == "refresh" ){
				location.reload(true);
			}
			
		}
		
		
	};

	webSocket.onclose = function(event) {
		writeStatus("Connection closed");
	};
}

function setChart(jo){
	drawChart("chart_div",jo);
}


function setTick(jo){
	if( jo.value.per_krw != "" ){
		per_krw = jo.value.per_krw;
	}
	
	//alert(per_krw);
	$("#per_krw").text( comma(per_krw) );
	
	//alert(jo.value.eid_6);
	
	var ALL_COINS = jo.value.eid_2.concat(jo.value.eid_3).concat(jo.value.eid_4).concat(jo.value.eid_7).concat(jo.value.eid_6).concat(jo.value.eid_5).concat(jo.value.eid_1)
	
	regCoins( ALL_COINS  );
}

/**
 * Sends the value of the text input to the server
 */
var initFlag = true;
var cmd_tick = {"cmd":"tick", "initFlag" : initFlag };
var cmd = [cmd_tick];
function send() {
	
	
	
	if( TOUCH_FLAG == 1) return;
	
	if( webSocket.readyState !== WebSocket.CLOSED ){
		webSocket.send(JSON.stringify(cmd));
	}else{
		openSocket();
	}
	
	if( cmd_tick.initFlag) {
		
		cmd_tick.initFlag = false;	
	}
	
	
}

function addRequest(json){
	
	for(var i = 0; i < cmd.length;i++){
		if( cmd[i].cmd == json.cmd ){
			cmd[i] = json;
			send();
			return;
		}
	}
	cmd.push(json);
	send();
}

function removeRequest(json){
	for(var i = 0; i < cmd.length;i++){
		if( cmd[i].cmd == json.cmd ){
			cmd.splice(i, 1);
			return;
		}
	}
}

function closeSocket() {
	webSocket.close();
}

function writeStatus(text){
	document.getElementById("prg_txt").value = text;
}

function writeResponse(text) {
	
	messages.innerHTML += "<br/>" + text;
}


// Application
// cookie save. 
var activeCoins;
var per_usdt = 1;
var per_krw = 1130;

function getObjStr(obj){
	var objStr;
	for(var e in obj ){
		objStr += ( "obj["+e+"]  :" + obj[e] + "\n");
	}
	console.log(objStr);
	return objStr;
}





function regCoins( coins ){
	
	var coinRank = $("#coinRank");
	var coinRank_bak = $("#coinRank_bak");
	
	
	//alert($.cookie("kr.co.cobot.activeCoins"));
	
	var cData = $.cookie("kr.co.cobot.activeCoins");
	
	if( cData == undefined ){
		activeCoins = 
		{		
				"eid_1":[ "BTC" , "BTS"]
				, "eid_3":[ "XRP" ]
				, "eid_5":[ "NEO" , "STEEM", "SBD"]
				, "eid_6":[ "EOS" ]
		};
	}else{
		activeCoins = JSON.parse(cData);
	}
	
	//alert(activeCoins.eid_1);
	
	var BitCoin;
	for(var i = 0; i < coins.length;i++){
		
		//writeResponse( coins[i].price );
		if( coins[i].ccd == "BTC" && coins[i].unit_cid == "9999" ){
			BitCoin = coins[i]; 
		} 
	}
	if( BitCoin == null || BitCoin == undefined ){
		BitCoin = {price : document.getElementById( "rankRow_1_BTC" ).getAttribute("data-usdt") };
	}
	// for loop coins. 
	for(var i = 0; i < coins.length;i++){
		if( 
			// BTC, USD, KRW 
			!( coins[i].unit_cid == "1" || coins[i].unit_cid == "9999" || coins[i].unit_cid == "9998" )
		){
			continue;
		}
		
		if( coins[i].unit_cid == "9999" && coins[i].ccd != "BTC" ){
			continue;
		}		
		var rankRow;		
		var str_id = "rankRow_" + coins[i].eid + "_" + coins[i].ccd;
		var old_ch = 100;
		var price = coins[i].price;
		if( coins[i].unit_cid == "9999" || coins[i].unit_cid == "9998" ){
			price = "-";
		}
		
		var usd = "";
		if( coins[i].unit_cid == "9999" ){
			usd = coins[i].price;
		}else{
			usd = BitCoin.price * price;
		}
		
		var tmpUsd = exactRound(usd, 0);
		
		if( tmpUsd > 10 ){
			if( tmpUsd < 100 ){
				usd = exactRound(usd, 2);
			}else if( tmpUsd < 1000 ){
				usd = exactRound(usd, 1);
			}else{
				usd = tmpUsd;
			}
			
			
		}else{
			usd = exactRound(usd, 4);
		}
		var krw = usd * per_usdt * per_krw;
		if( coins[i].unit_cid == "9998" ){
			krw = coins[i].price;
			
			usd = krw / per_krw / per_usdt;
		}
		
		usd = formatValue(usd);
		krw = formatValue(krw);
		var upndownCls = "";
		var even_class = "up_bg";
		var ch = exactRound( coins[i].per_ch * 100 , 2 );
		if( ch > 0 ){
			ch = "+" + ch;
			upndownCls = "up";
		}else if( ch < 0 ){
			upndownCls = "down";
			even_class = "down_bg";
		}
		
		if( document.getElementById(str_id) == undefined ){
			rankRow = document.createElement("div");
			rankRow = $(rankRow);
			rankRow.attr("id", str_id );
			rankRow.attr("class","rankRow");
			rankRow.attr("data-cd","data");
			rankRow.attr("data-eid", coins[i].eid);
			rankRow.attr("data-ccd", coins[i].ccd );
			
			
			
			var str_html = '<span class="rCell col_ex '+ even_class +' " onclick="swapCoin(this.parentNode);" >'
			 + '<a href="javascript:;" ><img class="rIcon" src="/img/exchange/' + coins[i].eid + '.png" title="" /></span></a>';
			
			var viewChartClick = 'onclick="viewChart(\''+coins[i].eid+'\',\''+ coins[i].ccd +'\', \''+1+'\');"';
			
			str_html += '<span '+viewChartClick+' class="rCell col_coin '+even_class +'">'+ coins[i].ccd +'</span>';
			
			str_html += '<span '+viewChartClick+' class="rCell col_btc '+ even_class +'">'+ price +'</span>';
			
			str_html += '<span '+viewChartClick+' class="rCell col_usd '+ even_class +'">' + comma(usd) + '</span>';
			str_html += '<span '+viewChartClick+' class="rCell col_krw '+ even_class +'">' + comma(krw) + '</span>';
			str_html += '<span '+viewChartClick+' class="rCell col_ch '+ even_class +' '+upndownCls+' ">' + ch + '</span>';
			rankRow.html(str_html);
			
		}else{
			//rankRow = document.getElementById(str_id);			
			//rankRow = arrCoinRank[str_id];
			rankRow = $("div[id='"+str_id+"']");			
			old_ch = rankRow.attr("data-ch");
			old_btc = rankRow.attr("data-btc");
			old_krw = rankRow.attr("data-krw");
			
			rankRow.find(".col_coin").text(coins[i].ccd);
			rankRow.find(".col_btc").text(price);
			
			//console.log(str_id + " old_krw : " + old_krw + ", new_krw : " + krw + ", text : " + rankRow.find(".col_krw").text() );
			
			
			rankRow.find(".col_usd").text(comma(usd));
			rankRow.find(".col_krw").text(comma(krw));
			rankRow.find(".col_ch").text(ch);
			rankRow.find(".col_ch").removeClass("up");
			rankRow.find(".col_ch").removeClass("down");
			rankRow.find(".col_ch").addClass(upndownCls);
		}
		
		if( activeCoins["eid_" + coins[i].eid] == null ){
			activeCoins["eid_" + coins[i].eid] = new Array();
		}
		
		/*
		rankRow.on('dblclick', function() {			
			var jObj = $(this);
			if( this.parentNode.id == "coinRank" ){
				
				var eid = jObj.attr("data-eid");
				var ccd = jObj.attr("data-ccd");
				var tmpCoins = activeCoins["eid_" + eid ];
				//alert(tmpCoins + " : " + tmpCoins.indexOf( ccd ));
				tmpCoins.splice( tmpCoins.indexOf(ccd), 1 );
				coinRank_bak.append(jObj);
				coinRank.remove("div[id='"+jObj.attr("id")+"']");
								
			}else{
				if( activeCoins["eid_" + jObj.attr("data-eid") ] == null ){
					activeCoins["eid_" + jObj.attr("data-eid") ] = new Array();
				}
				activeCoins["eid_" + jObj.attr("data-eid") ].push(jObj.attr("data-ccd") );
				coinRank_bak.remove("div[id='"+jObj.attr("id")+"']");				
				coinRank.append(jObj);
			}			
			$.cookie("kr.co.cobot.activeCoins", JSON.stringify(activeCoins), { expires: 365 } );
		});
		*/
		
		
		
		var chkList = activeCoins["eid_" + coins[i].eid];		
		
		rankRow.attr("data-ccd", coins[i].ccd );
		rankRow.attr("data-btc", price );
		rankRow.attr("data-usdt", usd );
		rankRow.attr("data-krw", krw );
		rankRow.attr("data-ch", ch );
		
		//document.getElementById("rankRow_" + coins[i].eid )
		try{				
			var new_ch = parseFloat(ch);
			if( old_ch < new_ch ){
				rankRow.attr("data-ch-cd", "up" );
				//console.log(str_id + " : old_ch - " + old_ch + ", new_ch : " + ch + ", up");
			}else if( old_ch > new_ch ){
				rankRow.attr("data-ch-cd", "down" );
				//console.log(str_id + " : old_ch - " + old_ch + ", new_ch : " + ch + ", down");
			}else{
				rankRow.attr("data-ch-cd", "" );
			}
		}catch(e){
			alert(e);
		}
		if( initFlag == true ){
			rankRow.attr("data-sort", i);
		}
		if( chkList.indexOf( coins[i].ccd ) > -1 ){
			//$("#coinRank div[data-cd=data]").remove(rankRow);
			if( initFlag ){
				coinRank.append(rankRow);
			}			
			
			//document.getElementById("coinRank").appendChild(rankRow);
		}else{			
			if( initFlag ){
				coinRank_bak.append(rankRow);				
			}
			//document.getElementById("coinRank_bak").appendChild(rankRow);
		}
		
	} // end for coins
	
	
	calcKrPrimeum(); // 김치 프리미엄 계산
	
	// sort coins
	//cfg_order = {"colId":colId , "orderBy" : orderBy};
	if( cfg_order.colId == undefined ){
		//$("#coinRank div[data-cd=data]").remove();
		
	}else{
		exeOrder(cfg_order.colId, cfg_order.orderBy, "coinRank" , "div[data-cd=data]" );
		
	}
	
	//exeOrder("sort", "asc", arrCoinRank_bak);
	
	if( initFlag ){
		//$("#coinRank").append( mapCoinRank);
		//$("#coinRank_bak").append( mapCoinRank_bak);
	}
	flashCoins();
	//var arrCoinRank = new Array();
	//var arrCoinRank_bak = new Array(); // coinRank_bak
	initFlag = false;
}

function swapCoin( obj ){
	var jObj = $(obj);
	
	var coinRank = $("#coinRank");
	var coinRank_bak = $("#coinRank_bak");
	
	if( obj.parentNode.id == "coinRank" ){
		
		var eid = jObj.attr("data-eid");
		var ccd = jObj.attr("data-ccd");
		var tmpCoins = activeCoins["eid_" + eid ];
		//alert(tmpCoins + " : " + tmpCoins.indexOf( ccd ));
		tmpCoins.splice( tmpCoins.indexOf(ccd), 1 );
		coinRank_bak.append(jObj);
		coinRank.remove("div[id='"+jObj.attr("id")+"']");
						
	}else{
		if( activeCoins["eid_" + jObj.attr("data-eid") ] == null ){
			activeCoins["eid_" + jObj.attr("data-eid") ] = new Array();
		}
		activeCoins["eid_" + jObj.attr("data-eid") ].push(jObj.attr("data-ccd") );
		coinRank_bak.remove("div[id='"+jObj.attr("id")+"']");				
		coinRank.append(jObj);
	}			
	$.cookie("kr.co.cobot.activeCoins", JSON.stringify(activeCoins), { expires: 365 } );
}

function calcKrPrimeum(){
	var arr_coinDiv = $("div[data-cd=data]");	
	var arr_plnxDiv = arr_coinDiv.filter("div[data-eid=5]");
	var arr_bitfinexDiv = arr_coinDiv.filter("div[data-eid=6]");	
	var arr_kr_eid = [2,3,4];
	for( var i = 0; i < arr_kr_eid.length;i++){
		var arr_krDiv = arr_coinDiv.filter("div[data-eid="+arr_kr_eid[i]+"]");
		for(var j = 0; j < arr_krDiv.length;j++){
			var krDiv = $(arr_krDiv[j]);
			var dataCcd = krDiv.attr("data-ccd");
			if( dataCcd == "BCH" ){
				dataCcd = "BCC";
			}			
			var plnxDiv = $(arr_plnxDiv.filter("div[data-ccd="+dataCcd+"]")[0]);			
			if( dataCcd == "EOS" || dataCcd == "IOTA" ){
				if( dataCcd == "IOTA" ){dataCcd = "IOT";}				
				plnxDiv = $(arr_bitfinexDiv.filter("div[data-ccd="+dataCcd+"]")[0]);
			}
			var kr_col_btc = $(krDiv.children().filter(".col_btc")[0])
			kr_col_btc.css("color","green");
			try{
				var kr_pr = ( parseFloat(krDiv.attr("data-usdt")) / parseFloat(plnxDiv.attr("data-usdt")) - 1 ) * 100;
				var kr_pr_per = exactRound(kr_pr, 2);
				krDiv.attr("data-btc", kr_pr_per);
				kr_col_btc.text(  kr_pr_per + "%"  );
			}catch(e){}
		}
	}
}

function flashCoins(){
	
	//return;
	var isFlash = false;
	var arrCoinRank = $("div[data-cd=data]");
	for(var i = 0; i < arrCoinRank.length;i++){
		var coinDiv = $(arrCoinRank[i]);
		if( coinDiv.attr( "data-ch-cd" ) != "" ){
			//arrCoinRank[i].onFlash();
			if( coinDiv.attr("data-ch-cd") == "up"){
				flash_class = "up_flash";
			}else if( coinDiv.attr("data-ch-cd") == "down" ){
				flash_class = "down_flash";
			}else{
				continue;
			}
			coinDiv.attr("data-ch-cd", "");
			$("#"+coinDiv.attr("id") + " .rCell").addClass(flash_class);			
			var cmdStr = "$('#"+coinDiv.attr("id") + " .rCell').removeClass('"+flash_class+"');";
			//alert(cmdStr);
			setTimeout(cmdStr ,500);			
			isFlash = true;
		}
	}
	if(isFlash){
		//var ranColor = getRandomColor();
		//alert(ranColor);
		//$("#brandTxt").css("color",ranColor);
		//setTimeout('$("#brandTxt").css("color","white");',150);
	}
	
	
}

//영문과 숫자만 허용
function SetAlphaNum(obj){
	val=obj.value;
	re=/[^a-zA-Z0-9]/gi;
	obj.value=val.replace(re,"").toUpperCase();
}

function filterCoins(obj){
	
	SetAlphaNum(obj);
	var coinRank_bak = $("#coinRank_bak");
	console.log( coinRank_bak );
	var arr_bak_coins = coinRank_bak.find("div[data-cd=data]");
	
	console.log( arr_bak_coins.length );
	var value = obj.value;
	for(var i = 0; i < arr_bak_coins.length;i++){
		var jObj = $(arr_bak_coins[i]);
		if( jObj.attr("data-ccd").startsWith(value)){
			jObj.css("display", "table-row");
		}else{
			jObj.css("display", "none");
		}
	}
	
}


function formatValue( value ){
	var tmpValue = exactRound(value, 0);
	if( tmpValue > 10 ){
		if( tmpValue < 100 ){
			value = exactRound(value, 2);
		}else if( tmpValue < 1000 ){
			value = exactRound(value, 1);
		}else{
			value = tmpValue;
		}
	}else{
		value = exactRound(value, 4);
	}
	return value;
}

function viewChart( eid, ccd, unit_cid ){
	return;
	if( eid == "2" || eid == "4" || eid == "5"  ){
		alert("비트렉스, 빗썸, 코빗 거래소 차트는 준비중입니다.");
		return;
	}	
	if( eid == "1" && ccd == "BTC"  ){
		alert("폴로닉스 비트코인은 준비중입니다.");
		return;
	}
	
	//alert(eid + ", " + ccd);
	var chart_div = $("#chart_div");
	var key = eid + "_" + unit_cid + "_" + ccd;
	var chart_import = $("#chart_import");
	
	//alert(chart_div.attr("id"));
	
	// 차트가 없으면 생성
	if( chart_div.attr("id") == undefined ){
		
		var div = createChart("chart_div");
		div.setAttribute("data-eid", eid);
		div.setAttribute("data-unit_cid", unit_cid);
		div.setAttribute("data-ccd", ccd);
		div.setAttribute("data-key", key );
		
		chart_import.append(div);
		chart_import.css("display","block");
		
		var request = {"cmd":"chart", "eid" : eid, "ccd" : ccd , "unit_cid" : unit_cid};
		addRequest(request);
	}else{
		
		if ( chart_div.attr("data-key") == key ){
			
			var request = {"cmd":"chart", "eid" : eid, "ccd" : ccd , "unit_cid" : unit_cid};
			removeRequest(request);
			chart_import.css("display","none");
			chart_div.attr("data-key", "" );
		}else{
			
			chart_div.attr("data-eid", eid);
			chart_div.attr("data-unit_cid", unit_cid);
			chart_div.attr("data-ccd", ccd);
			chart_div.attr("data-key", key );
			
			var request = {"cmd":"chart", "eid" : eid, "ccd" : ccd , "unit_cid" : unit_cid};
			addRequest(request);
			
			chart_import.css("display","block");
			
		}

	}
}


var timeout,longtouch, pressTimer;

function setDragNDrop(id){
	
	/*
	jq_rankRow		
	.on('touchstart', function() {
		console.log("touchstart : " + $(this).attr("id") );
		var cmd = 'pressTimer = window.setTimeout(function() { setDragNDrop( "' + $(this).attr("id") + '" ); },1000);';
		eval(cmd);
		
	})
	.bind('touchend', function() {
		   clearTimeout(pressTimer);
	 });
	*/
	
	/*
	jq_rankRow.draggable(
			{ 
				scroll : true
				, opacity: 0.8
				, containment: "window"
				, revert: "invalid"
				, cursor: 'move'
				, connectToSortable: ".coinRank"
						
			}
	);
	*/
	//alert();
	console.log("setDragNDrop : "+id);
	
	
	$(".coinRank").sortable({
        revert: true
        , update : function( e, ui ){
        	var draggableId = $(ui.item).attr("id");
        	var droppableId = $(this).attr("id");
        	console.log("update : draggableId : "+draggableId + ", droppableId : "+droppableId);
        	
        	var strDraggableId = draggableId.split("_");
        	
        	var tmpCoins = activeCoins["eid_" + strDraggableId[1] ];
        	
        	if( droppableId == "coinRank" ){
        		activeCoins["eid_" + strDraggableId[1] ].push( strDraggableId[2] );
        	}else if( droppableId == "coinRank_bak" ){
        		tmpCoins.splice( tmpCoins.indexOf( strDraggableId[2] ), 1 );	
        	}
        	
        	$.cookie("kr.co.cobot.activeCoins", JSON.stringify(activeCoins), { expires: 365 } );
        	
        }
        
    });
	// getObjStr
	$(".coinRank").droppable({
		
        drop: function(e, ui ){
        	var draggableId = $(ui.draggable).attr("id");
        	var droppableId = $(this).attr("id");
        	//console.log("draggableId : "+draggableId + ", droppableId : "+droppableId);
        }
		, tolerance: "touch"
    });
	
	$("#"+id).draggable(
			{ 
				scroll : true
				, opacity: 0.8
				, containment: "window"
				, revert: "invalid"
				, cursor: 'move'
				, connectToSortable: ".coinRank"
						
			}
	);
	
}