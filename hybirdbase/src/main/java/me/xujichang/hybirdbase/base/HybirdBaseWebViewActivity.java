package me.xujichang.hybirdbase.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.xujichang.utils.tool.LogTool;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.interfaces.JSBridgeBaseListener;
import me.xujichang.hybirdbase.utils.JSBridgeManager;
import me.xujichang.util.activity.SuperActivity;

/**
 * WebView 的基本Activity
 * 1，
 * Created by xjc on 2017/6/1.
 */

public abstract class HybirdBaseWebViewActivity extends SuperActivity implements JSBridgeBaseListener {
    /**
     * WebView对象
     */
    private BridgeWebView webView;
    /**
     * 加载的URl
     */
    private String url;

    private MaterialDialog progressDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        webView = (BridgeWebView) findViewById(R.id.base_web_view);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_status);
        //加载默认方法
        JSBridgeManager.getInstance().addDefaultHandler(webView, this);
        //加载Client
        webView.setWebChromeClient(new SelfWebViewChromeClient());
        webView.setWebViewClient(new SelfWebViewClient(webView));
        //下载
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //下载
                if (!onDownLoad(url, userAgent, contentDisposition, mimetype, contentLength)) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                LogTool.d("Download:" + url);
            }
        });
        initWebViewActionBar();
        initHandler();
    }


    /**
     * 可以注册 自己的Handler
     */
    protected abstract void initHandler();

    protected void startLoadUrl(String url) {
        this.url = url;
        //加载URL
        webView.loadUrl(url);
    }

    public void reloadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            webView.reload();
        } else {
            webView.clearHistory();
            webView.loadUrl(url);
        }
    }

    private void reload() {
        reloadUrl(null);
    }

    /**
     * 默认的错误处理 加载error html
     *
     * @param errorCode
     * @param msg
     * @param failingUrl
     * @return
     */
    private String getErrorPage(int errorCode, String msg, String failingUrl) {
        return "file:///android_asset/web/error.html?msg=" + msg;
    }

    /**
     * 用于子类 获取WebView对象进行自定义
     *
     * @return
     */
    protected BridgeWebView getWebView() {
        return webView;
    }

    /**
     * 注册一个Handler  以及处理类
     *
     * @param handlerName
     * @param handler
     */
    protected void registerHandler(String handlerName, BridgeHandler handler) {
        if (TextUtils.isEmpty(handlerName)) {
            registerDefaultHandler(handler);
            return;
        }
        webView.registerHandler(handlerName, handler);
    }

    protected void callJsHandler(String handlerName, String data, CallBackFunction callback) {
        webView.callHandler(handlerName, data, callback);
    }

    protected void sendMessage(String msg, CallBackFunction callback) {
        webView.send(msg, callback);
    }

    protected void sendMessage(String msg) {
        webView.send(msg, null);
    }

    /**
     * 注册一个默认的Handler 但会覆盖掉本类给予的默认handler
     *
     * @param handler
     */
    protected void registerDefaultHandler(BridgeHandler handler) {
        webView.setDefaultHandler(handler);
    }

    private class SelfWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //加载进度
            onPageProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //获取Title
            onPageReceiveTitle(title);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //全屏
        }

        @Override
        public void onHideCustomView() {
            //取消全屏
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            //选择文件
            if (onPageFileChooser(filePathCallback, fileChooserParams)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            //prompt事件
            if (onPagePrompt(url, message, defaultValue, result)) {
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            //打印Console消息
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //alert
            if (onPageAlert(url, message, result)) {
                return true;
            }
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            if (onPageConfirm(url, message, result)) {
                return true;
            }
            //confirm事件 如果拦截 必须调用 result.confirm
            return super.onJsConfirm(view, url, message, result);
        }
    }

    private class SelfWebViewClient extends BridgeWebViewClient {
        public SelfWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //开始加载网页
            onPageStart(url, favicon);
            LogTool.d("page start:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //网页加载完成
            super.onPageFinished(view, url);
            onPageFinish(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //拦截超链接
            LogTool.d("overrideUul Loading:" + url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //加载出错 errorCode 错误码 description 描述 failingUrl 加载错误的Url
            LogTool.d("receive error:" + failingUrl);
            if (onPageError(view, errorCode, description, failingUrl)) {
                return;
            }
            view.loadUrl(getErrorPage(errorCode, description, failingUrl));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //加载SSL出错 是否继续加载
//            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            //拦截 按键事件
            //返回
            if (onOverrideKeyEvent(view, event)) {
                return true;
            }
            LogTool.d("keyevent:" + event.getCharacters());
            return super.shouldOverrideKeyEvent(view, event);
        }
    }


    /**
     * 子类可覆盖此方法 实现自己的错误处理逻辑
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     * @return
     */
    protected boolean onPageError(WebView view, int errorCode, String description, String failingUrl) {
        return false;
    }

    //=======================================Dialog=================================================
    private void stopProgressDialog() {
        progressBar.setProgress(0);
        progressBar.setVisibility(View.GONE);

//        if (null != progressDialog) {
//            progressDialog.dismiss();
//        }
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
//        if (null == progressDialog) {
//            createProgressDialog();
//        }
//        progressDialog.show();
    }

    private void setDialogProgress(int newProgress) {
        progressBar.setProgress(newProgress);
//        if (null != progressDialog) {
//            progressDialog.setProgress(newProgress);
//        }
    }

    private void createProgressDialog() {
//        progressDialog = new MaterialDialog.Builder(this)
//                .content("网页数据加载中...")
//                .progress(true, 100)
//                .build();
    }
    //=======================================ActionBar=================================================

    private void setHyBirdBaseWebActionBarTitle(String title) {
        setActionBarTitle(title);
    }

    private void initWebViewActionBar() {
        showBackArrow();
        setRightImg(R.drawable.ic_refresh);
    }

    @Override
    protected void onRightAreaClick() {
        reloadUrl(url);
    }

    //JsBridge 实现的方法
    @Override
    public void finishActivity() {
        super.finishActivity();
    }

    @Override
    public void startAnotherActivity(String activityName) {
        if (null == activityName) {
            return;
        }
        super.startAnotherActivity(activityName);
    }

    @Override
    public void showToast(String queryParameter) {
        super.showToast(queryParameter);
    }

    @Override
    public void startLoading(String queryParameter) {
        super.startLoading(queryParameter);
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
    }

    @Override
    public void showDialog(String queryParameter) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content(queryParameter)
                .positiveText("确定")
                .cancelable(false)
                .build()
                .show();
    }

    protected void onPageProgress(int newProgress) {
        setDialogProgress(newProgress);
    }

    protected void onPageReceiveTitle(String title) {
        setHyBirdBaseWebActionBarTitle(title);
    }

    protected boolean onPageFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    protected boolean onPagePrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    protected boolean onPageConfirm(String url, String message, JsResult result) {
        return false;
    }

    protected boolean onPageAlert(String url, String message, JsResult result) {
        return false;
    }

    protected boolean onOverrideKeyEvent(WebView view, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_BACK;
    }

    protected void onPageFinish(String url) {
        stopProgressDialog();
    }

    protected void onPageStart(String url, Bitmap favicon) {
        showProgressDialog();
    }

    private boolean onDownLoad(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}


