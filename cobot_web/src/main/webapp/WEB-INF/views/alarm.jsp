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
  <link rel="stylesheet" href="/css/push_css.css" charset="UTF-8">
  <script>
  
	  function updateAlarm(value){
		  parent.$.cookie("kr.co.cobot.alarm.steemDvcd", value , { expires: 3650 } );
		  var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
		  if( alarmID != "" ){
			  parent.reqJson("/Alarm/Update", {alarmID : alarmID, steemDvcd : value }, alarmUpdateSuc );  
		  }
	  }
	  
	  function alarmUpdateSuc(res){
		  //alert(res);
	  }
	  
	  window.onload=function(){
		  var steemDvcd = parent.$.cookie("kr.co.cobot.alarm.steemDvcd");
		  parent.$("#alarm_iframe").contents().find("input:radio[name=steemDvcd][value="+steemDvcd+"]").attr("checked", true);  
	  }
  </script>
</head>
<body>
  <main>
  	<p>Cobot에서 Steemit-Coinkorea의 새글을 대상으로 시범적으로 알람 서비스의 알파 운영을 시행 하고자 합니다. 해당 서비스는 안드로이드 브라우저나 크롬 브라우저에서 서비스 가능하며  동의하시는 분은 'Cobot 알람 받기'를 터치해주면 됩니다. 또한 언제든지 'Cobot 알람 중지'나 사용중이신 브라우저 상에서의 알람 거부 설정을 통해서 알람을  거부하실수도 있습니다. 앱과 달리 브라우저의 알람 기능을 이용하는 것으로 어떠한 해킹의 위험도 없음을 알려드립니다.</p>
    <p>
      <button disabled class="js-push-btn mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
        	Cobot 알람 받기
      </button>
    </p>
    <p>
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="created" checked> New
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="hot" > Hot
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="trending" > Trending
    	<input type="radio" name="steemDvcd" onclick="updateAlarm(this.value);" value="promoted" > Promoted
    </p>
    <section class="subscription-details js-subscription-details is-invisible">
      <pre><code class="js-subscription-json"></code></pre>
    </section>
  </main>

  <script src="/js/alarm/push_main.js"></script>
</body>
</html>
