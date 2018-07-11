package me.xujichang.hybirdbase.module.web.handler;

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
    private IWebJsCallBack mIWebJsCallBack;

    public void addJsCallBack(IWebJsCallBack IWebJsCallBack) {
        mIWebJsCallBack = IWebJsCallBack;
    }

    public WebHandler(BridgeWebView webView) {
        this(webView, null);
    }

    public WebHandler(BridgeWebView webView, IWebJsCallBack callBack) {
        mWebView = webView;
        mIWebJsCallBack = callBack;
        patchWebHandler();
    }

    public void registerNativeHandler(final String type, final IWebJsCallBack callBack) {
        if (null == mIWebJsCallBack) {
            mIWebJsCallBack = callBack;
        }
        if (TextUtils.isEmpty(type)) {
            registerDefaultNativeHandler();
            return;
        }
        mWebView.registerHandler(type, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                dispatchHandler(type, data, function);
            }
        });
    }

    private void dispatchHandler(String type, String data, CallBackFunction function) {
        if (null == mIWebJsCallBack) {
            throw new RuntimeException("There is a null callback");
        }
        mIWebJsCallBack.onJsCallBack(type, data, function);
    }

    protected void registerDefaultNativeHandler() {
        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                dispatchHandler(CALLBACK_DEFAULT, data, function);
            }
        });
    }

    public void registerNativeHandler(final String type) {
        if (TextUtils.isEmpty(type)) {
            registerDefaultNativeHandler();
            return;
        }
        mWebView.registerHandler(type, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                dispatchHandler(type, data, function);
            }
        });
    }

    protected abstract void patchWebHandler();
}
