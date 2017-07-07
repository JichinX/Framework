package me.xujichang.hybirdbase.module.gesturelock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.module.gesturelock.GestureLockActivity;
import me.xujichang.hybirdbase.module.gesturelock.util.GestureLockUtil;
import me.xujichang.hybirdbase.widget.GestureLock;

/**
 * Created by xjc on 2017/6/28.
 */

public class GestureUnlockFragment extends Fragment {
    private TextView tvResult;
    private GestureLock glLock;
    private TextView tvForgetPassword;
    private ArrayList<Integer> password;
    private int lockNum = 3;
    private TextView tvLockMessage;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(GestureLockActivity.GESTURE_FLAG);
        lockNum = GestureLockUtil.getLockNum(getContext());
        password = GestureLockUtil.getLockPassword(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gesture_unlock, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        glLock = (GestureLock) view.findViewById(R.id.gl_lock);
        tvLockMessage = (TextView) view.findViewById(R.id.tv_lock_message);
        tvForgetPassword = (TextView) view.findViewById(R.id.tv_forget_password);
        if (type == GestureLockActivity.TYPE_CONFIRM) {
            tvLockMessage.setText("该操作需验证密码");
        } else {
            tvLockMessage.setText("已锁定");
        }
        glLock.setNumber(lockNum);
        glLock.setLockListener(new GestureLock.GestureLockListener() {
            @Override
            public void onResult(GestureLock lock, ArrayList<Integer> results) {
                if (null == unLockListener) {
                    return;
                }
                if (GestureLockUtil.comparePassword(results, password)) {
                    //密码正确
                    tvResult.setTextColor(getResources().getColor(R.color.material_white));
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText("密码正确，请稍等");
                    if (type == GestureLockActivity.TYPE_LOCK) {
                        tvLockMessage.setText("已解锁");
                    }
                    unLockListener.onResult(true);
                } else {
                    //密码错误
                    tvResult.setTextColor(getResources().getColor(R.color.material_red_500));
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText("密码错误，请重试");
                    unLockListener.onResult(false);
                }
            }

            @Override
            public void onGestureDown() {
                tvResult.setVisibility(View.INVISIBLE);
                tvResult.setText("");

            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public interface UnLockListener {
        void onResult(boolean result);
    }

    private UnLockListener unLockListener;

    public void setUnLockListener(UnLockListener unLockListener) {
        this.unLockListener = unLockListener;
    }
}
