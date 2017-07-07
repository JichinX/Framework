package me.xujichang.hybirdbase.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;

import me.xujichang.hybirdbase.module.gesturelock.util.VibratorUtil;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by xjc on 2017/6/27.
 */

public class GestureLock extends View {
    public static final int MODE_SETTING = 10;
    public static final int MODE_LOCK = 11;
    public static final int RESULT_DEFAULT = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAIL = 2;
    private static final String TAG = "GestureLock";
    //点数量
    private int number;
    private Paint paint;
    private int circleSize;
    private int circleRadius;
    private int baseX;
    private int baseY;
    private ArrayList<RectF> points;
    private ArrayList<RectF> selectedPoints;
    private ArrayList<Integer> results;
    private PointF movePoint;
    private int result = RESULT_DEFAULT;
    //路径 颜色
    private int lineDefaultColor;
    private int lineSuccessColor;
    private int lineFailColor;
    //路径宽度
    private int lineWidth;
    private int selectedPointColor;
    private int pointDefaultColor;
    private int pointRadius;
    private int minHeight;
    private Context context;
    private int mode = MODE_LOCK;

    public GestureLock(Context context) {
        this(context, null);
    }

    public GestureLock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        points = new ArrayList<>();
        results = new ArrayList<>();
        selectedPoints = new ArrayList<>();
        paint = new Paint();
        lineDefaultColor = Color.YELLOW;
        lineSuccessColor = Color.GREEN;
        lineFailColor = Color.RED;
        lineWidth = 6;
        number = 3;
        minHeight = dip2px(context, 300);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取宽高
        int height = getHeight();
        int width = getWidth();
        //取最小 作为手机滑动范围
        int size = Math.min(height, width);
        //圆直径
        circleSize = size / number;
        //点的半径
        if (pointRadius == 0) {
            circleRadius = circleSize / 20;
        } else {
            circleRadius = pointRadius;
        }
        baseY = (height - size) / 2;
        baseX = (width - size) / 2;
        //画点
        points.clear();
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                drawPoint(canvas, i, j);
            }
        }
        //画线
        paint.reset();
        paint.setColor(Color.YELLOW);
        drawLine(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //比较宽高 取最小
        int size = Math.min(width, height);
        if (width < height && heightMode == AT_MOST) {
            height = size;
        }
        if (height < width && widthMode == AT_MOST) {
            width = size;
        }
        if (heightMode == AT_MOST && widthMode == AT_MOST) {
            height = minHeight;
        }
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//match
            return size;
        }
        return minHeight;
    }

    /**
     * 画触摸点
     *
     * @param canvas
     * @param i
     * @param j
     */
    private void drawPoint(Canvas canvas, int i, int j) {
        //半径
        int size = circleSize / 2;
        int pointX = circleSize * i + size + baseX;
        int pointY = circleSize * j + size + baseY;
        paint.reset();
        RectF temp = new RectF(pointX - size / 3, pointY - size / 3, pointX + size / 3, pointY + size / 3);
        if (selectedPoints.contains(temp)) {
            //选中后的点 的颜色 要跟 线的颜色相同
            paint.setColor(getLineColor());
            canvas.drawCircle(pointX, pointY, circleRadius * 3 / 2, paint);
        } else {
            paint.setColor(getPointDefaultColor());
            canvas.drawCircle(pointX, pointY, circleRadius, paint);
        }
        points.add(temp);
    }

    private int getPointDefaultColor() {
        if (pointDefaultColor != 0) {
            return pointDefaultColor;
        }
        return Color.WHITE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                if (movePoint == null) {
                    movePoint = new PointF();
                }
                movePoint.set(moveX, moveY);
                judgePoint(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                if (selectedPoints.size() <= 1) {
                    selectedPoints.clear();
                }
                movePoint = null;
                if (null != lockListener) {
                    lockListener.onResult(this, results);
                }
                results.clear();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isShown() && mode == MODE_LOCK) {
                            //1秒后 消失
                            selectedPoints.clear();
                            result = RESULT_DEFAULT;
                            invalidate();
                        }
                    }
                }, 1000);
                break;
            case MotionEvent.ACTION_DOWN:
                if (null != lockListener) {
                    lockListener.onGestureDown();
                }
                float startX = event.getX();
                float startY = event.getY();
                results.clear();
                selectedPoints.clear();
                result = RESULT_DEFAULT;
                judgePoint(startX, startY);
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 判断点所在位置，并记录
     *
     * @param startX
     * @param startY
     */
    private void judgePoint(float startX, float startY) {
        int index = -1;
        RectF rectF = new RectF();
        for (RectF rect : points) {
            if (rect.contains(startX, startY)) {
                index = points.indexOf(rect);
                rectF.set(rect);
                break;
            }
        }
        if (index != -1) {
            if (results.contains(index)) {
                return;
            }
            results.add(index);
            selectedPoints.add(rectF);
            //选中 之后 会给与震动提示
            VibratorUtil.Vibrate(context, 100);
        }
    }

    private void drawLine(Canvas canvas) {
        paint.reset();
        paint.setColor(getLineColor());
        paint.setStrokeWidth(lineWidth);
        PointF start = new PointF();
        RectF rectF;
        for (int i = 0; i < selectedPoints.size(); i++) {
            rectF = selectedPoints.get(i);
            if (i != 0) {
                canvas.drawLine(start.x, start.y, rectF.centerX(), rectF.centerY(), paint);
            }
            start.set(rectF.centerX(), rectF.centerY());
        }
        if (null != movePoint && selectedPoints.size() > 0) {
            //画在移动的线
            canvas.drawLine(start.x, start.y, movePoint.x, movePoint.y, paint);
        }
    }

    private int getLineColor() {
        if (result == RESULT_DEFAULT) {
            return lineDefaultColor;
        }
        if (result == RESULT_FAIL) {
            return lineFailColor;
        }
        if (result == RESULT_SUCCESS) {
            return lineSuccessColor;
        }
        return lineDefaultColor;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void clear() {
        results.clear();
        selectedPoints.clear();
        movePoint = null;
        result = RESULT_DEFAULT;
        invalidate();
    }

    public interface GestureLockListener {
        void onResult(GestureLock lock, ArrayList<Integer> results);

        void onGestureDown();
    }

    private GestureLockListener lockListener;

    public void setLockListener(GestureLockListener lockListener) {
        this.lockListener = lockListener;
    }

    public void onResultCallBack(int result) {
        this.result = result;
    }

    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }

    public void setLineDefaultColor(int lineDefaultColor) {
        this.lineDefaultColor = lineDefaultColor;
    }

    public void setLineFailColor(int lineFailColor) {
        this.lineFailColor = lineFailColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineSuccessColor(int lineSuccessColor) {
        this.lineSuccessColor = lineSuccessColor;
    }

    public void setNumber(int number) {
        this.number = number;
        invalidate();
    }

    public void setSelectedPointColor(int selectedPointColor) {
        this.selectedPointColor = selectedPointColor;
    }

    public void setPointDefaultColor(int pointDefaultColor) {
        this.pointDefaultColor = pointDefaultColor;
        invalidate();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
