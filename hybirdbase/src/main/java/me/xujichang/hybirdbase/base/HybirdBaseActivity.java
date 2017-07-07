package me.xujichang.hybirdbase.base;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xujichang.utils.activity.SuperActivity;

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
}
