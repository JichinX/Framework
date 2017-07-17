package me.xujichang.hybirdbase.base;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xujichang.utils.activity.SuperActivity;
import com.xujichang.utils.bean.AppInfo;
import com.xujichang.utils.download.DownLoadTool;

import java.io.File;

import io.reactivex.Observable;
import me.xujichang.hybirdbase.api.DownLoadApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by xjc on 2017/6/23.
 */

public abstract class HybirdBaseActivity extends SuperActivity {

    protected void showAlertDialog(@NonNull String msg, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content(msg)
                .positiveText("确定")
                .onPositive(callback)
                .cancelable(false)
                .build()
                .show();
    }

    /**
     * 检查离线地图是否存在
     *
     * @return 是否存在
     */
    protected boolean checkOfflineMapFile(String mapFileName) {
        if (isMapOfflineDirExits()) {
            return isMapFileExits(mapFileName);
        }
        return false;
    }

    private File getMapFile(String fileName) {
        File mapDir = getMapDir();
        if (!mapDir.exists()) {
            mapDir.mkdirs();
        }
        final String downloadPath = mapDir.getAbsolutePath() + File.separator + fileName;
        File file = new File(downloadPath);
        return file;
    }

    private File getMapDir() {
        File appExternalDir = null;
        if (Build.VERSION.SDK_INT > 20) {
            appExternalDir = getExternalFilesDir(null);
        } else {
            appExternalDir = Environment.getExternalStorageDirectory();
        }
        if (appExternalDir == null) {
            return null;
        }
        return new File(appExternalDir, HybirdConst.PATH.mapPath);
    }

    public boolean isMapOfflineDirExits() {
        File mapDir = getMapDir();
        return null != mapDir && mapDir.exists();
    }

    public boolean isMapFileExits(String mapFileName) {
        File mapFile = getMapFile(mapFileName);
        return mapFile.exists();
    }

    //下载离线地图
    protected void downloadMapFile(String mapFileName) {
        File mapFile = getMapFile(mapFileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
    }

    protected void downloadMapFile(String appBaseUrl, String fileName) {
        File mapFile = getMapFile(fileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
        DownLoadTool downLoadTool = new DownLoadTool
                .Builder()
                .fileName(fileName)
                .showProgress(true)
                .storeDir(getMapDir())
                .withContext(this)
                .build();
        Observable<ResponseBody> observable = new Retrofit
                .Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(appBaseUrl)
                .build()
                .create(DownLoadApi.class)
                .getOfflineMapFile(fileName);
        downLoadTool.apply(observable);
    }

    protected void downloadMapFile(String appBaseUrl, String fileName, DownLoadTool.DownLoadStatusCallback statusCallback) {
        File mapFile = getMapFile(fileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
        DownLoadTool downLoadTool = new DownLoadTool
                .Builder()
                .fileName(fileName)
                .showProgress(false)
                .storeDir(getMapDir())
                .withContext(this)
                .build();
        Observable<ResponseBody> observable = new Retrofit
                .Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(appBaseUrl)
                .build()
                .create(DownLoadApi.class)
                .getOfflineMapFile(fileName);
        downLoadTool.apply(observable, statusCallback);
    }

    /**
     * 下载APK
     *
     * @param appInfo
     */
    protected void downloadApkFile(String baseurl, AppInfo appInfo) {
        DownLoadTool downLoadTool = new DownLoadTool
                .Builder()
                .fileName(appInfo.getPackageName() + ".apk")
                .showProgress(true)
                .storeDir(getExternalFilesDir(null))
                .withContext(this)
                .build();
        Observable<ResponseBody> observable = new Retrofit
                .Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseurl)
                .build()
                .create(DownLoadApi.class)
                .getApkFile(appInfo.getPackageName());
        downLoadTool.apply(observable);
    }

}
