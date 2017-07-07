package me.xujichang.hybirdbase.module.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.base.HybirdBaseActivity;
import me.xujichang.hybirdbase.module.gesturelock.util.GestureLockUtil;

/**
 * Created by xjc on 2017/6/28.
 */

public class GestureOperationActivity extends HybirdBaseActivity {
    private TextView tvClearGesture;
    private TextView tvSetGesture;
    private TextView tvAlterGesture;
    private TextView tvLockApp;
    private Context context;
    private boolean unlock = false;
    private boolean isLockSet = false;
    private TextView tvLockStatus;
    private int requestType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_operation);
        context = this;
        initView();
    }

    private void initView() {
        initActionBar();
        tvClearGesture = (TextView) findViewById(R.id.tv_clear_gesture);
        tvSetGesture = (TextView) findViewById(R.id.tv_set_gesture);
        tvAlterGesture = (TextView) findViewById(R.id.tv_alter_gesture);
        tvLockApp = (TextView) findViewById(R.id.tv_lock_app);
        tvLockStatus = (TextView) findViewById(R.id.tv_lock_status);
        tvLockStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置
                if (isLockSet) {
                    if (!unlock) {
                        //验证手势密码
                        toLockActivity(GestureLockActivity.TYPE_CONFIRM, GestureLockActivity.TYPE_CONFIRM);
                    }
                } else {
                    //设置手势密码
                    toLockActivity(GestureLockActivity.TYPE_SETTING, GestureLockActivity.TYPE_SETTING);
                }
            }
        });
        tvClearGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除
                startOperation(GestureLockActivity.TYPE_CLEAR);
            }
        });
        tvAlterGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改
                startOperation(GestureLockActivity.TYPE_ALTER);
            }

        });
        tvSetGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOperation(GestureLockActivity.TYPE_SETTING);
            }
        });
        tvLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //锁定
                startOperation(GestureLockActivity.TYPE_LOCK);
            }
        });

    }

    private void initActionBar() {
        showBackArrow();
        setActionBarTitle("手势密码设置");
    }

    private void startOperation(int type) {
        if (isLockSet) {
            if (!unlock) {
                if (type == GestureLockActivity.TYPE_LOCK) {
                    toLockActivity(GestureLockActivity.TYPE_LOCK, -1);
                    return;
                }
                //先解锁 去解锁
                toLockActivity(GestureLockActivity.TYPE_CONFIRM, type);
            } else {
                if (type == GestureLockActivity.TYPE_CLEAR) {
                    showClearDialog();
                    return;
                }
                toLockActivity(type, -1);
            }
        } else {
            if (type == GestureLockActivity.TYPE_SETTING) {
                toLockActivity(type, -1);
                return;
            }
            showNoLockDialog();
        }
    }

    private void showClearDialog() {
        showWarningDialog("您将删除之前设置的手势密码，确定要删除吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    GestureLockUtil.clearLockInfo(context);
                    updateStatus();
                }
                dialog.dismiss();
            }
        });
    }

    private void toLockActivity(int type, int request) {
        requestType = request;
        Intent intent = new Intent(this, GestureLockActivity.class);
        intent.putExtra(GestureLockActivity.GESTURE_FLAG, type);
        if (request != -1) {
            startActivityForResult(intent, 12);
        } else {
            startActivity(intent);
        }
    }

    private void showNoLockDialog() {
        showWarningDialog("您并未设置手势密码,需要现在立刻去设置吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    //去设置密码
                    toLockActivity(GestureLockActivity.TYPE_SETTING, -1);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showToast("您取消了操作");
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                dealNextOperation();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void dealNextOperation() {
        if (requestType == -1) {
            return;
        }
        switch (requestType) {
            case GestureLockActivity.TYPE_CONFIRM:
                //解锁成功
                unlock = true;
                updateStatus();
                break;
            case GestureLockActivity.TYPE_SETTING:
                updateStatus();
                break;
            case GestureLockActivity.TYPE_CLEAR:
                showClearDialog();
                break;
            case GestureLockActivity.TYPE_ALTER:
                toLockActivity(GestureLockActivity.TYPE_ALTER, -1);
                break;
        }
    }

    private void updateStatus() {
        isLockSet = GestureLockUtil.isPassWordSet(context);
        if (isLockSet) {
            if (!unlock) {
                tvLockStatus.setText("您需要验证手势密码，才可进行操作，点击进行验证");
                tvLockStatus.setBackgroundColor(getResources().getColor(R.color.material_red_500));
            } else {
                tvLockStatus.setVisibility(View.GONE);
            }
        } else {
            tvLockStatus.setText("您还未设置手势密码，点击此处可进行设置");
            tvLockStatus.setBackgroundColor(getResources().getColor(R.color.material_red_500));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    @Override
    protected long getActivityExitDuration() {
        return 0;
    }

    @Override
    protected String getMainActivityName() {
        return "";
    }
}
