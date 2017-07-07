package me.xujichang.hybirdbase.module.gesturelock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xujichang.hybirdbase.R;


/**
 * Created by xjc on 2017/6/28.
 */

public class SelectGestureNumberFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_num, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView tvNumThree = (TextView) view.findViewById(R.id.tv_num_three);
        TextView tvNumFourth = (TextView) view.findViewById(R.id.tv_num_fourth);
        TextView tvNumFive = (TextView) view.findViewById(R.id.tv_num_five);
        TextView tvNumSix = (TextView) view.findViewById(R.id.tv_num_six);
        tvNumThree.setOnClickListener(this);
        tvNumFourth.setOnClickListener(this);
        tvNumFive.setOnClickListener(this);
        tvNumSix.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int num;
        int id = v.getId();
        if (id == R.id.tv_num_three) {
            num = 3;
        } else if (id == R.id.tv_num_fourth) {
            num = 4;
        } else if (id == R.id.tv_num_five) {
            num = 5;
        } else if (id == R.id.tv_num_six) {
            num = 6;
        } else {
            num = 3;
        }
        if (null != callBack) {
            callBack.onSelectedNum(num);
        }
    }

    public interface NumSelectedCallBack {
        void onSelectedNum(int num);
    }

    private NumSelectedCallBack callBack;

    public void setCallBack(NumSelectedCallBack callBack) {
        this.callBack = callBack;
    }
}
