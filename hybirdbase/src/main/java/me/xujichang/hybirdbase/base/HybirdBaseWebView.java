package me.xujichang.hybirdbase.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.module.web.WebDataParse;
import me.xujichang.hybirdbase.module.web.WebSettingConst;
import me.xujichang.hybirdbase.module.web.client.SelfWebChromeClient;
import me.xujichang.hybirdbase.module.web.client.SelfWebViewClient;
import me.xujichang.hybirdbase.module.web.handler.DefaultWebHandler;
import me.xujichang.hybirdbase.module.web.interfaces.IWebBase;
import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;
import me.xujichang.hybirdbase.module.web.interfaces.IWebLoading;
import me.xujichang.hybirdbase.module.web.interfaces.IWebParseData;
import me.xujichang.hybirdbase.module.web.loading.ProgressLoading;
import me.xujichang.util.activity.SuperActivity;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 10:21.
 */

public abstract class HybirdBaseWebView extends SuperActivity implements IWebBase, IWebJsCallBack {
    /**
     * 要加载的Url
     */
    private String url;
    /**
     * loading
     */
    private IWebLoading mLoading;
    /**
     * WebView对象
     */
    private BridgeWebView mWebView;
    /**
     * Progress
     */
    private ProgressBar mProgressBar;
    /**
     * ChromeClient
     */
    private SelfWebChromeClient mChromeClient;
    /**
     * WebViewClient
     */
    private SelfWebViewClient mWebClient;
    /**
     * 对默认操作的数据 进行解析
     */
    private IWebParseData mParseData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        initView();
        initWebView();
    }

    public void setParseData(IWebParseData parseData) {
        mParseData = parseData;
    }

    /**
     * 初始化 WebView
     */
    private void initWebView() {
        mChromeClient = new SelfWebChromeClient(this);
        mWebClient = new SelfWebViewClient(mWebView, this);
        //加载Client
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebClient);
    }

    public void setWebClient(SelfWebViewClient webClient) {
        mWebClient = webClient;
    }

    public void setWebClient(SelfWebChromeClient webClient) {
        mChromeClient = webClient;
    }

    public void callJsHandler(String name, String data, CallBackFunction callBackFunction) {
        mWebView.callHandler(name, data, callBackFunction);
    }

    /**
     * 初始化 View
     */
    private void initView() {
        initActionBar();
        mWebView = findViewById(R.id.base_web_view);
        mProgressBar = findViewById(R.id.pb_loading_status);
        mLoading = new ProgressLoading(mProgressBar);
        mParseData = new WebDataParse();
        initWebHandler();
        initWebSetting(mWebView.getSettings());
    }

    protected void initWebSetting(WebSettings settings) {
    }

    private void initWebHandler() {
        new DefaultWebHandler(mWebView, this);
        initExtHandler(this);
    }

    protected abstract void initExtHandler(IWebJsCallBack callBack);

    /**
     * 加载Url
     *
     * @param url
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 初始化 ActionBar
     */
    protected void initActionBar() {
        showBackArrow();
        setActionBarTitle(url);
        setRightImg(R.drawable.ic_refresh);
    }

    @Override
    protected void onRightAreaClick() {
        //刷新当前页面
        reloadUrl(null);
    }

    /**
     * 重新加载刚加载的页面
     */
    protected void reload() {
        reloadUrl(url);
    }

    /**
     * 设置Loading的方式
     *
     * @param loading
     */
    public void setLoading(IWebLoading loading) {
        mLoading = loading;
    }

    /**
     * 右键刷新
     * 默认刷新刚进入时的Url
     *
     * @param url
     */
    private void reloadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mWebView.reload();
        } else {
            mWebView.clearHistory();
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mLoading.stop();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mLoading.start();
    }

    /**
     * 是否拦截返回键事件
     *
     * @param view  WebView
     * @param event event
     * @return
     */
    @Override
    public boolean onOverrideKeyEvent(WebView view, KeyEvent event) {
        //默认拦截返回事件
        LogTool.d("onOverrideKeyEvent:" + event.getKeyCode());
        return true;
    }

    @Override
    public void onPageProgress(WebView view, int newProgress) {
        mLoading.progress(newProgress);
    }

    @Override
    public void onPageReceiveTitle(WebView view, String title) {
        setActionBarTitle(title);
    }

    @Override
    public boolean onPageFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    public boolean onPagePrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return false;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public void onDownLoadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        //默认调用系统下载
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onJsCallBack(String type, String data, CallBackFunction function) {
        switch (type) {
            case DefaultWebHandler.CALLBACK_DEFAULT:
                mParseData.parseData(data, function);
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onOverrideKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onOverrideKeyEvent(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

    public IWebLoading getLoading() {
        return mLoading;
    }

    public BridgeWebView getWebView() {
        return mWebView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public SelfWebChromeClient getChromeClient() {
        return mChromeClient;
    }

    public SelfWebViewClient getWebClient() {
        return mWebClient;
    }

    public IWebParseData getParseData() {
        return mParseData;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }
}
