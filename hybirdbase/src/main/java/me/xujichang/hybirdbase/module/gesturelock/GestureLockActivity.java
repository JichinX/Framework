package me.xujichang.hybirdbase.module.gesturelock;

import android.arch.lifecycle.LifecycleActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.FrameLayout;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.module.gesturelock.fragment.GestureSettingFragment;
import me.xujichang.hybirdbase.module.gesturelock.fragment.GestureUnlockFragment;

/**
 * APP手势锁
 * Created by xjc on 2017/6/27.
 */
public class GestureLockActivity extends LifecycleActivity {

    public static final String GESTURE_FLAG = "gesture";
    //设置 密码
    public static final int TYPE_SETTING = 0;
    //清除 密码
    public static final int TYPE_CLEAR = 1;
    //修改 密码
    public static final int TYPE_ALTER = 2;
    //锁屏 强制锁屏 密码通过才能返回
    public static final int TYPE_LOCK = 3;
    //验证密码 不强制锁屏
    public static final int TYPE_CONFIRM = 4;

    private FrameLayout fragmentContainer;
    private GestureSettingFragment settingFragment;
    private GestureUnlockFragment unLockFragment;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(GESTURE_FLAG, TYPE_LOCK);
        if (type == TYPE_LOCK) {
            setTheme(R.style.lockStyle);
            //设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.layout_gesture);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        initFragment(type, fragmentContainer);

    }

    private void initFragment(int type, FrameLayout fragmentContainer) {
        Bundle bundle = new Bundle();
        bundle.putInt(GESTURE_FLAG, type);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (type) {
            case TYPE_SETTING:
            case TYPE_ALTER:
                //设置
                if (null == settingFragment) {
                    settingFragment = new GestureSettingFragment();
                    settingFragment.setSettingListener(new GestureSettingFragment.SettingListener() {
                        @Override
                        public void onSetting() {
                            finishDelay();
                        }
                    });
                }
                settingFragment.setArguments(bundle);
                transaction.replace(fragmentContainer.getId(), settingFragment);
                break;
            case TYPE_LOCK:
            case TYPE_CONFIRM:
                if (null == unLockFragment) {
                    unLockFragment = new GestureUnlockFragment();
                    unLockFragment.setUnLockListener(new GestureUnlockFragment.UnLockListener() {
                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                finishDelay();
                            }
                        }
                    });
                }
                unLockFragment.setArguments(bundle);
                transaction.replace(fragmentContainer.getId(), unLockFragment);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void finishDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        if (type == TYPE_LOCK) {
            return;
        }
        super.onBackPressed();
    }
}
