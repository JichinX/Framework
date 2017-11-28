package me.xujichang.hybirdbase.module.web.interfaces;

import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 16:51.
 */

public interface IWebParseData {
    /**
     * 对默认数据 进行解析
     *
     * @param data
     * @param function
     */
    void parseData(String data, CallBackFunction function);
}
