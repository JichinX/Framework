package me.xujichang.hybirdbase.base;

import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xujichang.utils.activity.SuperActivity;
import com.xujichang.utils.bean.AppInfo;
import com.xujichang.utils.download.DownLoadTool;
import com.xujichang.utils.tool.LogTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

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

    /**
     * 检查备份文件
     *
     * @param fileName
     * @return
     */
    protected boolean checkCacheMapFile(String fileName) {
        LogTool.d("checkCacheMapFile");
        File cacheDir = getCacheMapDir();
        if (null == cacheDir || !cacheDir.exists()) {
            return false;
        }
        File file = new File(cacheDir, fileName);
        return file.exists();
    }

    private boolean isCacheFileExits(String fileName) {
        return false;
    }

    private boolean isMapCacheDirExits() {
        return false;
    }

    private File getMapFile(String fileName) {
        File mapDir = getMapDir();
        if (!mapDir.exists()) {
            mapDir.mkdirs();
        }
        String downloadPath = mapDir.getAbsolutePath() + File.separator + fileName;
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

    private File getCacheMapDir() {
        File cacheMapDir = null;
        cacheMapDir = Environment.getExternalStorageDirectory();
        if (cacheMapDir == null) {
            return null;
        }
        return new File(cacheMapDir, HybirdConst.PATH.cmapCachePath);
    }

    public boolean isMapOfflineDirExits() {
        File mapDir = getMapDir();
        return null != mapDir && mapDir.exists();
    }

    public boolean isMapFileExits(String mapFileName) {
        File mapFile = getMapFile(mapFileName);
        return mapFile.exists();
    }

    /**
     * 下载离线地图 使用默认的额进度显示
     *
     * @param appBaseUrl
     * @param fileName
     */
    protected void downloadMapFile(String appBaseUrl, String fileName) {
        downloadMapFile(appBaseUrl, fileName, null);
    }

    /**
     * 下载离线地图 使用callback返回进度 供自定义进度显示
     *
     * @param appBaseUrl
     * @param fileName
     * @param statusCallback
     */
    protected void downloadMapFile(String appBaseUrl, String fileName, DownLoadTool.DownLoadStatusCallback statusCallback) {
        File mapFile = getMapFile(fileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
        DownLoadTool downLoadTool = new DownLoadTool
                .Builder()
                .fileName(fileName)
                .showProgress(statusCallback == null)
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
        if (statusCallback == null) {
            statusCallback = new DownLoadOfflineMapWithCacheCallBack();
        } else if (!(statusCallback instanceof DownLoadOfflineMapWithCacheCallBack)) {
            throw new RuntimeException("callback  should extends DownLoadOfflineMapWithCacheCallBack");
        }
        downLoadTool.apply(observable, statusCallback);
    }

    /**
     * 使用自己的链接配置
     */
    protected void downloadMapFile(String fileName, Observable<ResponseBody> observable, DownLoadTool.DownLoadStatusCallback statusCallback) {
        File mapFile = getMapFile(fileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
        DownLoadTool downLoadTool = new DownLoadTool
                .Builder()
                .fileName(fileName)
                .showProgress(statusCallback == null)
                .storeDir(getMapDir())
                .withContext(this)
                .build();
        if (statusCallback == null) {
            statusCallback = new DownLoadOfflineMapWithCacheCallBack();
        } else if (!(statusCallback instanceof DownLoadOfflineMapWithCacheCallBack)) {
            throw new RuntimeException("callback  should extends DownLoadOfflineMapWithCacheCallBack");
        }
        downLoadTool.apply(observable, statusCallback);
    }

    /**
     * 使用自己的链接配置
     */
    protected void downloadMapFile(String fileName, Observable<ResponseBody> observable) {
        downloadMapFile(fileName, observable, null);
    }

    protected void patchMapFileFromCache(String fileName) {
        File mapFile = getMapFile(fileName);
        if (mapFile.exists()) {
            mapFile.delete();
        }
        new CopyFileThread(getCacheMapFile(fileName), mapFile).start();
        LogTool.d("取本地缓存");
    }

    private File getCacheMapFile(String fileName) {
        LogTool.d("getCacheMapFile");
        return new File(getCacheMapDir(), fileName);
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

    public class DownLoadOfflineMapWithCacheCallBack extends DownLoadTool.SimpleDownloadStatusCallBack {
        @Override
        public void onComplete(String fileName) {
            File mapFile = getMapFile(fileName);
            //下载完成之后 SDk备份一份
            if (null == mapFile || !mapFile.exists()) {
                return;
            }
            File cacheDir = getCacheMapDir();
            if (null == cacheDir) {
                return;
            }
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            String downloadPath = cacheDir.getAbsolutePath() + File.separator + mapFile.getName();
            File file = new File(downloadPath);
            new CopyFileThread(mapFile, file).start();
        }
    }

    protected boolean checkMapFile(String fileName) {
        if (checkOfflineMapFile(fileName)) {
            return true;
        }
        if (checkCacheMapFile(fileName)) {
            patchMapFileFromCache(fileName);
            return true;
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param oldFile
     * @param newFile
     */
    private void copyFile(File oldFile, File newFile) {

        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldFile); //读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            LogTool.d("message:------" + msg.toString());
            return false;
        }
    }

    private class CopyFileThread extends Thread {
        private File oldFile;
        private File newFile;

        public CopyFileThread(File oldFile, File newFile) {
            this.oldFile = oldFile;
            this.newFile = newFile;
        }

        @Override
        public void run() {
            copyFile(oldFile, newFile);
        }
    }
}
