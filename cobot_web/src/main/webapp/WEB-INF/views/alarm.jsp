<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Push Cobot</title>
  <link rel="stylesheet" href="/css/alarm/material.indigo-pink.min.css">
  <script defer src="/js/alarm/material.min.js" charset="UTF-8"></script>
  <!-- script src="/js/jquery.min.js"></script>
  <script src="/js/jquery.cookie.1.4.1.js"></script-->
  <link rel="stylesheet" href="/css/push_css.css" charset="UTF-8">
  <link rel="manifest" href="/manifest.json">
  <script>
  
	  
	  
	  function alarmUpdateSuc(res){
		  //alert(res);
	  }
	  
	  
	  
	  function openPopup( div ){
		  
		  var dataSeq = div.getAttribute("data-seq");
		  var itemName = div.querySelector("span[id='item_"+dataSeq+"_val']").innerText;
		  if( itemName.indexOf("@") > -1 ){
			  window.open("https://steemit.com/"+itemName );
		  }else{
			  var selName = div.querySelector("select[id='item_"+dataSeq+"_sel']").value;
			  window.open("https://steemit.com/"+selName+"/" + itemName);  
		  }
		  
	  }
	  
	  
  </script>
</head>
<body >
  <main>
  	<p>Cobot에서 Steemit의 kr 새글을 대상으로 시범적으로 알람 서비스의 베타 운영을 시행 하고자 합니다. 
  	해당 서비스는 안드로이드 브라우저나 크롬 브라우저에서 서비스 가능하며  동의하시는 분은 'Cobot 알람 받기'를 터치해주면 됩니다. 
  	또한 언제든지 'Cobot 알람 중지'나 사용중이신 브라우저 상에서의 알람 거부 설정을 통해서 알람을  거부하실수도 있습니다. 
  	앱과 달리 브라우저의 알람 기능을 이용하는 것으로 어떠한 해킹의 위험도 없음을 알려드립니다.
  	</p>
    <p>
      <button disabled class="js-push-btn mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
        	Cobot 알람 받기
      </button>
    </p>
    <style>
    	.site_dvcd{
    		
    	}
    	.depth1{
    		padding-left:7%;
    	}
    </style>
    <p>    
    	<div style="padding-bottom:1em;" >
    		<input type="checkbox" onclick="updateAlarm();" id="steemitAlarmFlag" >  Steemit 알람 
    	</div>
    	<div style="padding-left:7%;">    		
    		<span>
    			<input type="text" id="steemitInputText" style="text-align:center;width:85%;" 
    			placeholder="kr-dev or @nhj12311" />
    		</span>
    		<span style="float:right;padding-right:2%;" onclick="input_steemit_item();" >
    			<img width="16px" height="16px" src="/img/btn/add.png" />
    		</span>
    		
    	</div>
    	<div style="padding-left:7%;">
    		<hr />
    	</div>
    	<script>
    		function $(id){
    			return document.getElementById(id);
    		}
    	    function input_steemit_item(){
    	    	var stmInputText = $("steemitInputText").value;
    	    	if( stmInputText == "" ){
    	    		alert("steemit 알람으로 등록하실 항목 명을 입력해주세요.");
    	    		return;
    	    	}
    	    	
    	    	var checkKr = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
    	    	var checkSp = /[~!\#$%<>^&*\()\=+_\’]/gi;
    	    	var dashIdx = stmInputText.indexOf("-");
				if( checkKr.test(stmInputText) || checkSp.test(stmInputText) ){
					alert("스티밋 항목으로 등록하실 수 있는 문자는 알파벳, 숫자 및 '-','@' 기호입니다. ");
					return;
				}
				else if( stmInputText.indexOf("@") > -1 && stmInputText.substring(0, 1 ) != "@"){
					alert("ID입력 시 '@'는 첫글자로 입력해주세요.");
					return;
				}
				else if( dashIdx == stmInputText.length-1 || dashIdx == 0 ){					
					alert("태그 등록 시 '-'기호는 중간에 입력하셔야 합니다.");
					return;	
				}
				else{
					//alert(stmInputText);	
				}
				
				var childNodes = $("steemit_item_list").querySelectorAll("div");
				
				for(var i = 0; i < childNodes.length;i++){
					var valTxt = childNodes[i].querySelector("span[id='item_"+(i+1)+"_val']").innerText;
					console.log("valTxt : " + valTxt + ", stmInputText : " + stmInputText);
					if( valTxt == stmInputText  ){
						alert("이미 등록한 항목값입니다.");
						return;
					}
				}
				
				if( childNodes.length > 10 ){
					alert("10개 이상 등록 하실수 없습니다.(운영해본 후에 늘려나갈순 있습니다.)");
					return;
				}
				
				add_steemit_item(stmInputText);
				
				updateAlarm();
    	    }
    	    
    	    function delSteemitItem(itemId){    	    	
    	    	var elem = document.getElementById(itemId);
    	    	elem.parentNode.removeChild(elem);
    	    	updateAlarm();
    	    }
    	    
    	    var scrollHeight = 0;
    		  window.onload=function(){
    			  var steemDvcd = parent.$.cookie("kr.co.cobot.alarm.steemDvcd");
    			  parent.$("#alarm_iframe").contents().find("input:radio[name=steemDvcd][value="+steemDvcd+"]").attr("checked", true);
    			  
    			  scrollHeight = document.body.scrollHeight;
    			  
    			  // parent.$.cookie("kr.co.cobot.alarm.alarmSetting", JSON.stringify(alarmJson), {expires: 3650}); 
    			  
    			  
    			  var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
    			  if( alarmID != null && alarmID != "" ){
    				  parent.reqJson("/Alarm/Search", { alarmID : alarmID } , alarmSearchSuc );  
    			  }else{
    				  var jsonStr = parent.$.cookie("kr.co.cobot.alarm.alarmSetting");
        			  if( jsonStr != undefined && jsonStr != "" ){
        				  var alarmJson = JSON.parse(jsonStr);
        				  setAlarmInfo(alarmJson);
        			  }
    			  }
    			  
    		  }
    		  
    		  function setAlarmInfo(alarmJson){
				  $("steemitAlarmFlag").setAttribute("checked", alarmJson.steemit.alarmFlag);
				  var item_list = alarmJson.steemit.item_list;
				  for(var i = 0; i < item_list.length;i++){
					  
					  add_steemit_item( item_list[i].itemValue , item_list[i].alamFlag);
				  }
    		  }
    		  
    		  function alarmSearchSuc(res){
    			  //alert(res.steemit.alarmFlag);
    			  setAlarmInfo(res);
				  
    		  }
    		  
    	    
    	    function add_steemit_item(stmInputText, chk ){
    	    	
    	    	if( chk != true && chk != "true" ){    	    		
    	    		chk = false;
    	    	}else{
    	    		chk = true;
    	    	}
    	    	
    	    	var childNodes = $("steemit_item_list").querySelectorAll("div");
				var dataSeq = childNodes.length + 1;
				
				var itemDiv = create("div");
				var itemId = "steemit_item_" + dataSeq;
				itemDiv.setAttribute("id", itemId );
				itemDiv.setAttribute("data-seq", dataSeq );
				itemDiv.setAttribute("class", "depth1" );
				
				var spanChk = create("span");
				spanChk.setAttribute("style", "display:inline-block;width:1.5em;");
				spanChk.innerHTML = "<input id='item_"+dataSeq+"_chk' type='checkbox' "+(chk?"checked":"")+" onclick='updateAlarm();' />";
				itemDiv.appendChild(spanChk);
				
				var spanVal = create("span");
				spanVal.setAttribute("id", "item_" + dataSeq + "_val");
				spanVal.setAttribute("style", "color:SteelBlue;");
				spanVal.setAttribute("onclick", "openPopup( this.parentNode );");
				spanVal.innerText = stmInputText;
				itemDiv.appendChild(spanVal);
				
				var spanDelImg = create("span");
				spanDelImg.setAttribute("style", "float:right;padding-right:2%;");
				spanDelImg.innerHTML = "<img src='/img/btn/minus.png' width='16px' width='16px' onclick='delSteemitItem(\""+itemId+"\")' />";
				itemDiv.appendChild(spanDelImg);
				
				if( stmInputText.indexOf("@") == -1 ){
					
					var spanSel = create("span");
					spanSel.setAttribute("style", "float:right;padding-right:3%;");
					spanSel.appendChild( makeSteemitSel("item_" + dataSeq + "_sel" ) );
					itemDiv.appendChild(spanSel);	
				}
				
				//alert( $("steemit_item_list").childNodes.length );
				itemDiv.appendChild(create("hr"));
				$("steemit_item_list").appendChild(itemDiv);
				
				console.log("scrollHeight : " + scrollHeight + ", new : " +document.body.scrollHeight );
				
				if( scrollHeight < document.body.scrollHeight ) {
					// alert("rere");
					parent.autoResize( parent.document.getElementById("alarm_iframe") );
				}
    	    }
    	    
		
    	    function create(tagName){
    	    	return document.createElement(tagName);
    	    }
    	    
    	    function makeSteemitSel(id){
    	    	
    	    	var sel = create("select");
    	    	sel.setAttribute("id", id);
    	    	sel.setAttribute("disabled", "");
    	    	
    	    	
    	    	sel.innerHTML = "<option value='created'>New</option>";
    	    	sel.innerHTML += "<option value='hot'>Hot</option>";
    	    	sel.innerHTML += "<option value='trending'>Trending</option>";
    	    	sel.innerHTML += "<option value='promoted'>Promoted</option>";
    	    	
    	    	return sel;
    	    }
    	    
    	    function getAlarmInfo(){
    	    	var alarmJson = new Object();
    	    	var steemit = new Object();
    	    	alarmJson.steemit = steemit;
    	    	//console.log( "chk : "+ document.getElementById("steemitAlarmFlag").checked );
    	    	steemit.alarmFlag = document.getElementById("steemitAlarmFlag").checked;
    	    	var steemit_item_list = document.getElementById("steemit_item_list").querySelectorAll("div");
    	    	var json_item_list = new Array();
    	    	
    	    	for( var i = 0; i < steemit_item_list.length;i++){
    	    		var json_item = new Object(); 
    	    		var dataSeq = steemit_item_list[i].getAttribute("data-seq");
    	    		json_item.dataSeq = dataSeq;
    	    		json_item.alamFlag = $("item_"+dataSeq+"_chk").checked;
    	    		
    	    		var itemValue = $("item_"+dataSeq+"_val").innerText;
    	    		if( itemValue.indexOf("@") > -1){
    	    			json_item.dvnm = "user"; 
    	    		}else{
    	    			json_item.dvnm = "tag"; 
    	    			
    	    			json_item.itemSel = $("item_"+dataSeq+"_sel").value;
    	    		}
    	    		json_item.itemValue = itemValue;
    	    		
    	    		json_item_list.push(json_item);
    	    	}
    	    	steemit.item_list = json_item_list;
    	    	return alarmJson;
    	    }
    	    
    	    function updateAlarm(){
				try{
					var alarmJson = getAlarmInfo();
					
					var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
	    	    	alarmJson.alarmID = alarmID;
	    	    	
					parent.$.cookie("kr.co.cobot.alarm.alarmSetting", JSON.stringify(alarmJson), {expires: 3650});
					
	    	    	console.log( "alarmJson : "+ JSON.stringify(alarmJson) );
	    	    	var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
					if( alarmID != "" ){
						parent.reqJson("/Alarm/Update", alarmJson , alarmUpdateSuc );  
					}
				}catch(e){
					alert(e);
				}
    		}
    	    
    	</script>
    	<div id="steemit_item_list">    	
	    	<!-- div id="steemit_item_1" data-seq="1" class="depth1" style="">
	    		<span>
	    			<input id="item_1_chk" type="checkbox" />
	    		</span>
	    		<span id="item_1_val" style="color:SteelBlue;" onclick="openPopup( this.parentNode );"  >
	    			kr-dev
	    		</span>
	    		
	    		<span style="float:right;padding-right:2%;" >
	    			<img src="/img/btn/minus.png" width="16px" width="16px" />
	    		</span>
	    		<span style="float:right;padding-right:3%;" >
	    			<select id="item_1_sel">
	    				<option value="created">New</option>
	    				<option value="hot">Hot</option>
	    				<option value="trending">Trending</option>
	    				<option value="promoted">Promoted</option>
	    			</select>
	    		</span>
	    		<hr />
	    	</div>
	    	<div id="steemit_item_2" data-seq="2" style="padding-left:7%;">
	    		<span>
	    			<input type="checkbox" id="chk_2" />
	    		</span>
	    		<span id="item_2_val" style="color:SteelBlue;" onclick="openPopup( this.parentNode );" >
	    			@nhj12311
	    		</span>
	    		<span style="float:right;padding-right:2%;" >
	    			<img src="/img/btn/minus.png" width="16px" width="16px" />
	    		</span>
	    		<hr />
	    	</div-->
    	</div>
    	<!-- steemit_item_list end -->
    	<br />  
    	<!-- div style="text-align:right;align:right">
	      <button disabled class="js-save-btn mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
	        	저 장
	      </button>
	    </div-->
    	<div style="display:none;">
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="created" checked> New
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="hot" > Hot
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="trending" > Trending
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="promoted" > Promoted
    	</div>
    </p>
    <!-- section class="subscription-details js-subscription-details is-invisible">
      <pre><code class="js-subscription-json"></code></pre>
    </section-->
  </main>

  <script src="/js/alarm/push_main.js"></script>
</body>
</html>
