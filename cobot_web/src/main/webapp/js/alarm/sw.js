/**
 * 
 */
try{
	

console.log('Started', self);
self.addEventListener('install', function(event) {
  self.skipWaiting();
  console.log('Installed', event);
});
self.addEventListener('activate', function(event) {
  console.log('Activated', event);
});
self.addEventListener('push', function(event) {
  console.log('Push message received', event);
  // TODO
});
self.addEventListener('push', function(event) {
	  console.log('[Service Worker] Push Received.');
	  console.log(`[Service Worker] Push had this data: "${event.data}"`);
	  console.log(`[Service Worker] Push had this data: "${event.data.text()}"`);
	  
	  var data = event.data.json();
	  const title = data.title ? data.title : 'Cobot';
	  const options = {
	    body: data.body,
	    icon: data.img ? data.img : '/img/alarm/cobot_192_192.png',
	    badge: data.img ? data.img : '/img/alarm/cobot_192_192.png',
	    data : {url:data.url}
	  };

	  event.waitUntil(self.registration.showNotification(title, options));
	});
self.addEventListener('notificationclick', function(event) {
	  console.log('[Service Worker] Notification click Received.');
	  
	  var url = event.notification.data.url;
	  event.notification.close();
	  
	  console.log(event);
	  
	  event.waitUntil(
	    clients.openWindow(url)
	  );
	});
} catch(e){
	alert("SW.js 등록중 에러 : "+ e);
}