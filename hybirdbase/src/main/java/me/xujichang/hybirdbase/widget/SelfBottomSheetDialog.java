package me.xujichang.hybirdbase.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;

import me.xujichang.hybirdbase.R;

import static com.xujichang.utils.tool.DisplayTool.getScreenHeight;
import static com.xujichang.utils.tool.DisplayTool.getStatusBarHeight;

/**
 * Created by xjc on 2017/6/1.
 */

public class SelfBottomSheetDialog extends BottomSheetDialog {
    private Context context;
    private boolean transparent = false;

    public SelfBottomSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public SelfBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public SelfBottomSheetDialog(Context context, boolean transparent) {
        this(context);
        this.transparent = transparent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        int screenHeight = getScreenHeight((Activity) context);
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        if (transparent) {
            getWindow().findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        }
    }
}
