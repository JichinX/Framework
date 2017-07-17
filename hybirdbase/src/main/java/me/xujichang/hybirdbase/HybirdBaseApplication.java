package me.xujichang.hybirdbase;

import android.app.Application;

import com.xujichang.utils.retrofit.RetrofitManager;

import me.xujichang.hybirdbase.router.XRouter;

/**
 * Created by xjc on 2017/6/29.
 */

public abstract class HybirdBaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //XRoute初始化
        new XRouter.Builder().basePackage(getBasePackageName()).htmlDir(getWebDir()).withContext(getBaseContext()).build();
        //RetrofitManager
        new RetrofitManager.Builder().baseUrl(getAppBaseUrl()).token(getInitToken()).build();
    }

    protected abstract String getInitToken();

    protected abstract String getAppBaseUrl();

    protected abstract String getWebDir();

    protected abstract String getBasePackageName();
}
