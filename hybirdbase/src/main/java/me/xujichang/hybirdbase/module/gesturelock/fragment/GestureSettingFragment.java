package me.xujichang.hybirdbase.module.gesturelock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.module.gesturelock.GestureLockActivity;
import me.xujichang.hybirdbase.module.gesturelock.util.GestureLockUtil;

/**
 * 设置手势密码
 * Created by xjc on 2017/6/28.
 */

public class GestureSettingFragment extends Fragment {
    private TextView tvSettingTip;
    private FrameLayout flSettingFragmentContainer;
    private int number = -1;
    private FragmentManager childManager;
    private ArrayList<Integer> result;
    private int lockNum;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        type = getArguments().getInt(GestureLockActivity.GESTURE_FLAG, GestureLockActivity.TYPE_SETTING);
        super.onCreate(savedInstanceState);
        childManager = getChildFragmentManager();
        result = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gesture_setting, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvSettingTip = (TextView) view.findViewById(R.id.tv_setting_tip);
        flSettingFragmentContainer = (FrameLayout) view.findViewById(R.id.fl_setting_fragment_container);
        SelectGestureNumberFragment selectGestureNumberFragment = new SelectGestureNumberFragment();
        final GestureInitFragment gestureInitFragment = new GestureInitFragment();
        selectGestureNumberFragment.setCallBack(new SelectGestureNumberFragment.NumSelectedCallBack() {
            @Override
            public void onSelectedNum(int num) {
                lockNum = num;
                //选择大小 进入下一步
                tvSettingTip.setText("选择解锁图案");
                Bundle bundle = new Bundle();
                bundle.putInt("lock_point_num", num);
                selectFragment(gestureInitFragment, bundle);
            }
        });
        gestureInitFragment.setInitListener(new GestureInitFragment.GestureInitListener() {
            @Override
            public void onCancel() {
                //取消密码设置
                getActivity().finish();
            }

            @Override
            public void onSure(ArrayList<Integer> results) {
                if (result.size() == 0) {
                    //确认第一次设置
                    result.addAll(results);
                    tvSettingTip.setText("请再次确认解锁图案");
                } else {
                    //作比较
                    if (GestureLockUtil.comparePassword(result, results)) {
                        //两次密码一致
                        tvSettingTip.setText("手势密码 设置成功");
                        GestureLockUtil.saveLockInfo(getContext(), result, lockNum);
                        if (null != settingListener) {
                            settingListener.onSetting();
                        }
                    } else {
                        //两次密码不一致
                        tvSettingTip.setText("两次密码不一致，请重新绘制");
                    }
                }
            }
        });
        if (type == GestureLockActivity.TYPE_ALTER) {
            int num = GestureLockUtil.getLockNum(getContext());
            lockNum = num;
            Bundle bundle = new Bundle();
            bundle.putInt("lock_point_num", num);
            selectFragment(gestureInitFragment, bundle);
        } else {
            selectFragment(selectGestureNumberFragment, null);
        }
    }

    private void selectFragment(Fragment fragment, Bundle bundle) {
        if (null != bundle) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction transaction = childManager.beginTransaction();
        transaction.replace(flSettingFragmentContainer.getId(), fragment);
        transaction.commitAllowingStateLoss();
    }

    public void setSettingListener(SettingListener settingListener) {
        this.settingListener = settingListener;
    }

    public interface SettingListener {

        void onSetting();
    }

    private SettingListener settingListener;
}
