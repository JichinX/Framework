package me.xujichang.hybirdbase.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xjc on 2017/6/9.
 */

public class ClickedEditText extends AppCompatEditText {
    public static final int DRAWABLE_LEFT = 0;
    public static final int DRAWABLE_TOP = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_BOTTOM = 3;
    private Drawable[] drawables = new Drawable[4];
    private boolean deleteModel = false;
    private Drawable deleteDrawable;

    public ClickedEditText(Context context) {
        super(context);
        init();
    }

    public ClickedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawables = getCompoundDrawables();
        deleteDrawable = drawables[2];
        setDrawable();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (deleteModel) {
                    setDrawable();
                }
            }
        });
    }

    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null, drawables[3]);
        else
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], deleteDrawable, drawables[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == drawableClickListener) {
            return super.onTouchEvent(event);
        }
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            boolean clicked = false;
            for (int i = 0; i < drawables.length; i++) {
                if (checkDrawClicked(i, x, y)) {
                    clicked = true;
                    break;
                }
            }
            return !clicked && super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean checkDrawClicked(int index, float x, float y) {
        Drawable drawable = drawables[index];
        if (null == drawable) {
            return false;
        }
        boolean clicked = false;
        switch (index) {
            case DRAWABLE_BOTTOM:
                if (x > getWidth() - getPaddingRight() - drawable.getIntrinsicWidth()) {
                    drawableClickListener.onDrawClick(DRAWABLE_BOTTOM, drawable, getText());
                    clicked = true;
                }
                break;
            case DRAWABLE_TOP:
                if (x > getWidth() - getPaddingRight() - drawable.getIntrinsicWidth()) {
                    drawableClickListener.onDrawClick(DRAWABLE_TOP, drawable, getText());
                    clicked = true;
                }
                break;
            case DRAWABLE_LEFT:
                if (x < drawable.getIntrinsicWidth()) {
                    drawableClickListener.onDrawClick(DRAWABLE_LEFT, drawable, getText());
                    clicked = true;
                }
                break;
            case DRAWABLE_RIGHT:
                if (x > getWidth() - getPaddingRight() - drawable.getIntrinsicWidth()) {
                    drawableClickListener.onDrawClick(DRAWABLE_RIGHT, drawable, getText());
                    clicked = true;
                }
                break;
        }
        return clicked;
    }

    public void setDeleteModel(boolean b) {
        deleteModel = true;
    }

    public interface onDrawableClickListener {
        void onDrawClick(int index, Drawable drawable, Editable editable);
    }

    private onDrawableClickListener drawableClickListener;

    public void setonDrawableClickedListener(onDrawableClickListener listener) {
        drawableClickListener = listener;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}
