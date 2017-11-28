function invokeNative(name,data,responseCallback) {
    window.WebViewJavascriptBridge.callHandler("handlerName", data,
    function(responseData) {

    });
};
function show(msg) {
    document.getElementById("show").innerHTML = msg;
};
//请求位置信息
function requestLocation(){
invokeNative("location",
"",
function(responseData) {
       show(responseData)
       })
}
//获取图片
function requestPicture(){
invokeNative("picture",
"",
function(responseData) {
       show(responseData)
       })
}
//录音
function requestLocation(opt){
invokeNative("location",
{
option：opt
},
function(responseData) {
       show(responseData)
       }))
}
//===================必须的==========================
function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener('WebViewJavascriptBridgeReady',
        function() {
            callback(WebViewJavascriptBridge)
        },
        false);
    }
};
// 第一连接时初始化bridge
connectWebViewJavascriptBridge(function(bridge) {
          bridge.init(function(message, responseCallback) {
            console.log('JS got a message' + message, "message 呢");
            var data = "Javascript Responds 测试中文!";
            console.log('JS responding with '+data, '什么鬼');
            responseCallback(data);
        });
});
//==========================================
