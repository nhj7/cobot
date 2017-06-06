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
			//writeResponse(event.data);
			regCoins( JSON.parse(jo.value).eid1 );
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


//		Application

function regCoins( coins ){
	
	$("#coinRank div[data-cd=data]").remove();
	
	
	for(var i = 0; i < coins.length;i++){
		if( coins[i].unit_cid != "1" &&  coins[i].unit_cid != "99" ){
			continue;
		}
		var rankRow = document.createElement("div");
		rankRow.setAttribute("class","rankRow");
		rankRow.setAttribute("data-cd","data");
		rankRow.setAttribute("ondoubleclick","$(this).remove();");
		
		var even_class = (i % 2 == 0 ? "rr_even":"");
		
		var str_html = '<span class="rCell col_ex '+ even_class +' "><img class="rIcon" src="/img/exchange/1.png" title="폴로닉스" /></span>';
		
		str_html += '<span class="rCell col_coin '+even_class +'">'+ coins[i].ccd +'</span>';
		
		var price = coins[i].price;
		if( coins[i].unit_cid == "99" ){
			price = "-";
		}
		
		str_html += '<span class="rCell col_btc '+ even_class +'">'+ price +'</span>';
		
		var usd = "";
		if( coins[i].unit_cid == "99" ){
			usd = coins[i].price;
		}
		
		if( exactRound(usd, 0) == 0 ){
			usd = exactRound(usd, 4);
		}else{
			usd = exactRound(usd, 0);
		}
		
		str_html += '<span class="rCell col_usd '+ even_class +'">' + usd + '</span>';
		str_html += '<span class="rCell col_krw '+ even_class +'">' + '-' + '</span>';
		
		var ch = exactRound( coins[i].per_ch * 100 , 2 );
		var upndownCls = "";
		if( ch > 0 ){
			ch = "+" + ch;
			upndownCls = "up";
		}else if( ch < 0 ){
			upndownCls = "down";
			
		}
		str_html += '<span class="rCell col_ch '+ even_class +' '+upndownCls+' ">' + ch + '</span>';
		rankRow.innerHTML = str_html;
		
		document.getElementById("coinRank").appendChild(rankRow);
	}
}




