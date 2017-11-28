package me.xujichang.hybirdbase.module.web.loading;

import com.afollestad.materialdialogs.MaterialDialog;

import me.xujichang.hybirdbase.module.web.interfaces.IWebLoading;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/25 17:04.
 */

public class DialogLoading implements IWebLoading {
    /**
     * Dialog
     */
    private MaterialDialog mDialog;
    /**
     * 不确定
     */
    private boolean isIndeterminate;

    public DialogLoading(MaterialDialog dialog) {
        mDialog = dialog;
        isIndeterminate = mDialog.getProgressBar().isIndeterminate();
    }

    @Override
    public void start() {
        if (null != mDialog) {
            mDialog.show();
        }
    }

    @Override
    public void stop() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    @Override
    public void progress(int progress) {
        if (isIndeterminate) {
            //如果是不确定的进度，不需要设置进度
            return;
        }
        if (null != mDialog) {
            mDialog.setProgress(progress);
        }
    }
}
