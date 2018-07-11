package me.xujichang.hybirdbase.module.web.loading;

import android.view.View;
import android.widget.ProgressBar;

import me.xujichang.hybirdbase.module.web.interfaces.IWebLoading;

/**
 * Des:loading
 * progress类型的进度形式
 *
 * @author xjc
 *         Created on 2017/11/25 17:05.
 */

public class ProgressLoading implements IWebLoading {
    private ProgressBar mProgressBar;

    public ProgressLoading(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void start() {
        if (null != mProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stop() {
        if (null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void progress(int progress) {
        if (null != mProgressBar) {
            mProgressBar.setProgress(progress);
        }
    }
}
