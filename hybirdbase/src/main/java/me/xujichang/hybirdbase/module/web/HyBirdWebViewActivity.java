package me.xujichang.hybirdbase.module.web;

import android.webkit.WebSettings;

import me.xujichang.hybirdbase.base.HybirdBaseWebView;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/25 17:03.
 */

public abstract class HyBirdWebViewActivity extends HybirdBaseWebView {
    @Override
    protected void initWebSetting(WebSettings settings) {
        WebSettingConst.patchDefaultSetting(settings);
    }

}
