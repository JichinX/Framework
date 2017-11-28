package me.xujichang.hybirdbase.module.web.handler;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 16:58.
 */

public abstract class WebHandler {
    public static final String CALLBACK_DEFAULT = "default";
    private BridgeWebView mWebView;

    public WebHandler(BridgeWebView webView) {
        mWebView = webView;
    }

    public void registerNativeHandler(final String type, final IWebJsCallBack callBack) {
        if (TextUtils.isEmpty(type)) {
            mWebView.setDefaultHandler(new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    callBack.onJsCallBack(CALLBACK_DEFAULT, data, function);
                }
            });
            return;
        }
        mWebView.registerHandler(type, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBack.onJsCallBack(type, data, function);
            }
        });
    }

    protected abstract void addJsCallBack(IWebJsCallBack callBack);
}
