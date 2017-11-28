package me.xujichang.hybirdbase.module.web;

import me.xujichang.hybirdbase.base.HybirdBaseWebView;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/25 17:03.
 */

public class HyBirdWebViewActivity extends HybirdBaseWebView {
    @Override
    protected long getActivityExitDuration() {
        return 1000;
    }

    @Override
    protected String getMainActivityName() {
        return "MainActivity";
    }
}
