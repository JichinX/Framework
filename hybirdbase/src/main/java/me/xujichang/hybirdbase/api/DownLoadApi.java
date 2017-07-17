package me.xujichang.hybirdbase.api;

import com.xujichang.utils.bean.AppInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by xjc on 2017/7/7.
 */

public interface DownLoadApi {

    @GET("static/app/update/{name}.json")
    Observable<AppInfo> getAppInfo(@Path("name") String name);

    @Streaming
    @GET("static/app/update/{name}.apk")
    Observable<ResponseBody> getApkFile(@Path("name") String name);

    @Streaming
    @GET("static/app/map/{name}")
    Observable<ResponseBody> getOfflineMapFile(@Path("name") String name);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
