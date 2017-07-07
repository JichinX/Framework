package me.xujichang.hybirdbase.interfaces;

import com.xujichang.utils.base.SuperView;

/**
 * Created by xjc on 2017/5/23.
 */

public interface HybirdBaseView<T> extends SuperView<T> {
    void loadFail(int code, String msg);
}
