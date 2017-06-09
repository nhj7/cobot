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
		if (event.data === undefined)
			return;
		
		var jo = JSON.parse(event.data);
		if( jo.cmd == "status" ){
			writeStatus( jo.value );
			send();
		}else{
			//writeResponse(event.data);
		}
		
		
	};

	webSocket.onmessage = function(event) {
		var jo = JSON.parse(event.data);
		if( jo.cmd == "status" ){
			writeStatus( jo.value );
			send();
		}else{
			//alert( jo.value.eid_1 );
			//writeResponse( event.data );
			
			//$("#coinRank div[data-cd=data]").css("background-color","white");
			//$("#coinRank_bak div[data-cd=data]").css("background-color","white");
			
			
			
			regCoins( jo.value.eid_2.concat(jo.value.eid_3).concat(jo.value.eid_4).concat(jo.value.eid_1)  );
		}
	};

	webSocket.onclose = function(event) {
		writeStatus("Connection closed");
	};
}

/**
 * Sends the value of the text input to the server
 */
function send() {
	var text = document.getElementById("input_txt").value;
	webSocket.send(text);
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
var arrCoinRank = new Array();
var arrCoinRank_bak = new Array(); // coinRank_bak
function regCoins( coins ){
	
	arrCoinRank = [];
	arrCoinRank_bak = [];
	//alert($.cookie("kr.co.cobot.activeCoins"));
	
	var cData = $.cookie("kr.co.cobot.activeCoins");
	
	
	
	if( cData == undefined ){
		activeCoins = 
		{		
				"eid_1":[ "BTC" , "ETH"]
				, "eid_2":[ "BTC" , "ETH", "XRP"]
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
		
		var rankRow = document.createElement("div");
		
		var str_id = "rankRow_" + coins[i].eid + "_" + coins[i].ccd;
		
		rankRow.setAttribute("id", str_id );
		rankRow.setAttribute("class","rankRow");
		rankRow.setAttribute("data-cd","data");
		
		//.up_flash{ background-color:#FFFCFC !important;}
		//.down_flash{ background-color:#F8FFFF !important;}
		
		rankRow.setAttribute("data-eid", coins[i].eid);
		rankRow.setAttribute("data-ccd", coins[i].ccd );
				
		$(rankRow).on('dblclick', function() {
			
			if( this.parentNode.id == "coinRank" ){
				var eid = $(this).attr("data-eid");
				var ccd = $(this).attr("data-ccd");
				var tmpCoins = activeCoins["eid_" + eid ];
				
				//alert(tmpCoins + " : " + tmpCoins.indexOf( ccd ));
				
				tmpCoins.splice( tmpCoins.indexOf(ccd), 1 );				
				document.getElementById("coinRank_bak").appendChild(this);
			}else{
				
				activeCoins["eid_" + $(this).attr("data-eid") ].push( $(this).attr("data-ccd") );
				document.getElementById("coinRank").appendChild(this);
			}
			
			$.cookie("kr.co.cobot.activeCoins", JSON.stringify(activeCoins), { expires: 365 } );
			
			/*
			try{
				$("#coinRank_bak").append($(this).clone());
			}catch(e){
				alert(e);
			}
			
			$(this).remove();
			*/
			
		});
		
		var ch = exactRound( coins[i].per_ch * 100 , 2 );
		var upndownCls = "";
		var even_class = "up_bg";
		
		if( ch > 0 ){
			ch = "+" + ch;
			upndownCls = "up";
		}else if( ch < 0 ){
			upndownCls = "down";
			even_class = "down_bg";
		}
		
		var str_html = '<span onclick="$(this.parentNode).dblclick();" class="rCell col_ex '+ even_class +' ">'
		 + '<img class="rIcon" src="/img/exchange/' + coins[i].eid + '.png" title="" /></span>';
		
		str_html += '<span class="rCell col_coin '+even_class +'">'+ coins[i].ccd +'</span>';
		
		var price = coins[i].price;
		if( coins[i].unit_cid == "9999" || coins[i].unit_cid == "9998" ){
			price = "-";
		}
		
		str_html += '<span class="rCell col_btc '+ even_class +'">'+ price +'</span>';
		
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
		
		
		
		
		var krw = usd * 1.03 * 1120;
		if( coins[i].unit_cid == "9998" ){
			krw = coins[i].price;
			
			usd = krw / 1120;
		}
		
		usd = formatValue(usd);
		krw = formatValue(krw);
		
		str_html += '<span class="rCell col_usd '+ even_class +'">' + comma(usd) + '</span>';
		str_html += '<span class="rCell col_krw '+ even_class +'">' + comma(krw) + '</span>';
		
		
		str_html += '<span class="rCell col_ch '+ even_class +' '+upndownCls+' ">' + ch + '</span>';
		rankRow.innerHTML = str_html;
		
		if( activeCoins["eid_" + coins[i].eid] == null ){
			activeCoins["eid_" + coins[i].eid] = new Array();
		}
		var chkList = activeCoins["eid_" + coins[i].eid];
		
		
		rankRow.setAttribute("data-ch", ch );
		
		//document.getElementById("rankRow_" + coins[i].eid )
				
		if( chkList.indexOf( coins[i].ccd ) > -1 ){
			
			
			
			var oldRankRow = document.getElementById(str_id);
			//alert(oldRankRow);
			
			if( oldRankRow != null ){
				//alert(str_id + " : old_ch - " + oldRankRow.getAttribute("data-ch") + ", new_ch : " + ch );
				
				try{
					var old_ch = parseFloat(oldRankRow.getAttribute("data-ch"));
					var new_ch = parseFloat(ch);
					if( old_ch < new_ch ){
						rankRow.setAttribute("data-ch-cd", "up" );
					}else if( old_ch > new_ch ){
						rankRow.setAttribute("data-ch-cd", "down" );
					}else{
						
					}
				}catch(e){
					alert(e);
				}
				
				
			}
			
			arrCoinRank.push(rankRow);
			//document.getElementById("coinRank").appendChild(rankRow);
		}else{
			arrCoinRank_bak.push(rankRow);
			//document.getElementById("coinRank_bak").appendChild(rankRow);
		}
		
	} // end for coins
	
	$("#coinRank div[data-cd=data]").remove();
	$("#coinRank_bak div[data-cd=data]").remove();
	
	for(var i = 0; i < arrCoinRank.length;i++){
		document.getElementById("coinRank").appendChild( arrCoinRank[i] );
	}
	
	for(var i = 0; i < arrCoinRank_bak.length;i++){
		document.getElementById("coinRank_bak").appendChild( arrCoinRank_bak[i] );
	}
	
	flashCoins();
	//var arrCoinRank = new Array();
	//var arrCoinRank_bak = new Array(); // coinRank_bak
}

function flashCoins(){
	
	//return;
	
	for(var i = 0; i < arrCoinRank.length;i++){
		if( arrCoinRank[i].getAttribute( "data-ch-cd" ) != null ){
			//arrCoinRank[i].onFlash();
			if( arrCoinRank[i].getAttribute("data-ch-cd") == "up"){
				flash_class = "up_flash";
			}else{
				flash_class = "down_flash";
			}
			
			
			$("#"+arrCoinRank[i].getAttribute("id") + " .rCell").addClass(flash_class);
			
			var cmdStr = "$('#"+arrCoinRank[i].getAttribute("id") + " .rCell').removeClass('"+flash_class+"');";
			//alert(cmdStr);
			setTimeout(cmdStr ,500);
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



