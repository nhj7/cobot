chrome.browserAction.onClicked.addListener(function(tab) {
  chrome.windows.create({
      url: chrome.runtime.getURL("popup.html")
	  
    , type: "popup"
	, width : 480
	, height : 650
	
  }, function(win) {
    // win represents the Window object from windows API
    // Do something after opening
  });
});