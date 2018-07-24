package com.posin.systemupdate.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.posin.systemupdate.R;
import com.posin.systemupdate.utils.DensityUtils;


/**
 * FileName: CircleProgressBar
 * Author: Greetty
 * Time: 2018/7/20 11:36
 * Desc: TODO
 */
public class CircleProgressBar extends View {

    private static final String TAG = "CirclePressBar";

    //进度条颜色
    private int outsideColor;
    //外圆半径（进度条）大小
    private float outsideRadius;
    //背景颜色
    private int insideColor;
    //内环半径
    private float insideRadius;
    //圆环内文字颜色
    private int progressTextColor;
    //圆环内文字大小
    private float progressTextSize;
    //最大进度
    private int maxProgress;
    //当前进度
    private float progress;
    //进度从哪里开始(设置了4个值,上左下右)
    private int direction;
    //圆环内文字
    private String progressText;
    //画圆画笔
    private Paint mCirclePaint;
    //画文字画笔
    private Paint mTxtPaint;
    //文字基线
    private Rect rect;

    enum DirectionEnum {
        LEFT(0, 180.0f),
        TOP(1, 270.0f),
        RIGHT(2, 0.0f),
        BOTTOM(3, 90.0f);

        private final int direction;
        private final float degree;

        DirectionEnum(int direction, float degree) {
            this.direction = direction;
            this.degree = degree;
        }

        public int getDirection() {
            return direction;
        }

        public float getDegree() {
            return degree;
        }

        public boolean equalsDescription(int direction) {
            return this.direction == direction;
        }

        public static DirectionEnum getDirection(int direction) {
            for (DirectionEnum enumObject : values()) {
                if (enumObject.equalsDescription(direction)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction) {
            DirectionEnum enumObject = getDirection(direction);
            if (enumObject == null) {
                return 0;
            }
            return enumObject.getDegree();
        }
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint();
        mTxtPaint = new Paint();

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleProgressBar, defStyleAttr, 0);

        outsideColor = array.getColor(R.styleable.CircleProgressBar_outside_color,
                ContextCompat.getColor(context, R.color.colorCirProOutside));
        outsideRadius = DensityUtils.px2dp(getContext(),
                array.getDimension(R.styleable.CircleProgressBar_outside_radius,
                        getResources().getDimension(R.dimen.circle_progress_outside_radius)));

        insideColor = array.getColor(R.styleable.CircleProgressBar_outside_color,
                ContextCompat.getColor(context, R.color.colorCirProInside));
        insideRadius = DensityUtils.px2dp(getContext(),
                array.getDimension(R.styleable.CircleProgressBar_inside_radius,
                        getResources().getDimension(R.dimen.circle_progress_inside_radius)));

        progressTextColor = array.getColor(R.styleable.CircleProgressBar_progress_text_color,
                ContextCompat.getColor(context, R.color.colorCirProText));
        progressTextSize = DensityUtils.px2dp(getContext(),
                array.getDimension(R.styleable.CircleProgressBar_progress_text_size,
                        getResources().getDimension(R.dimen.circle_progress_text_size)));

        maxProgress = array.getInt(R.styleable.CircleProgressBar_max_progress_size,
                100);
        progress = array.getInt(R.styleable.CircleProgressBar_current_progress,
                0);
        progressText = array.getString(R.styleable.CircleProgressBar_progress_default_text);

        direction = array.getInt(R.styleable.CircleProgressBar_direction, 1);

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = (int) (2 * (insideRadius + outsideRadius));
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = (int) (2 * (insideRadius + outsideRadius));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int parentWidth = getPaddingLeft() + getWidth() / 2;
        int parentHeight = getPaddingTop() + getHeight() / 2;

        //画内圆
        mCirclePaint.setColor(insideColor);
        //抗锯齿
        mCirclePaint.setAntiAlias(true);
        //防抖动
        mCirclePaint.setDither(true);
        //设置空心
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置圆的宽度
        mCirclePaint.setStrokeWidth(outsideColor);
        canvas.drawCircle(parentWidth, parentHeight, insideRadius + outsideRadius, mCirclePaint);

        mCirclePaint.setStyle(Paint.Style.STROKE);
        //设置圆的宽度
        mCirclePaint.setStrokeWidth(outsideRadius);
        mCirclePaint.setColor(outsideColor);
//        canvas.drawCircle(parentWidth, parentHeight, insideRadius + outsideRadius, mCirclePaint);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(
                parentWidth - outsideRadius - insideRadius,
                parentHeight - outsideRadius - insideRadius,
                parentWidth + outsideRadius + insideRadius,
                parentHeight + outsideRadius + insideRadius);
        //根据进度画圆弧
        canvas.drawArc(oval, DirectionEnum.getDegree(direction), 360 * (progress / maxProgress),
                false, mCirclePaint);

        rect = new Rect();
        mTxtPaint.setColor(progressTextColor);
        Log.e(TAG, "progressTextSize: "+progressTextSize);
        mTxtPaint.setTextSize(30);
        mTxtPaint.setStrokeWidth(0);
        progressText = getProgressText();
        mTxtPaint.getTextBounds(progressText, 0, progressText.length(), rect);
        Paint.FontMetricsInt fontMetrics = mTxtPaint.getFontMetricsInt();
        //获得文字的基准线
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(progressText, getMeasuredWidth() / 2 - rect.width() / 2, baseline, mTxtPaint);
    }

    public String getProgressText() {
        return (int) ((progress / maxProgress) * 100) + "%";
    }

    public int getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(int outsideColor) {
        this.outsideColor = outsideColor;
    }

    public float getOutsideRadius() {
        return outsideRadius;
    }

    public void setOutsideRadius(float outsideRadius) {
        this.outsideRadius = outsideRadius;
    }

    public int getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(int insideColor) {
        this.insideColor = insideColor;
    }

    public int getProgressTextColor() {
        return progressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
    }

    public float getProgressRadius() {
        return insideRadius+outsideRadius;
    }

    public void setInsideRadius(float insideRadius) {
        this.insideRadius = insideRadius;
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.maxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return progress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            this.progress = maxProgress;
        }else{
            this.progress=progress;
        }
        postInvalidate();
    }

}
