package me.xujichang.hybirdbase.module.web;

import android.os.Build;
import android.webkit.WebSettings;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/30 10:32.
 */

public class WebSettingConst {
    /**
     * 支持显示地图
     *
     * @param settings
     */
    public static void patchMapSupportSetting(WebSettings settings) {
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //隐藏缩放
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        //允许DOM
        settings.setDomStorageEnabled(true);
    }

    /**
     * 需要定位
     *
     * @param settings
     */
    public static void patchMapLocationSetting(WebSettings settings) {
        settings.setDatabaseEnabled(true);
        //支持JavaScriptEnabled
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //定位
        settings.setGeolocationEnabled(true);
    }

    public static void patchDefaultSetting(WebSettings settings) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    public static void patchNoCacheSetting(WebSettings settings) {
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }
}
