<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Push Notification codelab</title>
</head>
<body>
  <h1>Push Notification codelab</h1>
  <p>This page must be accessed using HTTPS or via localhost.</p>
  <script defer type="text/javascript">
  
  if ('serviceWorker' in navigator) {
	  console.log('Service Worker is supported');
	  navigator.serviceWorker.register('/js/sw2.js').then(function(reg) {
	    console.log(':^)', reg);
	    // TODO
	  }).catch(function(err) {
	    console.log(':^(', err);
	  });
	 }
  </script>
</body>
</html>