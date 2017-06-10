chrome.browserAction.onClicked.addListener(function() {
    var w = 440;
    var h = 220;
    var left = (screen.width/2)-(w/2);
    var top = (screen.height/2)-(h/2); 

    chrome.windows.create({'url': 'https://cobot.co.kr', 'type': 'normal', 'width': w, 'height': h, 'left': left, 'top': top} , function(window) {
    });
});
