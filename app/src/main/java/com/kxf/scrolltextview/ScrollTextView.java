package com.kxf.scrolltextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

public class ScrollTextView extends View {
    private static final String TAG = "ScrollTextView";
    private long delayMillisFlush = 1000/60;//更新view的时间间隔
    private float index = 0f;//偏移量
    private float indexPerAdd = 0f;//每次增加的偏移量
    private String text = "";//显示的文本
    private float textWith = 0;//显示的文本宽度
    private float textHeight = 0;
    private int speedByCharPerSecond = 1;//每秒滚动多少个字符
    private float space = 70;
    private float drawTextY = 0;
    private int width = 0;
    private int height = 0;
    private Paint paint;

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.scroll_text_view);
        text = ta.getString(R.styleable.scroll_text_view_text);
        speedByCharPerSecond = ta.getInteger(R.styleable.scroll_text_view_speedByCharPerSecond, speedByCharPerSecond);
        Log.e(TAG, "text=" + text);
        Log.e(TAG, "speedByCharPerSecond=" + speedByCharPerSecond);
        ta.recycle();

        if (null == paint){
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(100);
        }
        textWith = getTextWidth(paint, text);
        indexPerAdd = getTextWidth(paint, "a")/60*speedByCharPerSecond;
        textHeight = getTextHeight(paint);
        Paint.FontMetrics fm = paint.getFontMetrics();
        drawTextY = Math.abs(fm.ascent) + Math.abs(fm.descent);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * EXACTLY	精准模式，View需要一个精确值，这个值即为MeasureSpec当中的Size	match_parent
         * AT_MOST	最大模式，View的尺寸有一个最大值，View不可以超过MeasureSpec当中的Size值	wrap_content
         * UNSPECIFIED	无限制，View对尺寸没有任何限制，View设置为多大就应当为多大	一般系统内部使用
         */
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int)getTextWidth(paint, text), (int)getTextHeight(paint));
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int)getTextWidth(paint, text), heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, (int)getTextHeight(paint));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text,0-index,drawTextY,paint);
        canvas.drawText(text,textWith + space -index,drawTextY,paint);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                index = index+indexPerAdd;
                if (index>=textWith + space){
                    index -= textWith + space;
                }
                invalidate();
            }
        }, delayMillisFlush);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * 计算文本高度
     * @param paint
     * @return
     */
    public float getTextHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        Log.e(TAG, "fm.ascent=" + fm.ascent);
        Log.e(TAG, "fm.descent=" + fm.descent);
        Log.e(TAG, "fm.top=" + fm.top);
        Log.e(TAG, "fm.bottom=" + fm.bottom);
        return Math.abs(fm.ascent) + Math.abs(fm.descent) + Math.abs(fm.bottom);
    }

    /**
     * 计算文本宽度
     * @param paint
     * @param str
     * @return
     */
    public float getTextWidth(Paint paint , String str){
        float x = paint.measureText(str);
        return x;
    }

}
