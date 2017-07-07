function pickFile() {
    var name = document.getElementById("name").value;
    var pwd = document.getElementById("password").value;
    var data = "name = " + name + ",pwd = " + pwd;

    window.WebViewJavascriptBridge.callHandler("pickFile", {
        "param": data
    },
    function(responseData) {

});
};
//显示源码
function showSrc() {
    document.getElementById("show").innerHTML = document.getElementsByTagName("html")[0].innerHTML;
};
//发送消息
function sendMessage() {
    var message = {
        id: 1,
        content: "这是一张图片 <img src =\" a.png\"/> test \r\n hahaha"
    }
    window.WebViewJavascriptBridge.send(message,
    function(responseData) {
        document.getElementById("show").innerHTML = "response from Native: " + responseData;
    });
};
//
function invokeNative() {
    var name = document.getElementById("name").value;
    var pwd = document.getElementById("password").value;
    var data = "name = " + name + ",pwd = " + pwd;

    window.WebViewJavascriptBridge.callHandler("submitFromWeb", {
        "param": data
    },
    function(responseData) {
        document.getElementById("show").innerHTML = "response from Native: " + responseData;
    });
};
function bridgeLog(logContent) {
    document.getElementById("show").innerHTML = logContent;
};
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

    // 注册一个"functionInJs",
//    bridge.registerHandler("functionInJs",
//        function(data, responseCallback) {
//            document.getElementById("show").innerHTML = ("data from Java: = " + data);
//            var responseData = "Javascript Says 我要你的地址!";
//            // response层
//            responseCallback(responseData);
//        });
          bridge.init(function(message, responseCallback) {
            console.log('JS got a message' + message, "message 呢");
            var data = "Javascript Responds 测试中文!";
            console.log('JS responding with '+data, '什么鬼');
            responseCallback(data);
        });
});
//==========================================
//显示Toast
function showToast(){
  var message = {
      scheme: "uniview",
      host:"toast",
      msg: "toast 测试"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
//显示loading
function showLoading() {
  var message = {
      scheme: "uniview",
      host:"loading",
      operation:"start",
      msg: "loading 测试"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
//停止Loading
function stopLoading() {
  var message = {
      scheme: "uniview",
      host:"loading",
      operation:"stop",
      msg: "loading 测试"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
//显示Dialog
function showDialog() {
  var message = {
      scheme: "uniview",
      host:"dialog",
      content: "dialog 测试"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
//跳转
function toAnotherActivity() {
  var message = {
      scheme: "uniview",
      host:"activity",
      operation: "start",
      className:"PlatformActivity"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
//结束当前页面
function finish() {
  var message = {
      scheme: "uniview",
      host:"activity",
      operation: "stop"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}
function getUserInfo() {
  var message = {
      scheme: "uniview",
      host:"userInfo"
  }
window.WebViewJavascriptBridge.send(message,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
}function getDeviceInfo() {
   var message = {
       scheme: "uniview",
       host:"deviceInfo"
   }
 window.WebViewJavascriptBridge.send(message,
   function(responseData) {
       document.getElementById("show").innerHTML = "response from Native: " + responseData;
   });
 }function getLocation() {
    var message = {
        scheme: "uniview",
        host:"location"
    }
  window.WebViewJavascriptBridge.send(message,
    function(responseData) {
        document.getElementById("show").innerHTML = "response from Native: " + responseData;
    });
  }
  function onAlert(){
  alert("this is a alert");
  }
  function onConform(){
  confirm("this is conform")
  }
  function download(){
  var url = "http://183.134.9.47/apk.r1.market.hiapk.com/data/upload/marketClient/HiMarket7.8.1.81_1492485212332.apk";
 window.WebViewJavascriptBridge.callHandler("download", url
 ,
    function(responseData) {
        document.getElementById("show").innerHTML = "response from Native: " + responseData;
    });
    }
    function sendUrl(){
      var url = "http://dldir1.qq.com/qqfile/QQforMac/QQ_V5.5.1.dmg";
window.WebViewJavascriptBridge.send(url,
  function(responseData) {
      document.getElementById("show").innerHTML = "response from Native: " + responseData;
  });
    }
    function login(){
     var message = {
          scheme: "uniview",
          host:"activity",
          operation: "start",
          className:"view.LoginActivity"
      }
    window.WebViewJavascriptBridge.send(message,
      function(responseData) {
          document.getElementById("show").innerHTML = "response from Native: " + responseData;
      });
    }
