##WebViewActivity与Native交互的简单说明
###1.WebView内调用Native方法。
```
function invokeNative() {
    window.WebViewJavascriptBridge.callHandler("submitFromWeb", {
        "param": data
    },
    function(responseData) {
        document.getElementById("show").innerHTML = "response from Native: " + responseData;
    });
};
```
其中**window.WebViewJavascriptBridge**为注入的对象，调用Native则是接下来的方法：
```
//Js
callHandler("handlerName",
    data,
    function(responseData) {
       //do Something in Js
    });

//Java
mWebView.registerHandler(type, new BridgeHandler() {
             @Override
             public void handler(String data, CallBackFunction function) {
                 callBack.onJsCallBack(type, data, function);
             }
         });
```
有三个参数：  
1. handlerName     
    此字段表示调用Native代码中，注册方法时使用的标志，比如：以上的代码片段，其中的type,当js代码callHandler使用的handlerName与type匹配时，便会执行以上代码对应的BridgeHandler；   
2. data  
作为调用方法时的参数，在传递时是Json格式。以上代码中，当handlerName与type匹配后，执行对应的BridgeHandler,在BridgeHandler的方法：handler(String data, CallBackFunction function)
其中的data便是js中的data,不过是json格式化后的字符串。可以以此来传递执行方法需要的参数。                         
3. function(responseData) { //do Something in Js} 此方法是用来接收Native代码返回的数据，也就是java中的function;其定义为：
```
public interface CallBackFunction {
    public void onCallBack(String data);
}
```
function.onCallBack(data),便是将data传递给js的function(responseData)；即data->responseData;
