package me.xujichang.hybirdbase.module.web.client;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

import me.xujichang.hybirdbase.module.web.interfaces.IWebBase;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 10:32.
 */

public class SelfWebViewClient extends BridgeWebViewClient {
    private IWebBase mWebBase;

    public SelfWebViewClient(BridgeWebView webView, IWebBase base) {
        super(webView);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                mWebBase.onDownLoadStart(url, userAgent, contentDisposition, mimetype, contentLength);
            }
        });
        mWebBase = base;
    }

    /**
     * 必须调用 super,因为父类有额外操作{@link super#shouldOverrideUrlLoading(WebView, String)}
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogTool.d("url:" + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mWebBase.onPageStarted(view, url, favicon);
    }

    /**
     * 必须调用 super,因为父类有额外操作{@link super#onPageFinished(WebView, String)}
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mWebBase.onPageFinished(view, url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        //加载出错 errorCode 错误码 description 描述 failingUrl 加载错误的Url
        mWebBase.onError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //加载SSL出错 是否继续加载
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        //拦截 按键事件
        return mWebBase.onOverrideKeyEvent(view, event);
    }
}
