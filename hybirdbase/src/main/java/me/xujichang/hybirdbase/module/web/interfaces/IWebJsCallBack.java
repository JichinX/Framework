package me.xujichang.hybirdbase.module.web.interfaces;

import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 16:39.
 */

public interface IWebJsCallBack {
    void onJsCallBack(String type, String data, CallBackFunction function);
}
