package me.xujichang.hybirdbase.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.xujichang.utils.tool.StringTool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.xujichang.hybirdbase.base.HybirdConst;
import me.xujichang.hybirdbase.interfaces.JSBridgeBaseListener;

/**
 * Activity 跳转路由
 * Created by xjc on 2017/5/23.
 */

public class XRouter {
    private static XRouter ourInstance = null;
    private WeakReference<Context> context;
    private ArrayList<Activity> activities;
    private String basePackageName;
    private String htmlDir;

    private XRouter() {
        activities = new ArrayList<>();
    }

    private XRouter(Builder builder) {
        if (ourInstance == null) {
            ourInstance = new XRouter();
        }
        init(builder.context);
        ourInstance.setBasePackageName(builder.packageName);
        ourInstance.setHtmlDir(builder.htmlDir);
    }

    private void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    private void setHtmlDir(String htmlDir) {
        this.htmlDir = htmlDir;
    }

    private void init(Context applicationContext) {
        this.context = new WeakReference<>(applicationContext);
    }

    public void putActivity(Activity activity) {
        activities.add(activity);
    }

    public void popActivity(Activity activity) {
        activities.remove(activity);
    }

    public static XRouter getInstance() {
        if (null == ourInstance) {
            ourInstance = new XRouter();
        }
        return ourInstance;
    }

    /**
     * 检测协议是否符合要求
     */
    private Uri checkProtocol(String url) {
        Uri uri = Uri.parse(url);
        if (TextUtils.isEmpty(uri.getScheme()) || TextUtils.isEmpty(uri.getHost())) {
            throw new IllegalArgumentException("协议的格式必须是 scheme://host");
        }
        return uri;
    }

    public Intent getIntent(Context context, String url, Class webClass) throws Exception {
        return getIntent(context, Uri.parse(url), webClass);
    }

    /**
     * 获取Intent
     *
     * @param context  上下文
     * @param uri      url
     * @param webClass 处理Url的默认Activity
     * @return
     * @throws Exception 抛出异常
     */
    private Intent getIntent(Context context, Uri uri, Class webClass) throws Exception {
        String scheme = uri.getScheme();
        Intent intent = new Intent();
        if (TextUtils.isEmpty(scheme)) {
            intent.setClass(context, webClass);
            intent.putExtra(HybirdConst.FLAG.WEB_URL, StringTool.parseUrl(htmlDir, uri.toString()));
        } else if (scheme.equals(HybirdConst.SCHEME.NATIVE_SCHEME)) {
            String activityName = uri.getHost();
            String className = getClassName(activityName);
            //根据name获取Class
            intent.setClassName(context, className);
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfoList.size() <= 0) {
                throw new IllegalStateException("activity(" + className + ") 不存在");
            }
            createData(intent, uri.getQuery());
        } else if (scheme.startsWith(HybirdConst.SCHEME.HTTP_SCHEME)) {
            intent.setClass(context, webClass);
            intent.putExtra(HybirdConst.FLAG.WEB_URL, uri.toString());
        }
        return intent;
    }

    /**
     * 对Query解析 作为intent的参数
     *
     * @param intent
     * @param query
     */
    private void createData(Intent intent, String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        Map<String, String> map = StringTool.parseQuery2Map(query);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
    }

    private String getClassName(String activityName) {
        if (!activityName.startsWith(".")) {
            return activityName;
        } else {
            return basePackageName + activityName;
        }
    }

    public void popAllActivity() {
        if (null != activities) {
            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    /**
     * 解析网页发送的数据
     *
     * @param url              数据
     * @param baseListener     Native的事件
     * @param callBackFunction Native回复请求
     */
    public void parseURL(String url, JSBridgeBaseListener baseListener, CallBackFunction callBackFunction) {
        checkProtocol(url);
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();

        if (scheme.equals(HybirdConst.SCHEME.NATIVE_SCHEME)) {// "native://" 单纯主动调起本地逻辑
            String fragment = uri.getFragment();
            parseFragment(fragment, uri, baseListener);
            return;
        }
        if (scheme.equals(HybirdConst.SCHEME.CALL_BACK)) {//"callback://" 需要回传数据
            String fragment = uri.getFragment();
            parseFragment(fragment, uri, baseListener, callBackFunction);
        }
    }

    /**
     * @param fragment
     * @param uri
     * @param baseListener
     * @param callBackFunction
     */
    private void parseFragment(String fragment, Uri uri, JSBridgeBaseListener baseListener, CallBackFunction callBackFunction) {
        if (TextUtils.isEmpty(fragment)) {
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.location)) {//"#location"
            callBackFunction.onCallBack(baseListener.getLocation());
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.device)) {//"#device"
            callBackFunction.onCallBack(baseListener.getDeviceInfo());
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.self)) {//"#self"
            callBackFunction.onCallBack(baseListener.getUserInfo());
        }

    }

    /**
     * 解析 Uri 中的Fragment #
     *
     * @param fragment
     * @param uri
     */
    private void parseFragment(String fragment, Uri uri, JSBridgeBaseListener listener) {
        if (TextUtils.isEmpty(fragment)) {
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.toast)) {//"#toast"
            listener.showToast(uri.getQueryParameter(HybirdConst.Fragment.toast));
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.loading)) {//"#loading"
            if (uri.getQueryParameter("operation").equals(HybirdConst.Operation.START)) {
                listener.startLoading(uri.getQueryParameter(HybirdConst.Fragment.toast));
            } else {
                listener.stopLoading();
            }
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.dialog)) {//"#dialog"
            listener.showDialog(uri.getQueryParameter(HybirdConst.Fragment.dialog));
            return;
        }
        if (fragment.equals(HybirdConst.Fragment.activity)) {//"#activity"
            if (uri.getQueryParameter("operation").equals(HybirdConst.Operation.START)) {
                listener.startAnotherActivity(uri.getQueryParameter(HybirdConst.Fragment.activity));
            } else {
                listener.finishActivity();
            }
        }
    }

    public static class Builder {
        private Context context;
        private String htmlDir;
        private String packageName;

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder htmlDir(String dir) {
            this.htmlDir = dir;
            return this;
        }

        public Builder basePackage(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public void build() {
            new XRouter(this);
        }
    }
}
