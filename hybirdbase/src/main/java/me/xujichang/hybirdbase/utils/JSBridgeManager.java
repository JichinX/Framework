package me.xujichang.hybirdbase.utils;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.xujichang.utils.tool.LogTool;

import me.xujichang.hybirdbase.interfaces.JSBridgeBaseListener;
import me.xujichang.hybirdbase.router.XRouter;

/**
 * 管理基本的方法
 * toast
 * loading
 * dialog
 * deviceInfo
 * selfInfo
 * location
 * <p>
 * Created by xjc on 2017/5/23.
 */

public class JSBridgeManager {
    private static JSBridgeManager manager = null;

    private JSBridgeManager() {

    }

    public static JSBridgeManager getInstance() {
        if (null == manager) {
            manager = new JSBridgeManager();
        }
        return manager;
    }


    public void addDefaultHandler(BridgeWebView webView, final JSBridgeBaseListener baseListener) {
        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                LogTool.d(s);
                XRouter.getInstance().parseURL(s, baseListener, callBackFunction);
            }
        });

    }

}
