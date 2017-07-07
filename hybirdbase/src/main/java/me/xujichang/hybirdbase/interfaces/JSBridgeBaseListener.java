package me.xujichang.hybirdbase.interfaces;

/**
 * Created by xjc on 2017/5/23.
 */

public interface JSBridgeBaseListener {

    String getLocation();

    String getUserInfo();

    String getDeviceInfo();

    void finishActivity();

    void startAnotherActivity(String activityName);

    void showToast(String queryParameter);

    void startLoading(String queryParameter);

    void stopLoading();

    void showDialog(String queryParameter);

}
