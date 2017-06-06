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
			$("#coinRank div[data-cd=data]").remove();
			$("#coinRank_bak div[data-cd=data]").remove();
			regCoins( jo.value.eid_2.concat(jo.value.eid_1)  );
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
function regCoins( coins ){
	
	/*
	var activeCoins = JSON.parse($.cookie("kr.co.cobot.activeCoins"));
	if( activeCoins == null || activeCoins == ""){
		activeCoins = JSON.parse( "{eid_1:}" );
	}
	*/
	
	var BitCoin;
	for(var i = 0; i < coins.length;i++){
		
		//writeResponse( coins[i].price );
		if( coins[i].ccd == "BTC" && coins[i].unit_cid == "9999" ){
			BitCoin = coins[i]; 
		} 
	}
	
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
		rankRow.setAttribute("class","rankRow");
		rankRow.setAttribute("data-cd","data");
		
		$(rankRow).on('dblclick', function() {
			
			if( this.parentNode.id == "coinRank" ){
				document.getElementById("coinRank_bak").appendChild(this);
			}else{
				document.getElementById("coinRank").appendChild(this);
			}
			
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
		 + '<img class="rIcon" src="/img/exchange/' + coins[i].eid + '.png" title="폴로닉스" /></span>';
		
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
		
		str_html += '<span class="rCell col_usd '+ even_class +'">' + usd + '</span>';
		str_html += '<span class="rCell col_krw '+ even_class +'">' + krw + '</span>';
		
		
		str_html += '<span class="rCell col_ch '+ even_class +' '+upndownCls+' ">' + ch + '</span>';
		rankRow.innerHTML = str_html;
		if( coins[i].unit_cid == "9998" || coins[i].ccd == "BTC" ){
			document.getElementById("coinRank").appendChild(rankRow);
		}else{
			document.getElementById("coinRank_bak").appendChild(rankRow);
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



