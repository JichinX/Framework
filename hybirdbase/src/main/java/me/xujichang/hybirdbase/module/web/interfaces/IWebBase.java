package me.xujichang.hybirdbase.module.web.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 10:52.
 */

public interface IWebBase {
    /**
     * WebView加载出错
     *
     * @param view        WebView
     * @param errorCode   错误码
     * @param description 错误描述
     * @param failingUrl  加载失败的Url
     */
    void onError(WebView view, int errorCode, String description, String failingUrl);

    /**
     * 页面加载结束
     *
     * @param view WebView
     * @param url  url
     */
    void onPageFinished(WebView view, String url);

    /**
     * 页面开始加载
     *
     * @param view    WebView
     * @param url     url
     * @param favicon 网站Icon
     */
    void onPageStarted(WebView view, String url, Bitmap favicon);

    /**
     * 覆盖按键事件
     *
     * @param view  WebView
     * @param event event
     * @return true 该事件被重写， false 未覆盖
     */
    boolean onOverrideKeyEvent(WebView view, KeyEvent event);

    /**
     * 加载进度
     *
     * @param view        WebView
     * @param newProgress 进度
     */
    void onPageProgress(WebView view, int newProgress);

    /**
     * 获取到Title
     *
     * @param view  WebView
     * @param title 标题
     */
    void onPageReceiveTitle(WebView view, String title);

    /**
     * 选择文件
     *
     * @param webView           WebView
     * @param filePathCallback
     * @param fileChooserParams
     * @return
     */
    boolean onPageFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);

    /**
     * 输入框
     *
     * @param view         WebView
     * @param url          Url
     * @param message      消息
     * @param defaultValue 默认值
     * @param result       结果
     * @return
     */
    boolean onPagePrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result);

    /**
     * console 消息
     *
     * @param consoleMessage
     * @return
     */
    boolean onConsoleMessage(ConsoleMessage consoleMessage);

    /**
     * 提示框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    boolean onJsAlert(WebView view, String url, String message, JsResult result);

    /**
     * 确认框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    boolean onJsConfirm(WebView view, String url, String message, JsResult result);

    /**
     * 下载
     *
     * @param url
     * @param userAgent
     * @param contentDisposition
     * @param mimetype
     * @param contentLength
     */
    void onDownLoadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength);
}
