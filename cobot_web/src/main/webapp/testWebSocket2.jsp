<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script src="/js/socket.io.js"></script>
</head>
<body>
	<div>
		<input type="text" id="messageinput" />
	</div>
	<div>
		<button type="button" onclick="openSocket();">Open</button>
		<button type="button" onclick="send();">Send</button>
		<button type="button" onclick="closeSocket();">Close</button>
	</div>
	<!-- Server responses get written here -->
	<div id="messages"></div>

	<!-- Script to utilise the WebSocket -->
	<script type="text/javascript">
	var text = '{ "employees" : [' +
	'{ "firstName":"John" , "lastName":"Doe" },' +
	'{ "firstName":"Anna" , "lastName":"Smith" },' +
	'{ "firstName":"Peter" , "lastName":"Jones" } ]}';
		var obj = JSON.parse(text);
	//alert(obj);
	
	
	
	var socket = io.connect('https://push.coinone.co.kr/ticker', {transports:['websocket','polling']});
	socket.on('update', function (data) {
        console.log("data : "+data);
        //socket.emit('my other event', { my: 'data' });
    });
	
	

	
	  
		var webSocket;
		var messages = document.getElementById("messages");

		function openSocket() {
			// Ensures only one connection is open at a time
			if (webSocket !== undefined
					&& webSocket.readyState !== WebSocket.CLOSED) {
				writeResponse("WebSocket is already opened.");
				return;
			}

			// Create a new instance of the websocket
			webSocket = new WebSocket("wss://push.coinone.co.kr/ticker");

			/**
			 * Binds functions to the listeners for the websocket.
			 */
			webSocket.onopen = function(event) {
				 
				// For reasons I can't determine, onopen gets called twice
				// and the first time event.data is undefined.
				// Leave a comment if you know the answer.
				if (event.data === undefined)
					return;

				writeResponse(event.data);
				
				this.send(JSON.stringify({command: "subscribe",channel: 40}));
			};

			webSocket.onmessage = function(event) {
				writeResponse(event.data);
			};

			webSocket.onclose = function(event) {
				writeResponse("Connection closed");
			};
		}
		function rcvEvent(args, kwargs) {
			if (args[0] == 'trollboxMessage' || args[0] == 1001) {
				if (args[1] == -1)
					return showTrollboxWarning(args[2]);
				
			    var data = new Object;
			    var index = args[1].toString();
			    data[index] = {
			        "username": args[2],
			        "message": args[3],
			        "reputation": args[4]
			    };
			    refreshTrollbox(data, false, false);
			}
		}
		

		/**
		 * Sends the value of the text input to the server
		 */
		function send() {
			var text = document.getElementById("messageinput").value;
			webSocket.send(text);
		}

		function closeSocket() {
			webSocket.close();
		}

		function writeResponse(text) {
			messages.innerHTML += "<br/>" + text;
		}
	</script>
</body>
</html>