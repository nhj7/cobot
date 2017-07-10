

/*
 * 
 * Push Notifications codelab Copyright 2015 Google Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License
 * 
 */

/* eslint-env browser, es6 */

'use strict';

const applicationServerPublicKey = 'BLjkJUY4Padux-qoYj_qT5FgPM5YW9p19Y8JyWeN0zeAA2ESySpuHcaDImzEJBlxzkcXMa9FOZsKLP0WZ9O6AK4';

const pushButton = document.querySelector('.js-push-btn');

let isSubscribed = false;
let swRegistration = null;

function urlB64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - base64String.length % 4) % 4);
  const base64 = (base64String + padding)
    .replace(/\-/g, '+')
    .replace(/_/g, '/');

  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}

function initialiseUI() {
	  pushButton.addEventListener('click', function() {
	    pushButton.disabled = true;
	    if (isSubscribed) {
	    	// console.log('TODO: Unsubscribe user');
	    	unsubscribeUser();
	    } else {
	      subscribeUser();
	    }
	  });

	  // Set the initial subscription value
	  swRegistration.pushManager.getSubscription()
	  .then(function(subscription) {
	    isSubscribed = !(subscription === null);

	    updateSubscriptionOnServer(subscription);

	    if (isSubscribed) {
	      console.log('User IS subscribed.');
	    } else {
	      console.log('User is NOT subscribed.');
	    }

	    updateBtn();
	  });
	}

function subscribeUser() {
	  const applicationServerKey = urlB64ToUint8Array(applicationServerPublicKey);
	  swRegistration.pushManager.subscribe({
	    userVisibleOnly: true,
	    applicationServerKey: applicationServerKey
	  })
	  .then(function(subscription) {
	    console.log('User is subscribed:', subscription);

	    updateSubscriptionOnServer(subscription);

	    isSubscribed = true;

	    updateBtn();
	  })
	  .catch(function(err) {
	    console.log('Failed to subscribe the user: ', err);
	    updateBtn();
	  });
	}

function unsubscribeUser() {
	  swRegistration.pushManager.getSubscription()
	  .then(function(subscription) {
	    if (subscription) {
	      return subscription.unsubscribe();
	    }
	  })
	  .catch(function(error) {
	    console.log('Error unsubscribing', error);
	  })
	  .then(function() {
	    updateSubscriptionOnServer(null);

	    console.log('User is unsubscribed.');
	    isSubscribed = false;

	    updateBtn();
	  });
	}

function updateSubscriptionOnServer(subscription) {
	// TODO: Send subscription to application server
	// console.log("updateSubscriptionOnServer.subscription : " + subscription);
		
	  const subscriptionJson = document.querySelector('.js-subscription-json');
	  const subscriptionDetails =
	    document.querySelector('.js-subscription-details');

	  if (subscription) {
		var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
		if( alarmID == undefined || alarmID == "" ){
			
			var steemDvcd = parent.$("#alarm_iframe").contents().find("input:radio[name=steemDvcd]:checked").val();;
			
			//subscription["steemDvcd"] = steemDvcd;
			var data = JSON.stringify(subscription);
			var json = JSON.parse(data);
			json.steemDvcd = steemDvcd;
			parent.reqJson("/Alarm/Reg", json, alarmRegSuc );
		}	
		
	    // subscriptionJson.textContent = JSON.stringify(subscription);
	    // subscriptionDetails.classList.remove('is-invisible');
	  } else {
		  
		  var alarmID = parent.$.cookie("kr.co.cobot.alarmID");
		  if( alarmID != undefined && alarmID != "" ){
				var data = JSON.stringify(subscription);
				parent.reqJson("/Alarm/Del", {alarmID : alarmID}, alarmDelSuc );
			}	
	  }
	}

function alarmRegSuc( res ){
	//alert(res.alarmID);
	parent.$.cookie("kr.co.cobot.alarmID", res.alarmID, { expires: 3650 } );
	
}

function alarmDelSuc(res){
	parent.$.cookie("kr.co.cobot.alarmID", "", { expires: 3650 } );
}

function updateBtn() {
	  if (Notification.permission === 'denied') {
	    pushButton.textContent = 'Cobot 알람 거절';
	    pushButton.disabled = true;
	    updateSubscriptionOnServer(null);
	    return;
	  }

	  if (isSubscribed) {
	    pushButton.textContent = 'Cobot 알람 중지';
	  } else {
	    pushButton.textContent = 'Cobot 알람 받기';
	  }

	  pushButton.disabled = false;
	}


if ('serviceWorker' in navigator && 'PushManager' in window) {
	  console.log('Service Worker and Push is supported');

	  navigator.serviceWorker.register('/js/alarm/sw.js')
	  .then(function(swReg) {
	    console.log('Service Worker is registered', swReg);

	    swRegistration = swReg;
	    initialiseUI();
	  })
	  .catch(function(error) {
	    console.error('Service Worker Error', error);
	  });
	} else {
	  console.warn('Push messaging is not supported');
	  pushButton.textContent = 'Push Not Supported';
	}

