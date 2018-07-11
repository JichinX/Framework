package me.xujichang.framework;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.xujichang.hybirdbase.module.web.HyBirdWebViewActivity;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/26 17:18.
 */

public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
    }
}
