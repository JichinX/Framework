package me.xujichang.hybirdbase.module.web.handler;

import android.support.annotation.NonNull;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

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

    @Override
    public void addJsCallBack(@NonNull final IWebJsCallBack callBack) {
        //设置默认
        registerNativeHandler(null, callBack);
    }

}
