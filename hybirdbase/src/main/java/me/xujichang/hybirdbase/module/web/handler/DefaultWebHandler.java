package me.xujichang.hybirdbase.module.web.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 12:39.
 */

public class DefaultWebHandler extends WebHandler {
    public DefaultWebHandler(BridgeWebView webView) {
        super(webView);
    }

    public DefaultWebHandler(BridgeWebView webView, IWebJsCallBack callBack) {
        super(webView, callBack);
    }

    @Override
    protected void patchWebHandler() {
        registerNativeHandler(null);
    }
}
