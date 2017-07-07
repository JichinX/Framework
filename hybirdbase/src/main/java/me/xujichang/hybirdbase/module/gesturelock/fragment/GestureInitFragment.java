package me.xujichang.hybirdbase.module.gesturelock.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.widget.GestureLock;

/**
 * Created by xjc on 2017/6/28.
 */

public class GestureInitFragment extends Fragment {
    private GestureLock glLock;
    private TextView btnGestureCancel;
    private TextView btnGestureOk;
    private int num = 3;
    private ArrayList<Integer> integers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getArguments().getInt("lock_point_num");
        if (num == 0) {
            num = 3;
        }
        integers = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gesture_init, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        glLock = (GestureLock) view.findViewById(R.id.gl_lock);
        btnGestureCancel = (TextView) view.findViewById(R.id.btn_gesture_cancel);
        btnGestureOk = (TextView) view.findViewById(R.id.btn_gesture_ok);
        glLock.setPointDefaultColor(Color.BLUE);
        glLock.setNumber(num);
        btnGestureCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != initListener) {
                    initListener.onCancel();
                }
                //取消设置
            }
        });
        btnGestureOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认本次的设置
                if (null != initListener) {
                    initListener.onSure(integers);
                }
                glLock.clear();
            }
        });
        glLock.setMode(GestureLock.MODE_SETTING);
        glLock.setLockListener(new GestureLock.GestureLockListener() {
            @Override
            public void onResult(GestureLock lock, ArrayList<Integer> results) {
                integers.clear();
                integers.addAll(results);
                btnGestureOk.setEnabled(results.size() != 0);
            }

            @Override
            public void onGestureDown() {
                btnGestureOk.setEnabled(false);
            }
        });
        btnGestureOk.setEnabled(false);

    }

    public interface GestureInitListener {
        void onCancel();

        void onSure(ArrayList<Integer> results);
    }

    private GestureInitListener initListener;

    public void setInitListener(GestureInitListener initListener) {
        this.initListener = initListener;
    }
}
